package com.ray;

import java.io.*;

public class Main {

    static Color rayColor (Ray r){
        Vec3 unitRay = r.getDirection().unitVector();
        double a = 0.5 * (unitRay.y() + 1.0);
        //System.out.println(a);
        return new Color(1.0,1.0,1.0).multiply(1.0-a)
                .add(new Color(0.5,0.7,1.0).multiply(a));
    }

    public static void main(String[] args) {
        long start_time =  System.nanoTime();

        double aspectRatio = 16.0/9.0;
        int imageWidth = 400;

        int imageHeight = Math.max(1, (int)(imageWidth / aspectRatio));

        double focalLength = 1.0;
        double viewportHeight = 2.0;
        double viewportWidth = viewportHeight * ((double)imageWidth / imageHeight);
        Point3 cameraCenter = new Point3(0,0,0);

        Vec3 viewportU = new Vec3(viewportWidth, 0, 0);
        Vec3 viewportV = new Vec3(0, -viewportHeight, 0);

        Vec3 pixelDeltaU = viewportU.divide(imageWidth);
        Vec3 pixelDeltaV = viewportV.divide(imageHeight);

        Point3 viewportUpperLeft =
                cameraCenter.sub(new Point3(viewportWidth/2, -viewportHeight/2, focalLength));
        Point3 pixel00Loc = viewportUpperLeft.add(pixelDeltaU.divide(2))
                .add(pixelDeltaV.divide(2));



        System.out.println("P3\n" + imageWidth + ' ' + imageHeight + "\n255\n");

        PrintWriter pw;
        try {
            pw = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter("image.ppm")));
        }
        catch (IOException e) {
            System.out.println("Error writing to file " + e.getMessage());
            return;
        }

        pw.println("P3\n" + imageWidth + " "  + imageHeight + "\n255");

        for (int j = 0; j < imageHeight; j++) {
            System  .out.print("Scanlines remaining: " + (imageHeight-j) + "\r");
            System.out.flush();
            for (int i = 0; i < imageWidth; i++) {
                Point3 pixelCenter  = pixel00Loc.add(pixelDeltaU.multiply(i))
                        .add(pixelDeltaV.multiply(j));
                Vec3 rayDirection = pixelCenter.sub(cameraCenter);

                Ray r = new Ray(cameraCenter, rayDirection);
                Color pixelColor = rayColor(r);

                Color.write_color(pixelColor, pw);
            }
        }
        pw.close();
        long end_time = System.nanoTime();
        System.out.println("Time taken: " + (end_time - start_time)/1e9 + " seconds");
    }
}