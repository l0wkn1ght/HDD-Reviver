package com.boringpenguin.hddreviver.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow
    private int ticks;

    /**
     * finna inject into the tick method.
     * Vanilla checks if it's time to save very often.
     * i'll replace that check with my own longer interval.
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void hddreviver_onTick(CallbackInfo ci) {
        // meh, i'll implement it later in the future
    }
}
