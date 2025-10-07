package com.ray;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Utility {
    static final double pi = 3.1415926535897932385;
    static final double infinity = Double.POSITIVE_INFINITY;

    public static double degreesToRadians(double degrees) {
        return degrees * pi / 180.0;
    }

    // return number n where n >= 0 && n < 1
    public static double randomDouble(){
        return randomThreadLocalDouble();
    }

    public static double randomThreadLocalDouble(){
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double randomDouble(double min, double max){
        return min + randomThreadLocalDouble() * (max - min);
    }

}
