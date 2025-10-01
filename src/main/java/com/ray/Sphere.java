package com.ray;

public class Sphere implements Hittable {
    private Point3 center;
    private double radius;

    public Sphere(Point3 center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public boolean hit(Ray r, double tMin, double tMax, HitRecord hitRecord) {
        Vec3 oc = center.sub(r.getOrigin());
        double a = r.getDirection().magnitudeSquared();
        double h = r.getDirection().dot(oc);
        double c = oc.magnitudeSquared() - radius * radius;
        double discriminant = h * h - a * c;
        if (discriminant < 0.0) {
            return false;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);


        double root = (h - sqrtDiscriminant) / a;
        if (root <= tMin || root >= tMax ) {
            root = (h + sqrtDiscriminant) / a;
            if (root <= tMin || root >= tMax) {
                return false;
            }
        }

        hitRecord.t = root;
        hitRecord.p = r.at(root);
        Vec3 outwardNormal = (hitRecord.p
                .sub(center)).divide(radius);
        hitRecord.setFaceNormal(r, outwardNormal);
        return true;
    }
}
