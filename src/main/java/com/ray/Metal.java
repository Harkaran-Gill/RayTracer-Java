package com.ray;

public class Metal implements Material{
    private final Color albedo;
    private final double fuzz;

    public Metal(Color albedo, double fuzz) {
        this.albedo = albedo;
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }

    public boolean scatter(Ray incomingRay, HitRecord rec, Color attenuation, Ray scatteredRay){
        Vec3 reflected = Vec3.reflect(incomingRay.dir, rec.normal);
        if (fuzz > 0.0) {
            reflected = reflected
                    .addSelf(Vec3.randomUnitVector()
                            .multiplySelf(fuzz));
        }
        scatteredRay.set(rec.p, reflected);
        attenuation.set(albedo);
        return true;
    }
}
