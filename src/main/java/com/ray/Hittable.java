package com.ray;

public interface Hittable {
    boolean hit(Ray r, Interval rayInterval, HitRecord hitRecord);
}
