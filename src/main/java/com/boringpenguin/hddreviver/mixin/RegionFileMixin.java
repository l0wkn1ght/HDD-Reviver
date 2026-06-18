package com.boringpenguin.hddreviver.mixin;

import com.boringpenguin.hddreviver.io.BatchedRegionWriter;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.RegionFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.ByteBuffer;

@Mixin(RegionFile.class)
public abstract class RegionFileMixin {

    @Inject(method = "write", at = @At("HEAD"), cancellable = true)
    private void hddReviver$interceptWrite(ChunkPos pos, ByteBuffer buf, CallbackInfo ci) {
        // Cancel vanilla synchronous write
        ci.cancel();

        // Copy the buffer because Netty will reclaim it immediately
        byte[] safeData = new byte[buf.remaining()];
        buf.get(safeData);
        buf.rewind();

        RegionFile self = (RegionFile) (Object) this;
        // Pass a runnable that invokes the shadowed original method
        BatchedRegionWriter.enqueue(() -> {
            try {
                ((RegionFileInvoker) self).hddReviver$invokeOriginalWrite(pos, ByteBuffer.wrap(safeData));
            } catch (IOException e) {
                com.boringpenguin.hddreviver.HDDReviver.LOGGER.error("Failed to flush chunk {} to disk", pos, e);
            }
        });
    }
}
