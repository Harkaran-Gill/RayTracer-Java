package com.ray;

import java.io.*;
public class Main {

    public static void main(String[] args) {
        long start_time =  System.nanoTime();

        // World with all the objects
        HittableList world = new HittableList();
        Camera cam = new Camera();

        // Different Scenes that can be chosen to render
        //scene1(world, cam);
        scene2(world, cam);
        world.initializeArray();
        cam.render(world);

        long end_time = System.nanoTime();
        System.out.println("Time taken: " + (end_time - start_time)/1e9 + " seconds");
    }

    // Scene1, the cover scene in the README file
    public static void scene1(HittableList world, Camera cam) {
        Material groundMaterial = new Lambertian(new Color(0.5,0.5,0.5));
        world.add(new Sphere(new Point3(0,-1000,0), 1000, groundMaterial));

        // Loops to determine the position of the spheres, the material is chosen randomly
        // based on a fixed probability (You can change this in the below if statements).
        for (int a = -11; a < 11; a++){
            for (int b = -11; b <11; b++){
                double chooseMaterial = 0;//Utility.randomDouble();
                Point3 center = new Point3(a + 0.9 * Utility.randomDouble(), 0.2, b + 0.9 * Utility.randomDouble());

                if (center.sub(new Point3(4, 0.2, 0)).magnitude() > 0.9){
                    Material sphereMaterial;

                    if (chooseMaterial < 0.8){
                        // Diffuse or Lambertian or Matte
                        Color albedo = Color.random().multiply(Color.random());
                        sphereMaterial = new Lambertian(albedo);
                        world.add(new Sphere(center,0.2, sphereMaterial));
                    }
                    else if (chooseMaterial < 0.95){
                        // Metal
                        Color albedo = Color.random().multiply(Color.random());
                        double fuzz = Utility.randomDouble(0, 0.4);
                        sphereMaterial = new Metal(albedo, fuzz);
                        world.add(new Sphere(center,0.2, sphereMaterial));
                    }
                    else{
                        // Dielectric or Glass
                        sphereMaterial = new Dielectric(1.5);
                        world.add(new Sphere(center,0.2, sphereMaterial));
                    }
                }
            }
        }
        // Big spheres in the middle
        Material material1 = new Dielectric(1.5);
        world.add(new Sphere(new Point3(0,1,0), 1.0, material1));

        Material material2 = new Lambertian(new Color(0.4,0.2,0.1));
        world.add(new Sphere(new Point3(-4,1,0), 1.0, material2));

        Material material3 = new Metal(new Color(0.7,0.6,0.2), 0.0);
        world.add(new Sphere(new Point3(4,1,0), 1.0, material3));

        cam.aspectRatio      = 16.0 / 9.0;
        cam.imageWidth       = 800;
        cam.samplesPerPixel  = 50;
        cam.maxDepth         = 10;

        cam.vFov     = 20;
        cam.lookFrom = new Point3(13,2,3);
        cam.lookAt   = new Point3(0,0,0);
        cam.vUp      = new Vec3(0,1,0);

        cam.defocusAngle = 0;
        cam.focusDist    = 10.0;

    }

    // A simpler scene for faster rendering
    public static void scene2(HittableList world, Camera cam){
        Material materialGround = new Lambertian(new Color(0.8,0.8,0.0));
        Material materialCenter = new Lambertian(new Color(0.1,0.2,0.5));
        //Material materialLeft   = new Metal(new Color(0.8,0.8,0.8), 0.0);
        Material materialLeft = new Dielectric(1.5);
        Material materialBubble = new Dielectric(1.0/1.5);
        Material materialRight  = new Metal(new Color(0.8,0.6,0.2), 0.5);

        world.add(new Sphere(new Point3(0,-100.5,-1.0), 100, materialGround));
        world.add(new Sphere(new Point3(0,0.0,-1.2), 0.5, materialCenter));
        world.add(new Sphere(new Point3(-1.0,0.0,-1.0), 0.5, materialLeft));
        world.add(new Sphere(new Point3(-1.0,0.0,-1.0), 0.4, materialBubble));
        world.add(new Sphere(new Point3(1.0,0.0,-1.0), 0.5, materialRight));

        cam.aspectRatio     = 16.0/9.0;
        cam.imageWidth      = 400;
        cam.samplesPerPixel = 50;
        cam.maxDepth        = 10;

        cam.vFov     = 30;
        cam.lookFrom = new Point3(-2,2,1);
        cam.lookAt   = new Point3(0,0,-1);
        cam.vUp      = new Point3(0,1,0);

        cam.defocusAngle = 0.0;
        cam.focusDist    = 3.4;
    }
}