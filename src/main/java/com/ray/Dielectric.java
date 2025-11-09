package com.ray;

public class Dielectric implements Material{
    private final double refractiveIndex;

    public Dielectric(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }

    @Override
    public boolean scatter(Ray incomingRay, HitRecord rec, Vec3 attenuation, Ray scattered){
        attenuation.set(new Vec3(1.0, 1.0, 1.0));
        double ri = rec.front_face ? (1.0/refractiveIndex) : refractiveIndex;

        Vec3 unitDirection = incomingRay.getDirection().unitVector();

        double cosTheta = Math.min(unitDirection.negative().dot(rec.normal), 1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta*cosTheta);

        boolean cannotRefract = ri * sinTheta > 1.0;
        Vec3 direction;

        // Total Internal Reflection is from denser to rarer material
        // Schlick's approximation is for reflectance that happens from outside the glass
        if(cannotRefract || reflectance(cosTheta, ri) >  Utility.randomDouble()){
            direction = Vec3.reflect(unitDirection, rec.normal);
        }
        else {
            direction = Vec3.refract(unitDirection, rec.normal, cosTheta, ri);
        }

        scattered.set(rec.p, direction);
        return true;
    }

    private static double reflectance(double cosine, double refractiveIndex){
        // use Schlick's approximation for reflectance
        // This simulates how glass both reflects and refracts light which
        // varies by angle
        double r0 = (1 - refractiveIndex) / (1 + refractiveIndex);
        r0 = r0 * r0;
        return r0 + (1-r0) * Math.pow((1-cosine), 5);
    }
}
