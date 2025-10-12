package com.ray;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Camera {
    double aspectRatio     = 16.0/9.0;     // Ratio of image width over height
    int    imageWidth      = 100;          // Rendered image width in pixel count
    int    samplesPerPixel = 10;           // Count of random samples for each pixel
    int    maxDepth        = 10;           // Maximum number of ray bounces into scene

    double vFov     = 90;                           // Vertical view angle (field of view)
    Point3 lookFrom = new Point3(0,0,0);   // Point camera is looking from
    Point3 lookAt   = new Point3(0,0,-1);  // Point camera is looking at
    Vec3   vUp      = new Vec3(0,1,0);     // Camera-relative "up" direction

    double defocusAngle = 0;    // Variation angle of rays through each pixel
    double focusDist    = 10;   // Distance from camera lookFrom point to plane of perfect focus


    private int imageHeight;             // Rendered image height
    private double pixelSampleScale;     // Color scale factor for a sum of pixel samples
    private Point3 cameraCenter;         // Camera center
    private Point3 pixel00Loc;           // Location of pixel 0, 0
    private Vec3 pixelDeltaU;            // Offset to pixel to the right
    private Vec3 pixelDeltaV;            // Offset to pixel below
    private Vec3 u, v, w;                // Camera frame basis vectors
    private Vec3  defocusDiskU;          // Defocus disk horizontal radius
    private Vec3  defocusDiskV;          // Defocus disk vertical radius

    void render (Hittable world){
        initialize();
        System.out.println("P3\n" + imageWidth + ' ' + imageHeight + "\n255\n");

        // TODO: Find out if BufferedWriter performs better than PrintWriter here
        PrintWriter pw;
        try {
            pw = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter("image.ppm")));
        }
        catch (IOException e) {
            System.err.println("Error writing image.ppm " + e.getMessage());
            return;
        }

        pw.println("P3\n" + imageWidth + " "  + imageHeight + "\n255");

        for (int j = 0; j < imageHeight; j++) {  // Rows
            System.out.print("Scanlines remaining: " + (imageHeight-j) + "\r");
            System.out.flush();
            for (int i = 0; i < imageWidth; i++) { // Columns
                Color pixelColor = new Color(0,0,0);

                // This is for producing multiple rays per pixel
                for (int samples = 0; samples < samplesPerPixel; samples++) {
                    Ray r = getRay(i, j);
                    pixelColor.addSelf(rayColor(r, world, maxDepth));
                }

                Color.write_color(pixelColor.multiplySelf(pixelSampleScale), pw);
            }
        }
        pw.close();
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

        Point3 viewportUpperLeft = cameraCenter.sub(viewportU.divide(2))
                .subSelf(viewportV.divide(2))
                .subSelf(w.multiply(focusDist));

        pixel00Loc = viewportUpperLeft.add(pixelDeltaU.divide(2))
                .add(pixelDeltaV.divide(2));

        // Calculate the camera defocus disk basis vectors.
        double defocusRadius = focusDist * Math.tan(Math.toRadians(defocusAngle/2));
        defocusDiskU = u.multiply(defocusRadius);
        defocusDiskV = v.multiply(defocusRadius);
    }

    Ray getRay(int i,int j){
        // Construct a camera ray originating from the defocus disk and directed at a randomly
        // sampled point around the pixel location i, j.
        Vec3 offset = sampleSquare();
        Point3 sampledPoint = pixel00Loc.add(pixelDeltaU.multiply(i + offset.x()))
                .addSelf(pixelDeltaV.multiply(j + offset.y()));

        Point3 rayOrigin = (defocusAngle <= 0) ? cameraCenter: defocusDiskSample();
        Vec3 rayDirection = sampledPoint.subSelf(rayOrigin);

        return new Ray(rayOrigin, rayDirection);
    }

    // getting a random point from the Camera 'lens', this enables depth of field
    Point3 defocusDiskSample(){
        Point3 p = Point3.randomInUnitDisk();
        return cameraCenter.add(defocusDiskU.multiply(p.x()).addSelf(defocusDiskV.multiply(p.y())));
    }

    // getting a random point within the pixel's square
    Vec3 sampleSquare(){
        return new Vec3 (Utility.randomDouble(-0.5,0.5), Utility.randomDouble(-0.5, 0.5), 0);
    }

    // Method used to colour rays and determine the colour of pixels
    Color rayColor(Ray r, Hittable world, int depth) {
        // Base Case 1
        // Returning BLACK colour if number of ray bounces exceeds set threshold
        if (depth <= 0) {
            return new Color(0,0,0);
        }

        // Creating a HitRecord, serves as an output parameter
        HitRecord rec = new HitRecord();
        if (world.hit(r, new Interval(0.001, Utility.infinity), rec)){
            // scattered and attenuation are also output parameters
            Ray scattered = new Ray();
            Color attenuation = new Color();
            if(rec.mat.scatter(r,rec, attenuation, scattered)){
                // Recursive call to compute ray bounces between objects
                return attenuation.multiplySelf(rayColor(scattered, world, depth-1));
            }
        }

        Vec3 unitRay = r.getDirection().unitVector();
        double a = (unitRay.y() + 1.0) * 0.5;

        // Base Case 2
        // Return the colour of sky(which is a linear gradient) if ray doesn't hit any object
        return new Color(1.0,1.0,1.0).multiplySelf(1.0-a)
                .addSelf
                        (new Color(0.5,0.7,1.0).multiplySelf(a));
    }
}
