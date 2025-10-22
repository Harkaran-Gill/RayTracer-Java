package com.ray;

import java.util.ArrayList;

public class BVH implements Hittable {
    private Hittable left;
    private Hittable right;
    private AABB bBox;

    BVH(ArrayList<Hittable> objects, int start, int end){}

    BVH(HittableList list){
        this(list.arr, 0, list.arr.size());
    }

    public boolean hit(Ray r, Interval rayInterval, HitRecord rec){
        if (!bBox.hit(r, rayInterval))
            return false;

        boolean hitLeft = left.hit(r, rayInterval, rec);
        boolean hitRight = right.hit(r,new Interval(rayInterval.min, hitLeft ? rec.t : rayInterval.max), rec);

        return hitLeft || hitRight;
    }

    public AABB boundingBox(){return bBox;}
}
