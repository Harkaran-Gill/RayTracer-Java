package com.ray;

public class Lambertian implements Material{
    private final Color albedo;

    public Lambertian(Color albedo) {
        this.albedo  = albedo;
    }

    @Override
    public boolean scatter(Ray incomingRay, HitRecord rec, Color attenuation, Ray scatteredRay ){
        Vec3 scatterDirection = Vec3.randomUnitVector()
                .addSelf(rec.normal);
        if (scatterDirection.nearZero())
            scatterDirection = rec.normal;
        scatteredRay.set(rec.p, scatterDirection);
        attenuation.set(albedo);
        return true;
    }
}
