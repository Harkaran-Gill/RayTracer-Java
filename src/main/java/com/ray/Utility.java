package com.ray;

public abstract class Utility {
    static final double pi = 3.1415926535897932385;
    static final double infinity = Double.POSITIVE_INFINITY;

    public static double degreesToRadians(double degrees) {
        return degrees * pi / 180.0;
    }
}
