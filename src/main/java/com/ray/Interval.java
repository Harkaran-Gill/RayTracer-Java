package com.ray;

public class Interval {
    // this is the class used for determining Intervals

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

    Interval (Interval a, Interval b) {
        this.min = Math.min(a.min, b.min);
        this.max = Math.max(a.max, b.max);
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

    public Interval expands(double delta) {
        double padding = delta/2;
        return new Interval(min - padding, max + padding);
    }

    public Interval expandsSelf(double delta) {
        double padding = delta/2;
            this.min -= padding;
        this.max += padding;
        return this;
    }

    double clamp(double x) {
        if (x < min) return min;
        return Math.min(x, max);
    }
}
