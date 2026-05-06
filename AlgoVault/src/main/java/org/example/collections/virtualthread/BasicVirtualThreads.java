package org.example.collections.virtualthread;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * A class demonstrating basic usage of virtual threads in Java.
 * This example creates multiple virtual threads that perform a simple task
 * and measures the execution time.
 *
 * Key Improvements:
 * - Uses CountDownLatch for proper thread synchronization
 * - Provides clear, step-by-step console output
 * - Explains what's happening at each stage
 * - Demonstrates the efficiency of virtual threads
 */
public class BasicVirtualThreads {
    /**
     * The main method that demonstrates virtual threads with proper synchronization.
     * It creates 10 virtual threads, each named "vt-i" where i is the index,
     * and each thread sleeps for 1 second before printing a completion message.
     * Uses CountDownLatch to ensure all threads complete before measuring total time.
     *
     * @param args command-line arguments (not used)
     * @throws InterruptedException if the thread is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║  Basic Virtual Threads Example with Synchronization    ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        final int numberOfThreads = 10;
        final CountDownLatch latch = new CountDownLatch(numberOfThreads);
        List<Thread> threads = new ArrayList<>();

        System.out.println("📌 STEP 1: Creating " + numberOfThreads + " virtual threads...\n");

        Instant start = Instant.now();

        // Create and store virtual threads
        IntStream.range(0, numberOfThreads).forEach(i -> {
            Thread vThread = Thread.ofVirtual()
                .name("vt-" + i)
                .start(() -> {
                    try {
                        System.out.printf("  ➤ [%s] Started - sleeping for 1 second%n",
                            Thread.currentThread().getName());
                        Thread.sleep(Duration.ofSeconds(1));
                        System.out.printf("  ✓ [%s] Completed%n",
                            Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.printf("  ✗ [%s] Interrupted%n",
                            Thread.currentThread().getName());
                    } finally {
                        latch.countDown(); // Signal completion
                    }
                });
            threads.add(vThread);
        });

        System.out.println("\n📌 STEP 2: All threads started - waiting for completion...\n");

        // Wait for all threads to complete
        latch.await();

        Instant end = Instant.now();
        Duration totalTime = Duration.between(start, end);

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                    EXECUTION SUMMARY                   ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Threads: %d                                        ║%n", numberOfThreads);
        System.out.printf("║ Total Execution Time: %d ms                              ║%n", totalTime.toMillis());
        System.out.printf("║ Average Time per Thread: %.2f ms                         ║%n",
            (double) totalTime.toMillis() / numberOfThreads);
        System.out.println("║                                                        ║");
        System.out.println("║ 📊 Key Insight:                                         ║");
        System.out.println("║ All 10 threads ran CONCURRENTLY (in parallel),         ║");
        System.out.println("║ completing in ~1 second instead of 10 seconds!         ║");
        System.out.println("║ This demonstrates the power of virtual threads.        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
    }
}
