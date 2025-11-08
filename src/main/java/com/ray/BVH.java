package com.ray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// This is a BVH Node class, we build a binary tree here to enable Binary search
public class BVH implements Hittable {
    private final Hittable left;
    private final Hittable right;
    private AABB bBox;

    // Custom constructor, build a binary
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
        //interval = new Interval();
    }

    BVH(HittableList list){
        this(new ArrayList<>(list.arr), 0, list.arr.size());
    }

    public boolean hit(Ray r, Interval rayInterval, HitRecord rec){
        if (!bBox.hit(r, rayInterval))
            return false;

        HitRecord leftRec = new HitRecord();
        HitRecord rightRec = new HitRecord();

        boolean hitLeft = left.hit(r, rayInterval, leftRec);
        boolean hitRight = right.hit(r, new Interval(rayInterval.min, hitLeft ? leftRec.t : rayInterval.max), rightRec);

        if (hitLeft && hitRight) {
            rec.copyFrom(leftRec.t < rightRec.t ? leftRec : rightRec);
        } else if (hitLeft) {
            rec.copyFrom(leftRec);
        } else if (hitRight) {
            rec.copyFrom(rightRec);
        } else {
            return false;
        }
        return true;
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
