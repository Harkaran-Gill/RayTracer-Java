package com.ray;

public class Sphere implements Hittable {
    private final Point3 center;        // Center of the Sphere
    private final double radius;        // Radius if the Sphere
    private final double radiusSquared; // Radius squared (Reduces redundant calculations)
    private final Material mat;         // The material of sphere, responsible for HOW a ray reflected/refracted
    private final AABB bBox;            // The bounding box around the sphere

    // Constructor
    public Sphere(Point3 center, double radius, Material mat) {
        this.center = center;
        this.radius = Math.max(0, radius);
        this.mat = mat;
        radiusSquared = radius * radius;

        Vec3 radiusVec = new Vec3(radius, radius, radius);
        bBox = new AABB(center.sub(radiusVec), center.add(radiusVec));
    }

    // The actual method that calculates intersection with Spheres present in the scene
    // We are just solving a simplified quadratic equation to find the t(time) at which ray
    // intersects with the sphere
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

        // Storing the information about the ray-sphere intersection in the HitRecord object
        rec.t = root;
        rec.p = r.at(root);
        Vec3 outwardNormal = rec.p
                .sub(center)
                .divideSelf(radius);
        rec.setFaceNormal(r, outwardNormal);
        rec.mat = mat;
        return true;
    }

    // Returns the box that encloses the sphere
    @Override
    public AABB boundingBox() { return bBox; }
}
