package com.ray;

public class Dielectric implements Material{
    private final double refractiveIndex;

    public Dielectric(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }

    @Override
    public boolean scatter(Ray incomingRay, HitRecord rec, Color attenuation, Ray scattered){
        attenuation.set(new Color(1.0, 1.0, 1.0));
        double ri = rec.front_face ? 1.0/refractiveIndex : refractiveIndex;

        Vec3 unitDirection = incomingRay.dir.unitVector();
        Vec3 refracted = Vec3.refract(unitDirection, rec.normal, ri);

        scattered.set(rec.p, refracted);
        return true;
    }
}
