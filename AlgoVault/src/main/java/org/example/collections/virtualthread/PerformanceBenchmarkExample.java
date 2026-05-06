package org.example.collections.virtualthread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Duration;
import java.time.Instant;

/**
 * Performance benchmark: Virtual Threads vs Platform Threads.
 *
 * This example demonstrates the performance differences between:
 * - Virtual threads (lightweight, many can run concurrently)
 * - Platform threads (heavyweight, limited number)
 *
 * Virtual threads are particularly beneficial for I/O-bound workloads where
 * threads spend significant time waiting for operations to complete.
 */
public class PerformanceBenchmarkExample {

    /**
     * Simulates an I/O-bound task (e.g., network request, database query).
     */
    static class IOBoundTask implements Runnable {
        private final int taskId;
        private final AtomicInteger completedTasks;
        private final long ioDuration;

        IOBoundTask(int taskId, AtomicInteger completedTasks, long ioDuration) {
            this.taskId = taskId;
            this.completedTasks = completedTasks;
            this.ioDuration = ioDuration;
        }

        @Override
        public void run() {
            try {
                // Simulate I/O wait time
                Thread.sleep(ioDuration);
                completedTasks.incrementAndGet();

                if (taskId % 100 == 0) {
                    System.out.printf("  Task %d completed on thread: %s%n",
                        taskId, Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Benchmarks virtual threads execution.
     */
    static class VirtualThreadBenchmark {
        static long run(int numberOfTasks, long ioDuration) throws InterruptedException {
            System.out.printf("\n🧵 VIRTUAL THREADS: %d tasks with %d ms I/O\n",
                numberOfTasks, ioDuration);

            AtomicInteger completedTasks = new AtomicInteger(0);
            Instant startTime = Instant.now();

            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < numberOfTasks; i++) {
                    executor.submit(new IOBoundTask(i, completedTasks, ioDuration));
                }

                executor.shutdown();
                executor.awaitTermination(5, TimeUnit.MINUTES);
            }

            Duration elapsed = Duration.between(startTime, Instant.now());
            System.out.printf("  ✓ Completed: %d tasks in %d ms%n",
                completedTasks.get(), elapsed.toMillis());
            System.out.printf("  ✓ Throughput: %.2f tasks/sec%n",
                (numberOfTasks * 1000.0) / elapsed.toMillis());

            return elapsed.toMillis();
        }
    }

    /**
     * Benchmarks platform threads execution.
     */
    static class PlatformThreadBenchmark {
        static long run(int numberOfTasks, long ioDuration) throws InterruptedException {
            System.out.printf("\n🔧 PLATFORM THREADS (Fixed Pool): %d tasks with %d ms I/O\n",
                numberOfTasks, ioDuration);

            AtomicInteger completedTasks = new AtomicInteger(0);
            int poolSize = Math.min(numberOfTasks, 50); // Limited pool size

            Instant startTime = Instant.now();

            try (ExecutorService executor = Executors.newFixedThreadPool(poolSize)) {
                for (int i = 0; i < numberOfTasks; i++) {
                    executor.submit(new IOBoundTask(i, completedTasks, ioDuration));
                }

                executor.shutdown();
                executor.awaitTermination(5, TimeUnit.MINUTES);
            }

            Duration elapsed = Duration.between(startTime, Instant.now());
            System.out.printf("  ✓ Completed: %d tasks in %d ms (Pool size: %d)%n",
                completedTasks.get(), elapsed.toMillis(), poolSize);
            System.out.printf("  ✓ Throughput: %.2f tasks/sec%n",
                (numberOfTasks * 1000.0) / elapsed.toMillis());

            return elapsed.toMillis();
        }
    }

    /**
     * Runs comprehensive benchmarks.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  Virtual Threads vs Platform Threads - Performance Test    ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        // Test Case 1: Light I/O
        System.out.println("\n\n━━━ TEST CASE 1: Light I/O (10ms) ━━━");
        long virtualTime1 = VirtualThreadBenchmark.run(500, 10);
        Thread.sleep(500);
        long platformTime1 = PlatformThreadBenchmark.run(500, 10);

        System.out.printf("\n📊 Result: Virtual threads %.2fx faster%n",
            (double) platformTime1 / virtualTime1);

        // Test Case 2: Medium I/O
        System.out.println("\n\n━━━ TEST CASE 2: Medium I/O (100ms) ━━━");
        long virtualTime2 = VirtualThreadBenchmark.run(200, 100);
        Thread.sleep(500);
        long platformTime2 = PlatformThreadBenchmark.run(200, 100);

        System.out.printf("\n📊 Result: Virtual threads %.2fx faster%n",
            (double) platformTime2 / virtualTime2);

        // Test Case 3: Heavy I/O with many tasks
        System.out.println("\n\n━━━ TEST CASE 3: Heavy I/O (100ms) with 2000 tasks ━━━");
        long virtualTime3 = VirtualThreadBenchmark.run(2000, 100);
        Thread.sleep(500);
        long platformTime3 = PlatformThreadBenchmark.run(2000, 100);

        System.out.printf("\n📊 Result: Virtual threads %.2fx faster%n",
            (double) platformTime3 / virtualTime3);

        // Summary
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    BENCHMARK SUMMARY                       ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Test 1 (500 tasks, 10ms I/O):  %.2fx faster                 ║%n",
            (double) platformTime1 / virtualTime1);
        System.out.printf("║ Test 2 (200 tasks, 100ms I/O): %.2fx faster                 ║%n",
            (double) platformTime2 / virtualTime2);
        System.out.printf("║ Test 3 (2000 tasks, 100ms I/O): %.2fx faster                ║%n",
            (double) platformTime3 / virtualTime3);
        System.out.println("║                                                            ║");
        System.out.println("║ Key Insight: Virtual threads excel at I/O-bound workloads  ║");
        System.out.println("║ They provide better throughput and resource efficiency     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
}

