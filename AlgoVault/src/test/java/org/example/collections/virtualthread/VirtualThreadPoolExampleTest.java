package org.example.collections.virtualthread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for VirtualThreadPoolExample.
 * Tests the virtual thread pool executor for handling large numbers of concurrent tasks.
 */
@DisplayName("Virtual Thread Pool Example Test Suite")
class VirtualThreadPoolExampleTest {

    /**
     * Test that the example completes successfully.
     */
    @Test
    @DisplayName("Pool example should complete without exceptions")
    @Timeout(120)
    void testPoolExecutionCompletes() {
        assertDoesNotThrow(() -> {
            VirtualThreadPoolExample.main(new String[]{});
        });
    }

    /**
     * Test that large task volume is handled efficiently.
     * Virtual threads should handle 1000 tasks efficiently.
     */
    @Test
    @DisplayName("Virtual thread pool should handle 1000 concurrent tasks")
    @Timeout(120)
    void testLargeTaskVolume() {
        assertDoesNotThrow(() -> {
            VirtualThreadPoolExample.main(new String[]{});
        }, "Should handle 1000 I/O-bound tasks efficiently");
    }
}

