package com.ray;

import java.io.*;

public class Main {

    static double hitSphere(Ray r, Point3 camCenter, double radius) {
        Vec3 oc = camCenter.sub(r.getOrigin());
        double a = r.getDirection().magnitudeSquared();
        double h = r.getDirection().dot(oc);
        double c = oc.magnitudeSquared() - radius * radius;
        double discriminant = h * h - a * c;

        if (discriminant < 0) {
            return -1.0;
        }
        else
            return (h - Math.sqrt(discriminant))/a;
    }

    static Color rayColor (Ray r, Hittable world){
        HitRecord rec = new HitRecord();
        if (world.hit(r, new Interval(0, Utility.infinity), rec)){
            return new Color(rec.normal.x() + 1, rec.normal.y() + 1, rec.normal.z() + 1).multiply(0.5);
        }

        Vec3 unitRay = r.getDirection().unitVector();
        double a = 0.5 * (unitRay.y() + 1.0);

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

    public static void main(String[] args) {
        long start_time =  System.nanoTime();

        // World
        HittableList world = new HittableList();

        world.add(new Sphere(new Point3(0,0,-1), 0.5));
        world.add(new Sphere(new Point3(0,-100.5,-1), 100));

        Camera cam = new Camera();
        cam.aspectRatio = 16.0/9.0;
        cam.imageWidth = 802;
        cam.samplesPerPixel = 50;
        cam.maxDepth = 15;

        try {
            cam.render(world);
        }
        catch (Exception e) {
            System.err.println("Unable to write to file: " + e.getMessage());
        }

        long end_time = System.nanoTime();
        System.out.println("Time taken: " + (end_time - start_time)/1e9 + " seconds");
    }
}