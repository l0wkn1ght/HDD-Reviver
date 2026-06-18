package com.boringpenguin.hddreviver;

import com.boringpenguin.hddreviver.config.ReviverConfig;
import com.boringpenguin.hddreviver.io.BatchedRegionWriter;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HDDReviver implements ModInitializer {

    public static final String MOD_ID = "hdd-reviver";
    public static final Logger LOGGER = LoggerFactory.getLogger("HDD Reviver");

    @Override
    public void onInitialize() {
        LOGGER.info(
            "Initializing HDD Reviver on {}...",
            System.getProperty("java.vm.name")
        );
        ReviverConfig.load();
        BatchedRegionWriter.init();
    }
}
