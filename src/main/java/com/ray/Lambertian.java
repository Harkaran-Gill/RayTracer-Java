package com.ray;

public class Lambertian implements Material{
    private final Color albedo;

    public Lambertian(Color albedo) {
        this.albedo  = albedo;
    }

    @Override
    public boolean scatter(Ray incomingRay, HitRecord rec, Color attenuation, Ray scatteredRay ){
        // This method randomly scatters rays that hit a Lambertian Material

        Vec3 scatterDirection = Vec3.randomUnitVector()   // Adding random Vector to the normal of the
                .addSelf(rec.normal);                     // ray-object intersection
        if (scatterDirection.nearZero())
            scatterDirection = rec.normal;
        scatteredRay.set(rec.p, scatterDirection);
        attenuation.set(albedo);
        return true;
    }
}
