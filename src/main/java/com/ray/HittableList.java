package com.ray;

import java.util.ArrayList;
import java.util.List;

public class HittableList implements Hittable {
    // This class holds all the objects in the Scene uses ArrayList for convenience
    final List<Hittable> arr = new ArrayList<>();
    private final HitRecord tempRecord;
    private AABB bBox = new AABB();

    public HittableList() { tempRecord = new HitRecord(); }

    public HittableList(Hittable object) {
        tempRecord = new HitRecord();
        arr.add(object);
    }

    void clear() { arr.clear(); }

    void add(Hittable object) {
        arr.add(object);
        bBox = new AABB(bBox, object.boundingBox());
    }

    @Override
    public boolean hit(Ray r, Interval rayInterval, HitRecord rec) {
        // tempRecord.reset();
        // Might need this later in the project, for now it works without a reset

        boolean hitAnything = false;
        double closest = rayInterval.max;

        // Looping through all the objects in the scene
        Interval interval = new Interval(); // Interval in which a ray can hit the object
        interval.min = rayInterval.min;     // min does not change in the loop, so keep it out
        for (Hittable hittable : arr) {
            interval.max = closest;
            if (hittable.hit(r, interval, tempRecord)){
                hitAnything = true;
                closest = tempRecord.t;
                rec.set(tempRecord);
            }
        }
        return hitAnything;
    }

    public AABB boundingBox() {return bBox;}
}
