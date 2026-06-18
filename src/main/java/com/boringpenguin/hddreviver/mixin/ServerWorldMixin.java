package com.boringpenguin.hddreviver.mixin;

import com.boringpenguin.hddreviver.config.ReviverConfig;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    // need to store a custom timer since we can't easily access the server ticks here cleanly
    // without a lot of @Shadowing. instead, i'll use a simple thread-safe counter or
    // Just hook into the save method and return early.

    private int hddReviver$saveCounter = 0;

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    private void hddreviver_debounceSave(CallbackInfo ci) {
        if (!ReviverConfig.batchChunkSaves) {
            return;
        }

        this.hddReviver$saveCounter++;

        // Only allow the save to proceed if our custom interval is met
        if (this.hddReviver$saveCounter < ReviverConfig.autoSaveIntervalTicks) {
            ci.cancel(); // Cancel the save operation
        } else {
            // Reset counter
            this.hddReviver$saveCounter = 0;
            // Let the save Proceed (Vanilla logic runs)s
        }
    }
}
