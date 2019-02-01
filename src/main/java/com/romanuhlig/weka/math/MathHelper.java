package com.romanuhlig.weka.math;

import java.util.Vector;

public class MathHelper {


    public static double calculateVelocityFromPosition(double positionBegin, double positionFinal, double timeBegin, double timeFinal) {
        return (positionFinal - positionBegin) / (timeFinal - timeBegin);
    }

    public static double calculateAccelerationFromVelocity(double velocityBegin, double velocityFinal, double timeBegin, double timeFinal) {
        return (velocityFinal - velocityBegin) / (timeFinal - timeBegin);
    }

    public static double EuclideanNorm(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    public static double EuclideanNorm(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public static double getMinimumWithNAN(double double1, double double2) {

        if (Double.isNaN(double1)) {
            return double1;
        }

        if (Double.isNaN(double2)) {
            return double2;
        }

        return Double.min(double1, double2);
    }

    public static double getZeroIfNAN(double value) {
        if (Double.isNaN(value)) {
            return 0;
        } else {
            return value;
        }
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(
                Math.pow(x2 - x1, 2)
                        + Math.pow(y2 - y1, 2)
                        + Math.pow(z2 - z1, 2));
    }


}
