package com.ray;

import java.io.PrintWriter;

public class Color extends Vec3 {

    public Color() {super();}
    public Color(double x, double y, double z) {super(x, y, z);}
    public Color(Vec3 v) {super(v.x(), v.y(), v.z());}

    @Override
    public Color multiplySelf(double t){
        // The cast here would not normally work but since the super.multiplySelf returns 'this'
        // it is actually returning a Vec3 reference to Color object in memory, which can be
        // then successfully cast. Polymorphism for the WIN
        return (Color) super.multiplySelf(t);
    }

    @Override
    public Color multiplySelf(Vec3 v){
        return (Color) super.multiplySelf(v);
    }

    @Override
    public Color addSelf(Vec3 v){
        return (Color) super.addSelf(v);
    }

    @Override
    public Color add(Vec3 v) {
        return new Color(super.add(v));
    }

    @Override
    public Color multiply(double t){
        Vec3 result = super.multiply(t);
        return new Color(result);
    }

    @Override
    public Color multiply(Vec3 v){
        return new Color(super.multiply(v));
    }

    // Colour utility methods
    public static Color random(){
        return new Color(Utility.randomDouble(), Utility.randomDouble(), Utility.randomDouble());
    }

    public static double linearToGamma(double linear){
        // Gamma adjusting the pixel colours. Displays don't respond linearly
        // to colours/brightness, they assume pre-adjusted colours
        if(linear > 0)
            return Math.sqrt(linear);
        else return 0;
    }

    public static void write_color(Color pixel_color, PrintWriter pw) {
        var r = pixel_color.x();
        var g = pixel_color.y();
        var b = pixel_color.z();

        r = linearToGamma(r);
        g = linearToGamma(g);
        b = linearToGamma(b);

        final Interval intensity = new Interval(0, 0.999);
        // Normalize [0,1] to [0. 255]
        int rByte = (int)(256 * intensity.clamp(r));
        int gByte = (int)(256 * intensity.clamp(g));
        int bByte = (int)(256 * intensity.clamp(b));

        pw.write(rByte + " " + gByte + " " + bByte + "\n");
    }
}
