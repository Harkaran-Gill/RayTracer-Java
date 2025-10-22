package com.ray;

import java.util.ArrayList;

public class HittableList implements Hittable {
    // This class holds all the objects in the Scene.
    // using ArrayList for convenience, and then transferring objects to classic
    // array for speed
    final ArrayList<Hittable> arr = new ArrayList<>();
    private Hittable[] hittables;
    private HitRecord tempRecord;
    private AABB bBox;

    public HittableList() { tempRecord = new HitRecord(); }

    public HittableList(Hittable object) { arr.add(object); }

    void clear() { arr.clear(); }

    void add(Hittable object) {
        arr.add(object);
        bBox = new AABB(bBox, object.boundingBox());
    }

    void initializeArray() {
        hittables = new Hittable[arr.size()];
        hittables = arr.toArray(hittables);
         clear();
    }

    @Override
    public boolean hit(Ray r, Interval rayInterval, HitRecord rec) {
        // tempRecord.reset();
        // Might need this later in the project, for now it works without a reset

        boolean hitAnything = false;
        double closest = rayInterval.max;

        // Looping through all the objects in the scene
        Interval interval = new Interval();
        for (Hittable hittable : hittables) {
            interval.min = rayInterval.min;
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
