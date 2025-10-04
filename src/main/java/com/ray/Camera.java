package com.ray;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Camera {
    double aspectRatio  = 16.0/9.0;
    int imageWidth      = 100;
    int samplesPerPixel = 10;
    int maxDepth = 10;

    private int imageHeight;
    private double pixelSampleScale;
    private Point3 cameraCenter;
    private Point3 pixel00Loc;
    private Vec3 pixelDeltaU;
    private Vec3 pixelDeltaV;

    void render (Hittable world) throws IOException{
        initialize();
        System.out.println("P3\n" + imageWidth + ' ' + imageHeight + "\n255\n");

        // TODO: Find out if BufferedWriter performs better than PrintWriter here
        PrintWriter pw;
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter("image.ppm"));
        pw = new PrintWriter(bw);

        pw.println("P3\n" + imageWidth + " "  + imageHeight + "\n255");

        for (int j = 0; j < imageHeight; j++) {
            System.out.print("Scanlines remaining: " + (imageHeight-j) + "\r");
            System.out.flush();
            for (int i = 0; i < imageWidth; i++) {

                Color pixelColor = new Color(0,0,0);

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

        // determine Viewport dimensions
        double focalLength = 1.0;
        double viewportHeight = 2.0;
        double viewportWidth = viewportHeight * ((double)imageWidth / imageHeight);

        pixelSampleScale = 1.0/samplesPerPixel;

        cameraCenter = new Point3(0,0,0);

        Vec3 viewportU = new Vec3(viewportWidth, 0, 0);
        Vec3 viewportV = new Vec3(0, -viewportHeight, 0);

        pixelDeltaU = viewportU.divide(imageWidth);
        pixelDeltaV = viewportV.divide(imageHeight);

        Point3 viewportUpperLeft =
                cameraCenter.sub(new Point3(viewportWidth/2, -viewportHeight/2, focalLength));
        pixel00Loc = viewportUpperLeft.add(pixelDeltaU.divide(2))
                .add(pixelDeltaV.divide(2));
    }

    Ray getRay(int i,int j){
        Vec3 offset = sampleSquare();
        Point3 sampledPoint = pixel00Loc.add(pixelDeltaU.multiply(i + offset.x()))
                .addSelf(pixelDeltaV.multiply(j + offset.y()));

        Point3 rayOrigin = cameraCenter;
        Vec3 rayDirection = sampledPoint.subSelf(rayOrigin);

        return new Ray(rayOrigin, rayDirection);
    }

    Vec3 sampleSquare(){
        return new Vec3 (Utility.randomDouble(-.5,0.5), Utility.randomDouble(-0.5, 0.5), 0);
    }

    Color rayColor(Ray r, Hittable world, int depth) {
        if (depth <= 0) {
            return new Color(0,0,0);
        }
        HitRecord rec = new HitRecord();
        if (world.hit(r, new Interval(0.001, Utility.infinity), rec)){
            Vec3 direction = Vec3.randomOnHemisphere(rec.normal)
                    .addSelf(rec.normal);

            return rayColor(new Ray(rec.p, direction), world, depth-1)
                    .multiplySelf(0.5);
            //return new Color(direction.x() + 1, direction.y() + 1, direction.z() + 1).multiplySelf(0.5);
        }

        Vec3 unitRay = r.getDirection().unitVector();
        double a = (unitRay.y() + 1.0) * 0.5;

        // The following are two different return strategies, yet to find which is more efficient

        return new Color(1.0,1.0,1.0)
                .multiplySelf(1.0-a)
                .addSelf(new Color(0.5,0.7,1.0)
                        .multiplySelf(a));

        /*Color temp = new Color();
        temp.addSelf(white.multiply(1.0-a));
        temp.addSelf(skyBlue.multiply(a));
        return temp;*/
    }
}
