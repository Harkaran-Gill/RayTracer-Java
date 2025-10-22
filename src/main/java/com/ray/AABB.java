package com.ray;

import java.awt.*;

// Axis Aligned Bounding Boxes
public class AABB {
    Interval x, y, z;

    // Default Constructor, Intervals are empty by default
    AABB() {}

    AABB(Interval x, Interval y, Interval z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    AABB(Point3 a, Point3 b) {
        // Treat the two points a and b as extrema for the bounding box, so we don't require a
        // particular minimum/maximum coordinate order.

        x = a.x() <= b.x() ? new Interval(a.x(), b.x()) : new Interval(b.x(), a.x());
        y = a.y() <= b.y() ? new Interval(a.y(), b.y()) : new Interval(b.y(), a.y());
        z = a.z() <= b.z() ? new Interval(a.z(), b.z()) : new Interval(b.z(), a.z());
    }

    AABB (AABB box1, AABB box2) {
        this.x = new Interval(box1.x,  box2.x);
        this.y = new Interval(box1.y,  box2.y);
        this.z = new Interval(box1.z,  box2.z);
    }

    public Interval axisInterval(int n) {
        if (n == 1) return y;
        if (n == 2) return z;
        return x;
    }

    public boolean hit(Ray r, Interval rayInterval) {
        Point3 rayOrigin = r.getOrigin();
        Vec3 rayDirection = r.getDirection();

        for (int axis = 0; axis < 3; axis++) {
            Interval ax = axisInterval(axis);
            double rayAxisComponent = 1.0 / rayDirection.getAxis(axis);

            double t0 = (ax.min - rayOrigin.getAxis(axis)) * rayAxisComponent;
            double t1 = (ax.max - rayOrigin.getAxis(axis)) * rayAxisComponent;

            if (t0 < t1) {
                if (t0 < rayInterval.min) {
                    rayInterval.min = t0;
                }
                if (t1 > rayInterval.max) {
                    rayInterval.max = t1;
                }
            } else {
                if (t0 < rayInterval.min) {
                    rayInterval.min = t0;
                }
                if (t1 > rayInterval.max) {
                    rayInterval.max = t1;
                }
            }

            if (rayInterval.max <= rayInterval.min)
                return false;
        }
        return true;
    }
}

