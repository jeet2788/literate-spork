package org.example.collections.virtualthread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for HighPerformanceServerExample.
 * Tests simulated web server handling concurrent HTTP requests using virtual threads.
 */
@DisplayName("High-Performance Server Example Test Suite")
class HighPerformanceServerExampleTest {

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
     * Test that the server example completes successfully.
     */
    @Test
    @DisplayName("Server example should complete without exceptions")
    @Timeout(180)
    void testServerCompletes() {
        assertDoesNotThrow(() -> {
            HighPerformanceServerExample.main(new String[]{});
        });
    }

    /**
     * Test light load scenario (100 concurrent requests).
     */
    @Test
    @DisplayName("Server should handle light load (100 concurrent requests)")
    @Timeout(60)
    void testLightLoadScenario() {
        assertDoesNotThrow(() -> {
            HighPerformanceServerExample.runServer(100, 10);
        });
    }

    /**
     * Test medium load scenario (500 concurrent requests).
     */
    @Test
    @DisplayName("Server should handle medium load (500 concurrent requests)")
    @Timeout(60)
    void testMediumLoadScenario() {
        assertDoesNotThrow(() -> {
            HighPerformanceServerExample.runServer(500, 50);
        });
    }

    /**
     * Test heavy load scenario (2000 concurrent requests).
     */
    @Test
    @DisplayName("Server should handle heavy load (2000 concurrent requests)")
    @Timeout(120)
    void testHeavyLoadScenario() {
        assertDoesNotThrow(() -> {
            HighPerformanceServerExample.runServer(2000, 200);
        });
    }

    /**
     * Test that server output contains performance statistics.
     */
    @Test
    @DisplayName("Server output should contain performance statistics")
    @Timeout(60)
    void testServerStatistics() throws Exception {
        String output = captureOutput(() -> {
            try {
                HighPerformanceServerExample.runServer(50, 5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("SERVER STATISTICS"),
            "Output should contain server statistics");
        assertTrue(output.contains("Requests Processed"),
            "Output should display requests processed");
        assertTrue(output.contains("Execution Time"),
            "Output should display execution time");
    }

    /**
     * Test that all requests are successfully processed.
     */
    @Test
    @DisplayName("All submitted requests should be processed")
    @Timeout(60)
    void testAllRequestsProcessed() throws Exception {
        String output = captureOutput(() -> {
            try {
                HighPerformanceServerExample.runServer(100, 10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("100"),
            "Output should show 100 requests were processed");
    }
}

