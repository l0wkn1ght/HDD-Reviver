package com.boringpenguin.hddreviver.io;

import com.boringpenguin.hddreviver.HDDReviver;
import com.boringpenguin.hddreviver.config.ReviverConfig;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BatchedRegionWriter {

    private static final ConcurrentHashMap<Object, Queue<Runnable>> QUEUES =
        new ConcurrentHashMap<>();
    private static ScheduledExecutorService FLUSHER;

    public static void init() {
        if (!ReviverConfig.batchedIO) {
            return;
        }
        FLUSHER = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "HDD-Reviver-IO");
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });
        FLUSHER.scheduleAtFixedRate(
            BatchedRegionWriter::flushAll,
            ReviverConfig.batchFlushMs,
            ReviverConfig.batchFlushMs,
            TimeUnit.MILLISECONDS
        );
        HDDReviver.LOGGER.info(
            "Batched Region Writer initialized. Flushing every {}ms",
            ReviverConfig.batchFlushMs
        );
    }

    public static void enqueue(Runnable writeOp) {
        if (!ReviverConfig.batchedIO) {
            writeOp.run();
            return;
        }
        // Group by thread context or just use a global queue.
        // For simplicity and max batching, i will use a single global queue key.
        QUEUES.computeIfAbsent(new Object(), k ->
            new ConcurrentLinkedQueue<>()
        ).add(writeOp);
    }

    private static void flushAll() {
        for (var entry : QUEUES.entrySet()) {
            Queue<Runnable> queue = entry.getValue();
            Runnable task;
            while ((task = queue.poll()) != null) {
                try {
                    task.run();
                } catch (Throwable t) {
                    HDDReviver.LOGGER.error("Error during batched flush", t);
                }
            }
        }
    }
}
