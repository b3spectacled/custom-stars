package com.bespectacled.customstars.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(value = ClientWorld.class, priority = 1) 
public abstract class MixinClientWorld extends World {
    
    private MixinClientWorld() {
        super(null, null, null, null, false, false, 0L);
    }

    
}
