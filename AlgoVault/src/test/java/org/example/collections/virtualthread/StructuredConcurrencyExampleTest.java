package org.example.collections.virtualthread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for StructuredConcurrencyExample.
 * Tests three different concurrency coordination patterns: CountDownLatch, Phaser, and ExecutorService.
 */
@DisplayName("Structured Concurrency Example Test Suite")
class StructuredConcurrencyExampleTest {

    /**
     * Test that the complete example runs without errors.
     */
    @Test
    @DisplayName("Structured concurrency example should complete successfully")
    @Timeout(120)
    void testExampleCompletes() {
        assertDoesNotThrow(() -> {
            StructuredConcurrencyExample.main(new String[]{});
        });
    }

    /**
     * Test CountDownLatch example with worker coordination.
     */
    @Test
    @DisplayName("CountDownLatch example should synchronize multiple workers")
    @Timeout(30)
    void testCountDownLatchExample() {
        assertDoesNotThrow(() -> {
            StructuredConcurrencyExample.example1_CountDownLatch();
        }, "CountDownLatch should coordinate workers");
    }

    /**
     * Test Phaser example with multi-phase execution.
     */
    @Test
    @DisplayName("Phaser example should coordinate multi-phase execution")
    @Timeout(30)
    void testPhaserExample() {
        assertDoesNotThrow(() -> {
            StructuredConcurrencyExample.example2_Phaser();
        }, "Phaser should coordinate multiple phases");
    }

    /**
     * Test ExecutorService with error handling.
     */
    @Test
    @DisplayName("ExecutorService should handle worker errors gracefully")
    @Timeout(30)
    void testExecutorWithErrorHandling() {
        assertDoesNotThrow(() -> {
            StructuredConcurrencyExample.example3_ExecutorWithErrorHandling();
        }, "ExecutorService should handle exceptions properly");
    }
}

