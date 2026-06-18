package com.boringpenguin.hddreviver.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ReviverConfig {

    public static boolean batchChunkSaves = true;
    public static int autoSaveIntervalTicks = 6000; // 5 minutes
    public static boolean batchedIO = true;
    public static int batchFlushMs = 2000;

    public static void load() {
        Properties p = new Properties();
        Path path = Paths.get("config", "hdd-reviver.properties");
        try {
            if (Files.exists(path)) {
                p.load(Files.newBufferedReader(path));
            }
            batchChunkSaves = Boolean.parseBoolean(
                p.getProperty("batchChunkSaves", "true")
            );
            autoSaveIntervalTicks = Integer.parseInt(
                p.getProperty("autoSaveIntervalTicks", "6000")
            );
            batchedIO = Boolean.parseBoolean(
                p.getProperty("batchedIO", "true")
            );
            batchFlushMs = Integer.parseInt(
                p.getProperty("batchFlushMs", "2000")
            );
            save(path, p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void save(Path path, Properties p) throws Exception {
        p.setProperty("batchChunkSaves", String.valueOf(batchChunkSaves));
        p.setProperty(
            "autoSaveIntervalTicks",
            String.valueOf(autoSaveIntervalTicks)
        );
        p.setProperty("batchedIO", String.valueOf(batchedIO));
        p.setProperty("batchFlushMs", String.valueOf(batchFlushMs));
        Files.createDirectories(path.getParent());
        p.store(Files.newBufferedWriter(path), "HDD Reviver Config");
    }
}
