package com.boringpenguin.hddreviver.mixin;

import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.RegionFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RegionFile.class)
public interface RegionFileInvoker {
    @Invoker("write")
    void hddReviver$invokeOriginalWrite(ChunkPos pos, ByteBuffer buf)
        throws IOException;
}
