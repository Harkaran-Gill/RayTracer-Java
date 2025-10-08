package com.ray;

import java.util.ArrayList;

public class HittableList implements Hittable {
    ArrayList<Hittable> hittables = new ArrayList<>();
    public HittableList() {}

    public HittableList(Hittable object) { hittables.add(object); }

    void clear() { hittables.clear(); }

    void add(Hittable object) { hittables.add(object); }

    @Override
    public boolean hit(Ray r, Interval rayInterval, HitRecord rec) {
        HitRecord tempRec = new HitRecord();
        boolean hitAnything = false;
        double closest = rayInterval.max;

        for (Hittable hittable : hittables) {
            if (hittable.hit(r, new Interval(rayInterval.min, closest), tempRec)){
                hitAnything = true;
                closest = tempRec.t;
                rec.set(tempRec);
            }
        }
        return hitAnything;
    }
}
