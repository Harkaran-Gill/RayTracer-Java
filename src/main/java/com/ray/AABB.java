package com.ray;

import java.awt.*;

// Axis Aligned Bounding Boxes
public class AABB {
    Interval x, y, z;
    static final AABB empty    = new AABB(Interval.empty, Interval.empty, Interval.empty);
    static final AABB universe = new AABB(Interval.universe, Interval.universe, Interval.universe);

    // Default Constructor, Intervals are empty by default
    AABB() {
        x = new Interval();
        y = new Interval();
        z = new Interval();
    }

    // Custom constructor, initialize Interval based on input Intervals
    AABB(Interval x, Interval y, Interval z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Custom constructor, initialize values based on the extremes of the points
    AABB(Point3 a, Point3 b) {
        // Treat the two points a and b as extrema for the bounding box, so we don't require a
        // particular minimum/maximum coordinate order.

        x = a.x() <= b.x() ? new Interval(a.x(), b.x()) : new Interval(b.x(), a.x());
        y = a.y() <= b.y() ? new Interval(a.y(), b.y()) : new Interval(b.y(), a.y());
        z = a.z() <= b.z() ? new Interval(a.z(), b.z()) : new Interval(b.z(), a.z());
    }

    // Custom constructor, creates a new bounding box around the input boxes
    AABB (AABB box1, AABB box2) {
        this.x = new Interval(box1.x,  box2.x);
        this.y = new Interval(box1.y,  box2.y);
        this.z = new Interval(box1.z,  box2.z);
    }

    // Returns Interval based on n
    public Interval axisInterval(int n) {
        if (n == 1) return y;
        if (n == 2) return z;
        return x;
    }

    // Hit method that determines if the ray hit the box around the Sphere, if the
    // ray does not hit the box, it will not hit any sphere or smaller bounding box inside it
    // It is much less computationally intensive to calculate a box-hit that calculating a sphere hit
    // therefore this method reduces a lot of call that would have been made to Sphere.hit()
    public boolean hit(Ray r, Interval rayInterval) {
        Point3 rayOrigin = r.getOrigin();
        Vec3 rayDirection = r.getDirection();
        double tMin = rayInterval.min;
        double tMax = rayInterval.max;

        // We calculate if the ray-axis component (x,y,z part of the Ray) intersects with the
        // x,y,z Interval in which the box lies
        for (int axis = 0; axis < 3; axis++) {
            Interval ax = axisInterval(axis);
            double rayAxisComponent = 1.0 / rayDirection.getAxis(axis);

            // Formula: t = (x - O) / Dx, where x = a point on a line parallel to axis, O = ray origin,
            // Dx = direction vector of the ray parallel to axis
            double t0 = (ax.min - rayOrigin.getAxis(axis)) * rayAxisComponent;
            double t1 = (ax.max - rayOrigin.getAxis(axis)) * rayAxisComponent;

            // Determine if t0 < t1 or t1 > t0
            if (t0 < t1) {
                if (t0 > tMin) tMin = t0;

                if (t1 < tMax) tMax = t1;

            } else {
                if (t0 > tMin) tMin = t1;

                if (t1 < tMax) tMax = t0;
            }

            if (tMax <= tMin)
                return false;
        }
        return true;
    }

    public int longestAxis(){
        if(x.size() > y.size())
            return x.size() > z.size() ? 0 : 2;
        else
            return y.size() > z.size() ? 1 : 2;
    }
}

