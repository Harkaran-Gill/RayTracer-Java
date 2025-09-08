package com.ray;


import java.io.*;

public class Main {
    static int image_width = 256;
    static int image_height = 256;  //(int)(image_width * 9.0/16.0);

    public static void main(String[] args) throws IOException {
        System.out.println("P3\n" + image_width + ' ' + image_height + "\n255\n");

        PrintWriter pw =
                new PrintWriter(
                        new BufferedWriter(
                                new FileWriter("image.ppm")));

        pw.println("P3\n" + image_width + " "  + image_height + "\n255");

        for (int j = 0; j < image_height; j++) {
            System.out.print("Scanlines remaining: " + (image_height-j) + "\r");
            System.out.flush();

            for (int i = 0; i < image_width; i++) {
                float r = (float) i / (image_width-1);
                float g = (float) j / (image_height-1);
                float b = 0;

                int rbyte = (int)(255.999 * r);
                int gbyte = (int)(255.999 * g);
                int bbyte = 0;
                pw.print(rbyte + " " + gbyte + " " + bbyte +"\n");
            }
        }
        pw.close();
    }
}