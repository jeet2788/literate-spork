package org.example.collections.virtualthread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Demonstrates structured concurrency with virtual threads using scoped values and exception handling.
 * This pattern allows for organized, predictable lifecycle management of virtual threads.
 *
 * Structured concurrency ensures that:
 * - Child tasks complete before parent completes
 * - Exceptions are properly propagated
 * - Resource cleanup is automatic
 */
public class StructuredConcurrencyExample {

    /**
     * Represents a worker that processes items with error handling.
     */
    static class WorkerTask implements Callable<String> {
        private final int workerId;
        private final int itemsToProcess;
        private final int failureRate; // percentage chance of failure

        WorkerTask(int workerId, int itemsToProcess, int failureRate) {
            this.workerId = workerId;
            this.itemsToProcess = itemsToProcess;
            this.failureRate = failureRate;
        }

        @Override
        public String call() throws Exception {
            int processedItems = 0;
            for (int i = 0; i < itemsToProcess; i++) {
                // Simulate processing
                Thread.sleep((long) (Math.random() * 100));

                // Simulate occasional failures
                if (Math.random() * 100 < failureRate) {
                    throw new RuntimeException("Worker-" + workerId + " failed at item " + i);
                }
                processedItems++;
            }
            return String.format("Worker-%d: Processed %d items successfully", workerId, processedItems);
        }
    }

    /**
     * Demonstrates using virtual threads with a CountDownLatch for coordination.
     * Useful for synchronizing multiple concurrent operations.
     */
    public static void example1_CountDownLatch() throws InterruptedException {
        System.out.println("\n=== Example 1: CountDownLatch for Coordination ===\n");

        int numberOfWorkers = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numberOfWorkers);
        AtomicInteger completedWork = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        // Create and start worker threads
        for (int i = 0; i < numberOfWorkers; i++) {
            final int workerId = i;
            Thread.ofVirtual().name("worker-" + i).start(() -> {
                try {
                    System.out.println("[Worker-" + workerId + "] Ready, waiting for start signal...");
                    startLatch.await(); // Wait for start signal

                    // Simulate work
                    for (int j = 0; j < 10; j++) {
                        Thread.sleep((long) (Math.random() * 50));
                        completedWork.incrementAndGet();
                    }

                    System.out.println("[Worker-" + workerId + "] Completed work");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // Start all workers simultaneously
        System.out.println("Starting all workers...");
        startLatch.countDown();

        // Wait for all to complete
        endLatch.await();

        long duration = System.currentTimeMillis() - startTime;
        System.out.printf("All workers completed. Total work items: %d, Time: %d ms%n",
            completedWork.get(), duration);
    }

    /**
     * Demonstrates using virtual threads with a Phaser for multiple phases of execution.
     * Useful for coordinating work across multiple stages or rounds.
     */
    public static void example2_Phaser() throws InterruptedException {
        System.out.println("\n=== Example 2: Phaser for Multi-Phase Work ===\n");

        int numberOfWorkers = 4;
        int phases = 3;
        Phaser phaser = new Phaser(numberOfWorkers + 1); // +1 for main thread

        long startTime = System.currentTimeMillis();

        // Create workers
        for (int i = 0; i < numberOfWorkers; i++) {
            final int workerId = i;
            Thread.ofVirtual().name("phaser-worker-" + i).start(() -> {
                try {
                    for (int phase = 0; phase < phases; phase++) {
                        System.out.printf("[Phase-%d] Worker-%d executing phase %d%n",
                            phase + 1, workerId, phase + 1);

                        Thread.sleep((long) (Math.random() * 200) + 100);

                        int currentPhase = phaser.arrive(); // Signal completion of current phase
                        System.out.printf("[Phase-%d] Worker-%d waiting for sync...%n",
                            phase + 1, workerId);
                        phaser.awaitAdvance(currentPhase); // Wait for all to reach this phase
                    }
                    System.out.printf("Worker-%d completed all phases%n", workerId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        phaser.arrive(); // Main thread registers

        // Advance through phases
        for (int phase = 0; phase < phases; phase++) {
            phaser.awaitAdvance(phaser.getPhase());
            System.out.printf(">>> All threads completed phase %d%n%n", phase + 1);
        }

        phaser.arriveAndDeregister();
        long duration = System.currentTimeMillis() - startTime;
        System.out.printf("Multi-phase execution completed in %d ms%n", duration);
    }

    /**
     * Demonstrates using virtual threads with ExecutorService for batch processing with error handling.
     */
    public static void example3_ExecutorWithErrorHandling() throws InterruptedException {
        System.out.println("\n=== Example 3: Executor Service with Error Handling ===\n");

        int numberOfWorkers = 5;
        int itemsPerWorker = 10;

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            // Submit all worker tasks
            java.util.List<Future<String>> futures = new java.util.ArrayList<>();
            for (int i = 0; i < numberOfWorkers; i++) {
                Future<String> future = executor.submit(new WorkerTask(i, itemsPerWorker, 10));
                futures.add(future);
            }

            // Collect results and handle exceptions
            int successCount = 0;
            int failureCount = 0;

            for (int i = 0; i < futures.size(); i++) {
                try {
                    String result = futures.get(i).get(5, TimeUnit.SECONDS);
                    System.out.println("✓ " + result);
                    successCount++;
                } catch (TimeoutException e) {
                    System.err.printf("✗ Worker-%d timed out%n", i);
                    failureCount++;
                } catch (ExecutionException e) {
                    System.err.printf("✗ Worker-%d error: %s%n", i, e.getCause().getMessage());
                    failureCount++;
                }
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            System.out.printf("\nResults: %d successful, %d failed%n", successCount, failureCount);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  Structured Concurrency with Virtual Threads Examples      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        example1_CountDownLatch();
        example2_Phaser();
        example3_ExecutorWithErrorHandling();

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  All examples completed successfully!                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}

