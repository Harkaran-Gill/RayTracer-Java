package com.ray;

public class HitRecord {
    Point3 p;
    Vec3 normal;
    Material mat;
    double t;
    boolean front_face;

    HitRecord() {}

    void set(HitRecord other) {
        p = other.p;
        normal = other.normal;
        this.mat = other.mat;
        t = other.t;
        front_face = other.front_face;
    }

    void reset() {p = null; normal = null; mat = null; t = 0; front_face = false;}

    void setFaceNormal(Ray r, Vec3 outwardNormal) {
        // Sets the hit record normal vector.
        // NOTE: the parameter `outward_normal` is assumed to have unit length.

        front_face = outwardNormal.dot(
                r.getDirection()) < 0;
        normal = front_face ?
                outwardNormal.copy()
                : outwardNormal.negative();
    }

}
