package com.ray;

public class Metal implements Material{
    private final Color albedo;

    public Metal(Color albedo) {this.albedo = albedo;}

    public boolean scatter(Ray incomingRay, HitRecord rec, Color attenuation, Ray scatteredRay){
        Vec3 reflected = Vec3.reflect(incomingRay.dir, rec.normal);
        scatteredRay.set(rec.p, reflected);
        attenuation.set(albedo);
        return true;
    }
}
