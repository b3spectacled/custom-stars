package com.bespectacled.customstars.moon;

public enum MoonDeadzoneShape {
    SQUARE, CIRCLE;
    
    public static boolean inSquare(double x, double z, double size) {
        return x > -size && x < size && z > -size && z < size;
    }
    
    public static boolean inCircle(double x, double z, double size) {
        return x * x + z * z < size * size;
    }
}
