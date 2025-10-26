package com.ray;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Utility {
    // This class has constants and miscellaneous utility methods
    // ThreadLocalRandom is used because it is much faster than Math.random() because
    // Math.random() is synchronized and runs on a single thread

    static final double pi = 3.1415926535897932385;
    static final double infinity = Double.POSITIVE_INFINITY;

    // return number n where n >= 0 && n < 1
    public static double randomDouble(){
        return randomThreadLocalDouble();
    }

    private static double randomThreadLocalDouble(){
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double randomDouble(double min, double max){
        return min + randomThreadLocalDouble() * (max - min);
    }

    public static int randomInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max);
    }

}
