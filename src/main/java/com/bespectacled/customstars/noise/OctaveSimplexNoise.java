package com.bespectacled.customstars.noise;

import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.Random;

public class OctaveSimplexNoise {
    private final SimplexNoiseSampler[] generators;
    private final int octaves;
    
    public OctaveSimplexNoise(Random random, int octaves) {
        this.octaves = octaves;
        this.generators = new SimplexNoiseSampler[octaves];
        
        for (int i = 0; i < octaves; ++i) {
            this.generators[i] = new SimplexNoiseSampler(random);
        }
    }
    
    public double sample(double x, double y, double z) {
        double total = 0.0D;
        double frequency = 1.0D;
        double amplitude = 1.0D;
        
        double persistence = 0.5D;
        double lacunarity = 2.0D;
        
        for (int i = 0; i < this.octaves; ++i) {
            total += this.generators[i].sample(x * frequency, y * frequency, z * frequency) * amplitude;
            
            amplitude *= persistence;
            frequency *= lacunarity;
        }
        
        return total;
    }
}
