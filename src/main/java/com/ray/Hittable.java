package com.ray;

public interface Hittable {
    boolean hit(Ray r, double tMin, double tMax, HitRecord hitRecord);
}
