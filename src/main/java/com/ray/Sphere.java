package com.ray;

public class Sphere implements Hittable {
    private final Point3 center;
    private final double radius;
    private final double radiusSquared;

    public Sphere(Point3 center, double radius) {
        this.center = center;
        this.radius = radius;
        radiusSquared = radius * radius;
    }

    public boolean hit(Ray r, Interval rayInterval, HitRecord hitRecord) {
        Vec3 oc = center.sub(r.orig);
        double a = r.dir.magnitudeSquared();
        double h = r.dir.dot(oc);
        double c = oc.magnitudeSquared() - radiusSquared;
        double discriminant = h * h - a * c;
        if (discriminant < 0.0) {
            return false;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);


        double root = (h - sqrtDiscriminant) / a;
        if (root <= rayInterval.min || root >= rayInterval.max ) {
            root = (h + sqrtDiscriminant) / a;
            if (root <= rayInterval.min || root >= rayInterval.max) {
                return false;
            }
        }

        hitRecord.t = root;
        hitRecord.p = r.at(root);
        Vec3 outwardNormal = hitRecord.p
                .sub(center)
                .divideSelf(radius);
        hitRecord.setFaceNormal(r, outwardNormal);
        return true;
    }
}
