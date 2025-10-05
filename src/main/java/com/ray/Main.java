package com.ray;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        long start_time =  System.nanoTime();

        // World
        HittableList world = new HittableList();

        Material materialGround = new Lambertian(new Color(0.8,0.8,0.0));
        Material materialCenter = new Lambertian(new Color(0.1,0.2,0.5));
        Material materialLeft   = new Metal(new Color(0.8,0.8,0.8));
        Material materialRight  = new Metal(new Color(0.8,0.6,0.2));

        world.add(new Sphere(new Point3(0,-100.5,-1.0), 100, materialGround));
        world.add(new Sphere(new Point3(0,0.0,-1.2), 0.5, materialCenter));
        world.add(new Sphere(new Point3(-1.0,0.0,-1.0), 0.5, materialLeft));
        world.add(new Sphere(new Point3(1.0,0.0,-1.0), 0.5, materialRight));


        Camera cam = new Camera();
        cam.aspectRatio = 16.0/9.0;
        cam.imageWidth = 1920;
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