package com.ray;

public class Point3 extends Vec3{
    public Point3() {super();}
    public Point3(double x, double y, double z) {super(x, y, z);}
    public Point3 (Vec3 vec) {super(vec.x(), vec.y(), vec.z());}

    public Point3 addSelf(Vec3 other){
        return (Point3) super.addSelf(other);
    }

    @Override
    public Point3 add(Vec3 other) {
        Vec3 result = super.add(other);
        return new Point3(result);
    }

    @Override
    public Point3 sub(Vec3 other) {
        Vec3 result = super.sub(other);
        return new Point3(result);
    }

    public void subSelf(Vec3 other) {
        e[0] -= other.e[0];
        e[1] -= other.e[1];
        e[2] -= other.e[2];
    }

    public void set(Vec3 other) {
        e[0] = other.x();
        e[1] = other.y();
        e[2] = other.z();
    }
}
