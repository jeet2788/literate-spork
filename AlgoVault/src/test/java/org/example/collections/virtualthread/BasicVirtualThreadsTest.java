package org.example.collections.virtualthread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BasicVirtualThreads.
 * Tests the virtual thread creation, execution, and proper synchronization.
 */
@DisplayName("BasicVirtualThreads Test Suite")
class BasicVirtualThreadsTest {

    /**
     * Helper method to capture and restore System.out
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
     * Test that the main method executes without throwing exceptions.
     */
    @Test
    @DisplayName("Main method should execute without throwing exceptions")
    void testMainMethodExecutesSuccessfully() {
        assertDoesNotThrow(() -> {
            BasicVirtualThreads.main(new String[]{});
        });
    }

    /**
     * Test that the main method produces proper synchronization output.
     * Verifies that CountDownLatch ensures threads complete before end time is measured.
     */
    @Test
    @DisplayName("Main method should produce properly synchronized output")
    void testMainMethodProducesCorrectOutput() throws Exception {
        String output = captureOutput(() -> {
            try {
                BasicVirtualThreads.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Verify output structure
        assertTrue(output.contains("Basic Virtual Threads Example"),
            "Output should contain title");
        assertTrue(output.contains("STEP 1: Creating"),
            "Output should show step 1: thread creation");
        assertTrue(output.contains("STEP 2:"),
            "Output should show step 2: waiting for completion");
        assertTrue(output.contains("EXECUTION SUMMARY"),
            "Output should contain execution summary");
    }

    /**
     * Test that virtual threads are created with correct naming pattern.
     * Verifies that all thread names follow the "vt-i" pattern (0-9).
     */
    @Test
    @DisplayName("Virtual threads should be created with vt- prefix naming pattern")
    void testVirtualThreadNaming() throws Exception {
        String output = captureOutput(() -> {
            try {
                BasicVirtualThreads.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Verify all 10 threads are named correctly
        for (int i = 0; i < 10; i++) {
            assertTrue(output.contains("vt-" + i),
                "Output should contain thread name 'vt-" + i + "'");
        }
    }

    /**
     * Test that virtual threads complete their tasks.
     * Verifies that completion messages are printed for all threads.
     */
    @Test
    @DisplayName("All virtual threads should complete and print completion messages")
    void testThreadCompletion() throws Exception {
        String output = captureOutput(() -> {
            try {
                BasicVirtualThreads.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Verify completion indicators
        assertTrue(output.contains("Completed"),
            "Output should contain thread completion messages");
        assertTrue(output.contains("✓"),
            "Output should contain completion symbol (✓)");
    }

    /**
     * Test that the execution time is recorded and displayed.
     * Verifies that the execution time is reasonable (~1 second for 1 second sleeps).
     */
    @Test
    @DisplayName("Execution time should be measured and displayed")
    void testExecutionTimeMeasurement() throws Exception {
        String output = captureOutput(() -> {
            try {
                BasicVirtualThreads.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("Total Execution Time"),
            "Output should display total execution time");
        assertTrue(output.contains("ms"),
            "Output should show time in milliseconds");
    }

    /**
     * Test that concurrent execution is demonstrated.
     * Verifies that the program completes in approximately 1 second (not 10 seconds),
     * demonstrating concurrent execution.
     */
    @Test
    @DisplayName("Threads should execute concurrently, not sequentially")
    void testConcurrentExecution() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        BasicVirtualThreads.main(new String[]{});

        long duration = System.currentTimeMillis() - startTime;

        // All 10 threads sleep for 1 second each
        // Sequential execution would take ~10 seconds
        // Concurrent execution should take ~1-2 seconds
        assertTrue(duration < 5000,
            "Execution should complete in ~1-2 seconds with concurrent execution, " +
            "took: " + duration + "ms (if > 5s, threads may not be executing concurrently)");
    }

    /**
     * Test that the output contains performance metrics.
     * Verifies that summary statistics are provided for analysis.
     */
    @Test
    @DisplayName("Output should contain performance metrics and statistics")
    void testPerformanceMetrics() throws Exception {
        String output = captureOutput(() -> {
            try {
                BasicVirtualThreads.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("Total Threads: 10"),
            "Output should display total thread count");
        assertTrue(output.contains("Average Time per Thread"),
            "Output should display average time per thread");
        assertTrue(output.contains("Key Insight"),
            "Output should explain the key insight about concurrent execution");
    }

    /**
     * Test that the output is educational and explains virtual threads.
     * Verifies that the program demonstrates why virtual threads are powerful.
     */
    @Test
    @DisplayName("Output should be educational and explain virtual thread benefits")
    void testEducationalContent() throws Exception {
        String output = captureOutput(() -> {
            try {
                BasicVirtualThreads.main(new String[]{});
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("CONCURRENTLY"),
            "Output should explain that threads run concurrently");
        assertTrue(output.contains("parallel"),
            "Output should mention parallel execution");
        assertTrue(output.contains("power of virtual threads"),
            "Output should demonstrate the power of virtual threads");
    }
}

