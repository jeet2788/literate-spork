package org.example.collections.virtualthread;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

/**
 * A class demonstrating basic usage of virtual threads in Java.
 * This example creates multiple virtual threads that perform a simple task
 * and measures the execution time.
 */
public class BasicVirtualThreads {
    /**
     * The main method that demonstrates virtual threads.
     * It creates 10 virtual threads, each named "vt-i" where i is the index,
     * and each thread sleeps for 1 second before printing a completion message.
     * The total execution time is measured and printed.
     * Note: Joins or a latch should be added to wait for all threads to complete
     * before measuring the time accurately.
     *
     * @param args command-line arguments (not used)
     * @throws InterruptedException if the thread is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        Instant start = Instant.now();
        IntStream.range(0, 10).forEach(i -> {
            Thread.ofVirtual().name("vt-" + i).start(() -> {
                try {
                    Thread.sleep(Duration.ofSeconds(1));
                    System.out.println("Done: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });
        // Add joins or latch here
        Instant end = Instant.now();
        System.out.println("Time: " + Duration.between(start, end).toMillis() + " ms");
    }
}
