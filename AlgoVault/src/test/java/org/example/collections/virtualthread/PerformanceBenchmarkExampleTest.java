package org.example.collections.virtualthread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for PerformanceBenchmarkExample.
 * Tests comparative performance between virtual threads and platform threads.
 */
@DisplayName("Performance Benchmark Example Test Suite")
class PerformanceBenchmarkExampleTest {

    /**
     * Helper method to capture System.out during execution.
     */
    private String captureOutput(Runnable task) throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(outContent));
            task.run();
            return outContent.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Test that the benchmark example completes successfully.
     */
    @Test
    @DisplayName("Benchmark example should complete without exceptions")
    @Timeout(180)
    void testBenchmarkCompletes() {
        assertDoesNotThrow(() -> {
            PerformanceBenchmarkExample.main(new String[]{});
        });
    }

    /**
     * Test that benchmark displays comparison results.
     */
    @Test
    @DisplayName("Benchmark should display virtual threads vs platform threads comparison")
    @Timeout(180)
    void testBenchmarkComparison() throws Exception {
        String output = captureOutput(() -> {
            try {
                PerformanceBenchmarkExample.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("VIRTUAL THREADS"),
            "Output should show virtual threads results");
        assertTrue(output.contains("PLATFORM THREADS"),
            "Output should show platform threads results");
    }

    /**
     * Test that benchmark includes all three test cases.
     */
    @Test
    @DisplayName("Benchmark should include all three test cases")
    @Timeout(180)
    void testAllTestCases() throws Exception {
        String output = captureOutput(() -> {
            try {
                PerformanceBenchmarkExample.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("TEST CASE 1"),
            "Output should contain Test Case 1 (Light I/O)");
        assertTrue(output.contains("TEST CASE 2"),
            "Output should contain Test Case 2 (Medium I/O)");
        assertTrue(output.contains("TEST CASE 3"),
            "Output should contain Test Case 3 (Heavy I/O)");
    }

    /**
     * Test that benchmark shows virtual threads are faster.
     */
    @Test
    @DisplayName("Benchmark should demonstrate virtual threads performance advantage")
    @Timeout(180)
    void testPerformanceAdvantage() throws Exception {
        String output = captureOutput(() -> {
            try {
                PerformanceBenchmarkExample.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("faster"),
            "Output should indicate virtual threads are faster");
    }

    /**
     * Test that benchmark shows throughput metrics.
     */
    @Test
    @DisplayName("Benchmark should display throughput metrics")
    @Timeout(180)
    void testThroughputMetrics() throws Exception {
        String output = captureOutput(() -> {
            try {
                PerformanceBenchmarkExample.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("Throughput") || output.contains("throughput"),
            "Output should display throughput metrics");
        assertTrue(output.contains("tasks/sec"),
            "Output should show tasks per second");
    }

    /**
     * Test that benchmark provides clear summary.
     */
    @Test
    @DisplayName("Benchmark should provide clear summary with insights")
    @Timeout(180)
    void testBenchmarkSummary() throws Exception {
        String output = captureOutput(() -> {
            try {
                PerformanceBenchmarkExample.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("BENCHMARK SUMMARY"),
            "Output should contain summary section");
        assertTrue(output.contains("I/O-bound"),
            "Output should mention I/O-bound workloads");
    }

    /**
     * Test that virtual thread benchmark runs efficiently.
     */
    @Test
    @DisplayName("Virtual thread benchmark should complete efficiently")
    @Timeout(60)
    void testVirtualThreadBenchmarkEfficiency() {
        long startTime = System.currentTimeMillis();

        assertDoesNotThrow(() -> {
            PerformanceBenchmarkExample.VirtualThreadBenchmark.run(500, 10);
        });

        long duration = System.currentTimeMillis() - startTime;
        // Virtual threads should handle 500 tasks with 10ms I/O in < 30 seconds
        assertTrue(duration < 30000,
            "Virtual thread benchmark should complete efficiently in ~1-2 seconds, took: " + duration + "ms");
    }
}

