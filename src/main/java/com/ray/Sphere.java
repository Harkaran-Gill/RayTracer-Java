package com.ray;

public class Sphere implements Hittable {
    private final Point3 center;
    private final double radius;
    private final double radiusSquared;
    private final Material mat;

    public Sphere(Point3 center, double radius, Material mat) {
        this.center = center;
        this.radius = Math.max(0, radius);
        this.mat = mat;
        radiusSquared = radius * radius;
    }

    @Override
    public boolean hit(Ray r, Interval rayInterval, HitRecord rec) {
        Vec3 oc = center.sub(r.getOrigin());
        double a = r.getDirection().magnitudeSquared();
        double h = r.getDirection().dot(oc);
        double c = oc.magnitudeSquared() - radiusSquared;
        double discriminant = h * h - a * c;
        if (discriminant < 0.0) {
            return false;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);


        double root = (h - sqrtDiscriminant) / a;
        if (root <= rayInterval.min || root >= rayInterval.max) {
            root = (h + sqrtDiscriminant) / a;
            if (root <= rayInterval.min || root >= rayInterval.max) {
                return false;
            }
        }

        rec.t = root;
        rec.p = r.at(root);
        Vec3 outwardNormal = rec.p
                .sub(center)
                .divideSelf(radius);
        rec.setFaceNormal(r, outwardNormal);
        rec.mat = mat;
        return true;
    }
}
