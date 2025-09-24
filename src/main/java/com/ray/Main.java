package com.ray;

import java.io.*;

public class Main {
    static final Color red = new Color(1.0, 0, 0);
    static final Color white = new Color(1.0, 1.0, 1.0);
    static final Color skyBlue = new Color(0.5, 0.7, 1.0);

    private static final Color tempColor = new Color();
    private static final Point3 tempVec = new Point3();
    private static final Point3 tempVec2 = new Point3();

    static boolean hitSphere(Ray r, Point3 camCenter, double radius) {
        tempVec2.set(camCenter);
        tempVec2.subSelf(r.getOrigin());
        Vec3 oc = tempVec2;
        var a = r.getDirection().magnitudeSquared();
        var b = -2.0 * r.getDirection().dot(oc);
        var c = oc.magnitudeSquared() - radius * radius;
        var discriminant = b * b - 4.0 * a * c;

        return discriminant >= 0.0;
    }

    static Color rayColor (Ray r){
        if (hitSphere(r, new Point3(0,0,-1), 0.5)){
            return red;
        }

        Vec3 unitRay = r.getDirection().unitVector();
        double a = 0.5 * (unitRay.y() + 1.0);

        tempColor.set(0,0,0);
        tempColor.addSelf(white.multiply(1.0-a));
        tempColor.addSelf(skyBlue.multiply(a));
        return tempColor;
    }

    public static void main(String[] args) {
        long start_time =  System.nanoTime();

        double aspectRatio = 16.0/9.0;
        int imageWidth = 1920;

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

        Color pixelColor;
        for (int j = 0; j < imageHeight; j++) {
            System.out.print("Scanlines remaining: " + (imageHeight-j) + "\r");
            System.out.flush();
            for (int i = 0; i < imageWidth; i++) {
                tempVec.set(pixel00Loc);
                tempVec.addSelf(pixelDeltaU.multiply(i));
                tempVec.addSelf(pixelDeltaV.multiply(i));

                // The above code is replacement for below, reduces memory allocations
                //Point3 pixelCenter  = pixel00Loc.add(pixelDeltaU.multiply(i))
                //        .add(pixelDeltaV.multiply(j));

                Vec3 rayDirection = tempVec.sub(cameraCenter); // tempVec is pixelCenter



                Ray r = new Ray(cameraCenter, rayDirection);
                pixelColor = rayColor(r);

                Color.write_color(pixelColor, pw);
            }
        }
        pw.close();
        long end_time = System.nanoTime();
        System.out.println("Time taken: " + (end_time - start_time)/1e9 + " seconds");
    }
}