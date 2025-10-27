package com.ray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BVH implements Hittable {
    private Hittable left;
    private Hittable right;
    private AABB bBox;
    private Interval interval;

    BVH(){}

    BVH(List<Hittable> objects, int start, int end){
        bBox = AABB.empty;
        for (int objectIndex= 0; objectIndex < objects.size(); objectIndex++){
            bBox = new AABB(bBox, objects.get(objectIndex).boundingBox());
        }

        int axis = bBox.longestAxis();

        Comparator<Hittable> comparator = (axis == 0) ? boxCompare(0) :
                (axis == 1) ? boxCompare(1) :
                        boxCompare(2);

        int objectSpan = end - start;

        if (objectSpan == 1){
            left = right = objects.get(start);
        }
        else if (objectSpan == 2){
            left = objects.get(start);
            right = objects.get(start+1);
        }
        else{
            objects.subList(start, end).sort(comparator);
            int mid = start + objectSpan/2;
            left = new BVH(objects, start, mid);
            right = new BVH(objects, mid, end);
        }
        bBox = new AABB(left.boundingBox(), right.boundingBox());
        interval = new Interval();
    }

    BVH(HittableList list){
        this(new ArrayList<>(list.arr), 0, list.arr.size());
    }

    public boolean hit(Ray r, Interval rayInterval, HitRecord rec){
        if (!bBox.hit(r, rayInterval))
            return false;

        boolean hitLeft = left.hit(r, rayInterval, rec);
        interval.min = rayInterval.min;
        interval.max = hitLeft ? rec.t : rayInterval.max;
        boolean hitRight = right.hit(r, interval, rec);

        return hitLeft || hitRight;
    }

    public AABB boundingBox(){return bBox;}

    private Comparator<Hittable> boxCompare(int axis){
        return (a, b) -> {
            var aAxisInterval = a.boundingBox().axisInterval(axis);
            var bAxisInterval = b.boundingBox().axisInterval(axis);

            return Double.compare(aAxisInterval.min, bAxisInterval.min);
        };
    }


}
