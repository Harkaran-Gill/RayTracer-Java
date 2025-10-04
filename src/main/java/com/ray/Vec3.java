package com.ray;


public class Vec3 {

    double[] e = new double[3];


    Vec3(){}
    Vec3(double x, double y, double z) {e[0] = x; e[1] = y; e[2] = z;}

    public void set(Vec3 other){
        e[0] = other.e[0];
        e[1] = other.e[1];
        e[2] = other.e[2];
    }
    public Vec3 copy(){ return new Vec3(e[0], e[1], e[2]); }

    public double x() {return e[0];}
    public double y() {return e[1];}
    public double z() {return e[2];}

    public Vec3 addSelf(Vec3 other){
        e[0] += other.e[0];
        e[1] += other.e[1];
        e[2] += other.e[2];
        return this;
    }

    public Vec3 subSelf(Vec3 other){
        e[0] -= other.e[0];
        e[1] -= other.e[1];
        e[2] -= other.e[2];
        return this;
    }

    public Vec3 multiplySelf(double t){
        e[0] *= t;
        e[1] *= t;
        e[2] *= t;
        return this;
    }

    public Vec3 divideSelf(double t){
        e[0] /= t;
        e[1] /= t;
        e[2] /= t;
        return this;
    }

    public Vec3 negativeSelf(){
        e[0] = -e[0];
        e[1] = -e[1];
        e[2] = -e[2];
        return this;
    }

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

    public static Vec3 random(){
        return new Vec3(Utility.randomDouble(), Utility.randomDouble(), Utility.randomDouble());
    }

    public static Vec3 random(double min, double max){
        return new Vec3(Utility.randomDouble(min, max), Utility.randomDouble(min, max), Utility.randomDouble(min, max));
    }

    public static Vec3 randomUnitVector(){
        while(true){
            Vec3 rand = random(-1,1);
            double magSquared = rand.magnitudeSquared();
            if (1e-160 < magSquared && magSquared <= 1){
                return rand.divideSelf(Math.sqrt(magSquared));
                //return rand;
            }
        }
    }

    public static Vec3 randomOnHemisphere(Vec3 normal){
        // Tried using vector in Unit Square and saw marginal difference in image
        Vec3 unitSphereVector = randomUnitVector();
        if  (unitSphereVector.dot(normal) > 0){
            return unitSphereVector;
        }
        else
            return unitSphereVector.negativeSelf();
    }

    @Override
    public String toString(){
        return "vec3(" + e[0] + ", " + e[1] + ", " + e[2] + ")";
    }
}
