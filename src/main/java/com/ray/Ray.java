package com.ray;

public class Ray {
    private Point3 orig;    // Origin point of the Ray
    private Vec3 dir;       // Direction the ray is pointing towards

    //Constructors
    public Ray(){}
    public Ray(Point3 origin, Vec3 direction) {orig = origin; dir = direction;}

    // Set method
    public void set(Point3 origin, Vec3 direction){this.orig = origin; dir = direction;}

    // Getter methods
    public Point3 getOrigin() {return orig;}
    public Vec3 getDirection() {return dir;}

    // Determine where the ray is at the given Real Number
    public Point3 at(double t){
        return orig.add(dir.multiply(t));
    }
}
