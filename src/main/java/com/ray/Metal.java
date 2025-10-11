package com.ray;

public class Metal implements Material{
    private final Color albedo;
    private final double fuzz;

    public Metal(Color albedo, double fuzz) {
        this.albedo = albedo;
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }

    public boolean scatter(Ray incomingRay, HitRecord rec, Color attenuation, Ray scatteredRay){
        // Metals reflect light at the same angle
        Vec3 reflectedRay = Vec3.reflect(incomingRay.getDirection(), rec.normal);

        // However, we can introduce fuzz, which adds randomness to the reflectedRay
        if (fuzz > 0.0) {
            reflectedRay = reflectedRay
                    .addSelf(Vec3.randomUnitVector().multiplySelf(fuzz));
        }
        scatteredRay.set(rec.p, reflectedRay);
        attenuation.set(albedo);
        return true;
    }
}
