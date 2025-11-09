package com.ray;

public interface Material {
    boolean scatter (Ray incomingRay, HitRecord rec, Vec3 attenuation, Ray Scattered);
}
