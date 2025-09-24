package com.ray;


public class Vec3 {

    double[] e = new double[3];


    Vec3(){}
    Vec3(double x, double y, double z) {e[0] = x; e[1] = y; e[2] = z;}

    public double x() {return e[0];}
    public double y() {return e[1];}
    public double z() {return e[2];}

    Vec3 negative() { return new Vec3(-e[0], -e[1], -e[2]); }

    Vec3 add(final Vec3 other) {
        return new Vec3(
                e[0] + other.e[0],
                e[1] + other.e[1],
                e[2] + other.e[2]
        );
    }

    public void addSelf(Vec3 v) {
        e[0] += v.x();
        e[1] += v.y();
        e[2] += v.z();
    }

    Vec3 sub(final Vec3 other) {
        return new Vec3(
                e[0] - other.e[0],
                e[1] - other.e[1],
                e[2] - other.e[2]
        );
    }

    Vec3 multiply(double t) {
        return new Vec3(
                e[0] * t,
                e[1] * t,
                e[2] * t
        );
    }

    Vec3 divide(double t) {
        return new Vec3(
                e[0] / t,
                e[1] / t,
                e[2] / t
        );
    }

    double magnitudeSquared(){
        return e[0] * e[0] + e[1] * e[1] + e[2] * e[2];
    }

    double magnitude(){
        return Math.sqrt(magnitudeSquared());
    }

    public Vec3 unitVector() {
        return divide(this.magnitude());
    }

    public double dot(final Vec3 other) {
        return (e[0] * other.e[0]
                + e[1] * other.e[1]
                + e[2] * other.e[2]);
    }

    public Vec3 cross(final Vec3 other) {
        return new Vec3(e[1] * other.e[2] - e[2] * other.e[1],
                -(e[0] * other.e[2] - e[2] * other.e[0]),
                e[0] * other.e[1] - e[1] * other.e[0]);
    }

    @Override
    public String toString(){
        return "vec3(" + e[0] + ", " + e[1] + ", " + e[2] + ")";
    }
}
