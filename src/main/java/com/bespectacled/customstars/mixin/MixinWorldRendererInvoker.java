package com.bespectacled.customstars.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public interface MixinWorldRendererInvoker {
    @Invoker("renderStars")
    public BufferBuilder.BuiltBuffer rerenderStars(BufferBuilder builder);
}
