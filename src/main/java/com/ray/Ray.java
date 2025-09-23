package com.ray;

public class Ray {
    private Point3 orig;
    private Vec3 dir;

    public Ray(){}
    public Ray(Point3 origin, Vec3 direction) {orig = origin; dir = direction;}

    public Point3 getOrigin() {return orig;}
    public Vec3 getDirection() {return dir;}

    public Point3 at(double t){
        return orig.add(dir.multiply(t));
    }
}
