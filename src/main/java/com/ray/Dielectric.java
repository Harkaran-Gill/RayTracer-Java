package com.ray;

public class Dielectric implements Material{
    private final double refractiveIndex;

    public Dielectric(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }

    @Override
    public boolean scatter(Ray incomingRay, HitRecord rec, Color attenuation, Ray scattered){
        attenuation.set(new Color(1.0, 1.0, 1.0));
        double ri = rec.front_face ? (1.0/refractiveIndex) : refractiveIndex;

        Vec3 unitDirection = incomingRay.dir.unitVector();
        Vec3 negativeUnitDirection = unitDirection.negative();

        double cosTheta = Math.min(negativeUnitDirection
                .dot(rec.normal), 1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta*cosTheta);

        boolean cannotRefract = ri * sinTheta > 1.0;
        Vec3 direction;

        // TIR is from denser to rarer material
        // Schlick's approximation is for reflectance that happens from outside the glass
        if(cannotRefract || reflectance(cosTheta, ri) >  Utility.randomThreadLocalDouble()){
            direction = Vec3.reflect(unitDirection, rec.normal);
        }
        else {
            direction = Vec3.refract(unitDirection, rec.normal, negativeUnitDirection,cosTheta, ri);
        }

        scattered.set(rec.p, direction);
        return true;
    }

    private static double reflectance(double cosine, double refractiveIndex){
        // use Schlick's approximation for reflectance
        double r0 = (1 - refractiveIndex) / (1 + refractiveIndex);
        r0 = r0 * r0;
        return r0 + (1-r0) * Math.pow((1-cosine), 5);
    }
}
