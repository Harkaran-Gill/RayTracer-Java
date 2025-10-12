package com.ray;


public class Vec3 {

    private final double[] e = new double[3];


    Vec3(){}
    Vec3(double x, double y, double z) {e[0] = x; e[1] = y; e[2] = z;}



    // Getter methods
    public double x() {return e[0];}
    public double y() {return e[1];}
    public double z() {return e[2];}
    public Vec3 copy(){ return new Vec3(e[0], e[1], e[2]); }

    // Set method
    public void set(Vec3 other){
        e[0] = other.e[0];
        e[1] = other.e[1];
        e[2] = other.e[2];
    }

    // Vector arithmetic methods
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

    public Vec3 multiplySelf(Vec3 other){
        e[0] *= other.e[0];
        e[1] *= other.e[1];
        e[2] *= other.e[2];
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

    Vec3 multiply(Vec3 other) {
        return new Vec3(e[0] * other.e[0],
                e[1] * other.e[1],
                e[2] * other.e[2]);
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

    public boolean nearZero(){
        double s = 1e-8;
        return (Math.abs(e[0]) < s && Math.abs(e[1]) < s && Math.abs(e[2]) < s);
    }

    public double dot(final Vec3 other) {
        return (e[0] * other.e[0]
                + e[1] * other.e[1]
                + e[2] * other.e[2]);
    }

    public static Vec3 cross(Vec3 v1, Vec3 v2){
        return new Vec3(v1.e[1] * v2.e[2] - v1.e[2] * v2.e[1],
                -(v1.e[0] * v2.e[2] - v1.e[2] * v2.e[0]),
                v1.e[0] * v2.e[1] - v1.e[1] * v2.e[0]);
    }

    // Vector Utility methods
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
        // Returns a random vector within unit hemisphere
        // from the point of ray-material intersection
        Vec3 unitSphereVector = randomUnitVector();
        if  (unitSphereVector.dot(normal) > 0){
            return unitSphereVector;
        }
        else
            return unitSphereVector.negativeSelf();
    }

    public static Vec3 reflect(Vec3 v, Vec3 n){
        // n is assumed to be a unit vector
        // Formula: v - n * (2 * v.n) v = incoming ray, n = normal
        return v.sub(n.multiply(2 * v.dot(n)));

    }

    public static Vec3 refract(Vec3 uv, Vec3 n, double cosTheta, double etaIOverEtaT){
        // etaIOverEtaT is basically refractive index
        Vec3 rOutPerp = (n.multiply(cosTheta)
                .addSelf(uv))
                .multiplySelf(etaIOverEtaT); // rOutPerp = etaIOverEtaT * ( uv + (-uv.n)n)
        Vec3 rOutParallel = n.multiply(
                -Math.sqrt(1.0 - rOutPerp.magnitudeSquared()));
        return rOutPerp.addSelf(rOutParallel);
    }

    @Override
    public String toString(){
        return "vec3(" + e[0] + ", " + e[1] + ", " + e[2] + ")";
    }
}
