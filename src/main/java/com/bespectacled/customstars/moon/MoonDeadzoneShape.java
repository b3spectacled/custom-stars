package com.bespectacled.customstars.moon;

import org.apache.commons.lang3.function.TriFunction;

public enum MoonDeadzoneShape {
    SQUARE(MoonDeadzoneShape::inSquare),
    CIRCLE(MoonDeadzoneShape::inCircle);
    
    private TriFunction<Double, Double, Double, Boolean> testFunc;
    
    private MoonDeadzoneShape(TriFunction<Double, Double, Double, Boolean> testFunc) {
        this.testFunc = testFunc;
    }
    
    public boolean test(double x, double z, double size) {
        return this.testFunc.apply(x, z, size);
    }
    
    private static boolean inSquare(double x, double z, double size) {
        return x > -size && x < size && z > -size && z < size;
    }
    
    private static boolean inCircle(double x, double z, double size) {
        return x * x + z * z < size * size;
    }
}
