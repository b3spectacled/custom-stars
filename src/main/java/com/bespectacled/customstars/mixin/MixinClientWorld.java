package com.bespectacled.customstars.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.customstars.CustomStars;
import com.bespectacled.customstars.config.CustomStarsConfig;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.LunarWorldView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(ClientWorld.class)
public class MixinClientWorld {
    @Unique private static final CustomStarsConfig STARS_CONFIG = CustomStars.STARS_CONFIG;
    
    @Unique private float customStarsSkyAngle;
    
    @Inject(method = "getSkyColor", at = @At("HEAD"))
    private void injectGetSkyColor(Vec3d pos, float tickDelta, CallbackInfoReturnable<Vec3d> info) {
        this.customStarsSkyAngle = ((LunarWorldView)(Object)this).getSkyAngle(tickDelta);
        this.customStarsSkyAngle = MathHelper.cos(this.customStarsSkyAngle * ((float)Math.PI * 2)) * 2.0f + 0.5f;
        this.customStarsSkyAngle = MathHelper.clamp(this.customStarsSkyAngle, 0.0f, 1.0f);
    }
    
    @ModifyVariable(
        method = "getSkyColor",
        at = @At(value = "STORE"),
        ordinal = 3
    )
    private float injectSkyR(float r) {
        float nightSkyR = STARS_CONFIG.skyColor.normalR();
        nightSkyR *= 1.0f - this.customStarsSkyAngle;
        
        return r + nightSkyR;
    }
    
    @ModifyVariable(
        method = "getSkyColor",
        at = @At(value = "STORE"),
        ordinal = 4
    )
    private float injectSkyG(float g) {
        float nightSkyG = STARS_CONFIG.skyColor.normalG();
        nightSkyG *= 1.0f - this.customStarsSkyAngle;
        
        return g + nightSkyG;
    }
    
    @ModifyVariable(
        method = "getSkyColor",
        at = @At(value = "STORE"),
        ordinal = 5
    )
    private float injectSkyB(float b) {
        float nightSkyB = STARS_CONFIG.skyColor.normalB();
        nightSkyB *= 1.0f - this.customStarsSkyAngle;
        
        return b + nightSkyB;
    }
}
