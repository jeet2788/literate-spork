package org.example.collections.virtualthread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BasicVirtualThreads.
 * Tests the virtual thread creation and execution behavior.
 */
@DisplayName("BasicVirtualThreads Test Suite")
class BasicVirtualThreadsTest {

    /**
     * Test that the main method executes without throwing exceptions.
     */
    @Test
    @DisplayName("Main method should execute without exceptions")
    void testMainMethodExecutesSuccessfully() {
        assertDoesNotThrow(() -> {
            BasicVirtualThreads.main(new String[]{});
        });
    }

    /**
     * Test that the main method produces output.
     * Captures stdout to verify that "Done:" messages are printed.
     */
    @Test
    @DisplayName("Main method should print completion messages from virtual threads")
    void testMainMethodPrintsOutput() throws InterruptedException {
        // Capture standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(outContent));

            // Run main method
            BasicVirtualThreads.main(new String[]{});

            // Give threads time to complete
            Thread.sleep(2000);

            // Restore original output
            System.setOut(originalOut);

            String output = outContent.toString();

            // Verify output contains "Done:" messages
            assertTrue(output.contains("Done:"),
                "Output should contain 'Done:' messages from virtual threads");
            assertTrue(output.contains("Time:"),
                "Output should contain execution time");

        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Test that virtual threads are created with correct naming pattern.
     * Verifies that thread names follow the "vt-i" pattern.
     */
    @Test
    @DisplayName("Virtual threads should be named with vt- prefix")
    void testVirtualThreadNaming() throws InterruptedException {
        // Capture standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(outContent));

            // Run main method
            BasicVirtualThreads.main(new String[]{});

            // Give threads time to complete
            Thread.sleep(2000);

            // Restore original output
            System.setOut(originalOut);

            String output = outContent.toString();

            // Verify that threads are named correctly
            for (int i = 0; i < 10; i++) {
                assertTrue(output.contains("vt-" + i),
                    "Output should contain thread name 'vt-" + i + "'");
            }

        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Test that the execution completes in reasonable time.
     * Verifies that the program doesn't hang or take excessively long.
     */
    @Test
    @DisplayName("Main method should complete in reasonable time")
    void testExecutionCompletes() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        BasicVirtualThreads.main(new String[]{});

        // Give threads time to complete
        Thread.sleep(2000);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Should complete within 10 seconds (allowing buffer for system load)
        assertTrue(duration < 10000,
            "Execution should complete within 10 seconds, took: " + duration + "ms");
    }
}

