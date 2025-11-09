package com.ray;

import java.util.ArrayList;
import java.util.List;

public class HittableList implements Hittable {
    // This class holds all the objects in the Scene, using ArrayList for convenience.

    final List<Hittable> arr = new ArrayList<>();
    private AABB bBox = new AABB();

    public HittableList() {  }

    public HittableList(Hittable object) {
        arr.add(object);
    }

    void clear() { arr.clear(); }

    void add(Hittable object) {
        arr.add(object);
        bBox = new AABB(bBox, object.boundingBox());
    }


    @Override
    public boolean hit(Ray r, Interval rayInterval, HitRecord rec) {
        HitRecord tempRecord = new HitRecord();
        boolean hitAnything = false;
        double closest;

        // Looping through all the objects in the scene
        for (Hittable hittable : arr) {
            if (hittable.hit(r, rayInterval, tempRecord)){
                hitAnything = true;
                closest = tempRecord.t;
                rec.set(tempRecord);
                rayInterval.max = closest;
            }
        }
        return hitAnything;
    }

    public AABB boundingBox() {return bBox;}
}
