package com.ray;

import java.util.ArrayList;

public class HittableList implements Hittable {
    ArrayList<Hittable> hittables = new ArrayList<Hittable>();
    public HittableList() {}

    public HittableList(Hittable object) { hittables.add(object); }

    void clear() { hittables.clear(); }

    void add(Hittable object) { hittables.add(object); }

    @Override
    public boolean hit(Ray r, double tMin, double tMax, HitRecord rec) {
        HitRecord tempRec = new HitRecord();
        boolean hitAnything = false;
        double closest = tMax;

        for (Hittable hittable : hittables) {
            if (hittable.hit(r, tMin, closest, tempRec)){
                hitAnything = true;
                closest = tempRec.t;
                rec.set(tempRec);
            }
        }
        return hitAnything;
    }
}
