package com.ray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Camera {
    protected int TILE_SIZE = 32;
    public int nThreads    = 0;
    private ExecutorService executor;

    double aspectRatio     = 16.0/9.0;     // Ratio of image width over height
    int    imageWidth      = 100;          // Rendered image width in pixel count
    int    samplesPerPixel = 10;           // Count of random samples for each pixel
    int    maxDepth        = 10;           // Maximum number of ray bounces into scene

    double vFov     = 90;                           // Vertical view angle (field of view)
    Vec3 lookFrom = new Vec3(0,0,0);   // Vec camera is looking from
    Vec3 lookAt   = new Vec3(0,0,-1);  // Vec camera is looking at
    Vec3   vUp      = new Vec3(0,1,0);     // Camera-relative "up" direction

    double defocusAngle = 0;    // Variation angle of rays through each pixel
    double focusDist    = 10;   // Distance from camera lookFrom Vec to plane of perfect focus


    private int imageHeight;             // Rendered image height
    private double pixelSampleScale;     // Vec3 scale factor for a sum of pixel samples
    private Vec3 cameraCenter;         // Camera center
    private Vec3 pixel00Loc;           // Location of pixel 0, 0
    private Vec3 pixelDeltaU;            // Offset to pixel to the right
    private Vec3 pixelDeltaV;            // Offset to pixel below
    private Vec3 u, v, w;                // Camera frame basis vectors
    private Vec3  defocusDiskU;          // Defocus disk horizontal radius
    private Vec3  defocusDiskV;          // Defocus disk vertical radius

    private BufferedImage img;
    private static final Vec3 BLACK = new Vec3(0, 0, 0);

    void multiThreadRender(Hittable world){
        initialize();
        List<Callable<Void>> tasks = new ArrayList<>();
        AtomicInteger tilesDone = new AtomicInteger(0);
        int tilesX = (imageWidth + TILE_SIZE - 1) / TILE_SIZE;
        int tilesY = (imageHeight + TILE_SIZE - 1) / TILE_SIZE;
        int totalTiles = tilesX * tilesY;

        for (int ty = 0; ty < tilesY; ty++) {
            for (int tx = 0; tx < tilesX; tx++) {
                final int x0 = tx * TILE_SIZE;
                final int y0 = ty * TILE_SIZE;
                final int x1 = Math.min(imageWidth, x0+TILE_SIZE);
                final int y1 = Math.min(imageHeight, y0+TILE_SIZE);

                tasks.add(() -> {
                    renderTile(world, x0, y0, x1, y1);
                    int done = tilesDone.incrementAndGet();
                    //if (done % Math.max(1, totalTiles/20) == 0)
                        System.out.printf("Tiles rendered %d/%d \r", done, totalTiles);
                    return null;
                });
            }
        }

        boolean terminated = true;
        try {
            executor.invokeAll(tasks);
            executor.shutdown();
            terminated = executor.awaitTermination(30, TimeUnit.MINUTES);
        }
        catch (InterruptedException e) {
            System.err.println("Program Interrupted" + e.getMessage());
            System.exit(1);
        }
        if (!terminated){
            System.out.println("Program Timed Out");
        }

        try {
            ImageIO.write(img, "png", new File("render.png"));
        }
        catch (IOException e) {
            System.err.println("Error writing render.png " + e.getMessage());
        }
        System.out.println("Wrote render.png");
    }

    void renderTile(Hittable world, int x0, int y0, int x1, int y1){
        for (int j = y0; j < y1; j++){
            for (int i = x0; i < x1; i++){
                Vec3 pixelColor = new Vec3(0,0,0);

                // This is for producing multiple rays per pixel
                for (int samples = 0; samples < samplesPerPixel; samples++) {
                    Ray r = getRay(i, j);
                     {
                        pixelColor.addSelf(rayColor(r, world, maxDepth));
                    }
                }
                 {
                    Color.writePNG(img, pixelColor.multiplySelf(pixelSampleScale), i, j);
                }
            }
        }
    }

    private void initialize(){
        // Calculate the image height, and ensure that it's at least 1.
        imageHeight = Math.max(1, (int)(imageWidth / aspectRatio));

        Vec3 lookingDirection = lookFrom.sub(lookAt);

        // Unused Variable
        // double focalLength = lookingDirection.magnitude();

        // determine Viewport dimensions

        double theta = Math.toRadians(vFov);
        double h = Math.tan(theta/2);
        double viewportHeight = 2.0 * h * focusDist;
        double viewportWidth = viewportHeight * ((double)imageWidth / imageHeight);

        pixelSampleScale = 1.0/samplesPerPixel;

        cameraCenter = lookFrom;

        // Calculate u, v, w
        w = lookingDirection.unitVector();
        u = Vec3.cross(vUp, w).unitVector();
        v = Vec3.cross(w, u);

        // Calculate the vectors across the horizontal and down the vertical viewport edges.
        Vec3 viewportU = u.multiply(viewportWidth);
        Vec3 viewportV = v.negative().multiplySelf(viewportHeight);

        pixelDeltaU = viewportU.divide(imageWidth);
        pixelDeltaV = viewportV.divide(imageHeight);

        Vec3 viewportUpperLeft = cameraCenter.sub(viewportU.divide(2))
                .subSelf(viewportV.divide(2))
                .subSelf(w.multiply(focusDist));

        pixel00Loc = viewportUpperLeft.add(pixelDeltaU.divide(2))
                .add(pixelDeltaV.divide(2));

        // Calculate the camera defocus disk basis vectors.
        double defocusRadius = focusDist * Math.tan(Math.toRadians(defocusAngle/2));
        defocusDiskU = u.multiply(defocusRadius);
        defocusDiskV = v.multiply(defocusRadius);


        img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        // initialize Executor service and nThreads(if not set manually by the user)
        if(nThreads == 0)
            nThreads = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(nThreads);
    }

    Ray getRay(int i,int j){
        // Construct a camera ray originating from the defocus disk and directed at a randomly
        // sampled Vec around the pixel location i, j.
        Vec3 offset = sampleSquare();
        Vec3 sampledVec = (pixelDeltaU.multiply(i + offset.x()))
                .addSelf(pixelDeltaV.multiply(j + offset.y())).addSelf(pixel00Loc);

        Vec3 rayOrigin = (defocusAngle <= 0) ? cameraCenter: defocusDiskSample();
        Vec3 rayDirection = sampledVec.subSelf(rayOrigin);

        return new Ray(rayOrigin, rayDirection);
    }

    // getting a random Vec from the Camera 'lens', this enables depth of field
    Vec3 defocusDiskSample(){
        Vec3 p = Vec3.randomInUnitDisk();
        return cameraCenter.add(defocusDiskU.multiply(p.x()).addSelf(defocusDiskV.multiply(p.y())));
    }

    // getting a random Vec within the pixel's square
    Vec3 sampleSquare(){
        return new Vec3 (Utility.randomDouble(-0.5,0.5), Utility.randomDouble(-0.5, 0.5), 0);
    }

    // Method used to colour rays and determine the colour of pixels
    Vec3 rayColor(Ray r, Hittable world, int depth) {
        // Base Case 1
        // Returning BLACK colour if number of ray bounces exceeds set threshold
        if (depth <= 0) {
            return BLACK;
        }

        // Creating a HitRecord, serves as an output parameter
        HitRecord rec = new HitRecord();
        if (world.hit(r, new Interval(0.001, Utility.infinity), rec)) {
            // scattered and attenuation are also output parameters
            Ray scattered = new Ray();
            Vec3 attenuation = new Vec3();
            if(rec.mat.scatter(r,rec, attenuation, scattered)){
                // Recursive call to compute ray bounces between objects
                return attenuation.multiplySelf(rayColor(scattered, world, depth-1));
            }
        }

        Vec3 unitRay = r.getDirection().unitVector();
        double a = (unitRay.y() + 1.0) * 0.5;

        // Base Case 2
        // Return the colour of sky(which is a linear gradient) if ray doesn't hit any object
        return new Vec3(1.0,1.0,1.0).multiplySelf(1.0-a)
                .addSelf
                        (new Vec3(0.5,0.7,1.0).multiplySelf(a));
    }
}
