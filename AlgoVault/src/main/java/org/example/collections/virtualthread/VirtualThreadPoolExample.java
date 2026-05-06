package org.example.collections.virtualthread;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates virtual thread executor services for managing large numbers of concurrent tasks.
 * Virtual thread pools are lightweight and can handle millions of threads efficiently.
 *
 * This example simulates processing multiple I/O-bound tasks concurrently,
 * showcasing the performance advantages of virtual threads over platform threads.
 */
public class VirtualThreadPoolExample {

    /**
     * Simulates an I/O-bound operation like making an HTTP request or database query.
     *
     * @param taskId unique identifier for the task
     * @return execution time of the simulated operation
     */
    private static long simulateIOOperation(int taskId) {
        try {
            // Simulate variable I/O operation time (0-2 seconds)
            long sleepTime = (long) (Math.random() * 2000);
            Thread.sleep(sleepTime);
            System.out.printf("[TASK-%03d] Completed I/O operation in %d ms on %s%n",
                taskId, sleepTime, Thread.currentThread().getName());
            return sleepTime;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("[TASK-%03d] Interrupted: %s%n", taskId, e.getMessage());
            return -1;
        }
    }

    /**
     * Demonstrates virtual thread executor with a large number of tasks.
     * Uses newVirtualThreadPerTaskExecutor() for optimal performance with I/O-bound work.
     */
    public static void main(String[] args) throws InterruptedException {
        int totalTasks = 1000;
        Instant startTime = Instant.now();

        System.out.println("=== Virtual Thread Pool Example ===");
        System.out.printf("Processing %d I/O-bound tasks using virtual threads%n%n", totalTasks);

        // Create a virtual thread executor - creates a new virtual thread per task
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<Long>> futures = new ArrayList<>();

            // Submit all tasks
            for (int i = 0; i < totalTasks; i++) {
                final int taskId = i;
                Future<Long> future = executor.submit(() -> simulateIOOperation(taskId));
                futures.add(future);
            }

            // Collect results
            long totalTime = 0;
            int completedTasks = 0;

            for (Future<Long> future : futures) {
                try {
                    long taskTime = future.get();
                    if (taskTime >= 0) {
                        totalTime += taskTime;
                        completedTasks++;
                    }
                } catch (Exception e) {
                    System.err.printf("Task execution failed: %s%n", e.getMessage());
                }
            }

            // Shutdown executor
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            Instant endTime = Instant.now();
            Duration totalDuration = Duration.between(startTime, endTime);

            // Display results
            System.out.println("\n=== Results ===");
            System.out.printf("Completed Tasks: %d/%d%n", completedTasks, totalTasks);
            System.out.printf("Total Execution Time: %d ms%n", totalDuration.toMillis());
            System.out.printf("Average Task Time: %.2f ms%n", (double) totalTime / completedTasks);
            System.out.printf("Efficiency Ratio: %.2f%% (parallel execution benefit)%n",
                ((double) totalTime / totalDuration.toMillis()) * 100);
        }
    }
}

