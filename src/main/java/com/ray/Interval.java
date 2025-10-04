package com.ray;

public class Interval {
    double min;
    double max;
    static final Interval empty     = new Interval(Utility.infinity, -Utility.infinity);
    static final Interval universe  = new Interval(-Utility.infinity, Utility.infinity);

    Interval() {
        this.min = -Utility.infinity;
        this.max = Utility.infinity;
    }

    Interval(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double size() {
        return max - min;
    }

    public boolean contains(double value) {
        return value >= min && value <= max;
    }

    public boolean surrounds(double x) {
        return min < x && x < max;
    }

    double clamp(double x) {
        if (x < min) return min;
        return Math.min(x, max);
    }
}
