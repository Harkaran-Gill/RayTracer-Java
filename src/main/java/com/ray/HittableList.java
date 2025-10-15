package com.ray;

import java.util.ArrayList;

public class HittableList implements Hittable {
    // This class holds all the objects in the Scene.
    // using ArrayList for convenience, and then transferring objects to classic
    // array for speed
    private final ArrayList<Hittable> arr = new ArrayList<>();
    private int length = 0;
    private Hittable[] hittables;
    private HitRecord tempRecord;

    public HittableList() { tempRecord = new HitRecord(); }

    public HittableList(Hittable object) { arr.add(object); }

    void clear() { arr.clear(); }

    void add(Hittable object) {
        arr.add(object);
        length++;
    }

    void initializeArray() {
        hittables = new Hittable[length];
        for (int i = 0; i < length; i++){
            hittables[i] = arr.get(i);
        }
         clear();
    }

    @Override
    public boolean hit(Ray r, Interval rayInterval, HitRecord rec) {
        // tempRecord.reset();
        // Might need this later in the project, for now it works without a reset

        boolean hitAnything = false;
        double closest = rayInterval.max;

        // Looping through all the objects in the scene
        for (Hittable hittable : hittables) {
            if (hittable.hit(r, new Interval(rayInterval.min, closest), tempRecord)){
                hitAnything = true;
                closest = tempRecord.t;
                rec.set(tempRecord);
            }
        }
        return hitAnything;
    }
}
