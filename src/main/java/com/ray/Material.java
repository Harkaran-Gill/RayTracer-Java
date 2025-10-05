package com.ray;

public interface Material {
    boolean scatter (Ray incomingRay, HitRecord rec, Color attenuation, Ray Scattered);
}
