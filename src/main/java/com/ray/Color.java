package com.ray;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Color extends Vec3 {

    public Color() {super();}
    public Color(double x, double y, double z) {super(x, y, z);}
    public Color(Vec3 v) {super(v.x(), v.y(), v.z());}

    public Color add(Vec3 v) {
        return new Color(super.add(v));
    }

    public Color multiply(double t){
        Vec3 result = super.multiply(t);
        return new Color(result);
    }

    public static void write_color(Color pixel_color, PrintWriter pw) {
        var r = pixel_color.x();
        var g = pixel_color.y();
        var b = pixel_color.z();

        // Normalize [0,1] to [0. 255]
        int rbyte = (int)(255.999 * r);
        int gbyte = (int)(255.999 * g);
        int bbyte = (int)(255.999 * b);

        pw.write(rbyte + " " + gbyte + " " + bbyte + "\n");

    }
}
