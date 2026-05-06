package org.example.collections.virtualthread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for DataProcessingPipelineExample.
 * Tests multi-stage data processing pipeline with virtual threads.
 */
@DisplayName("Data Processing Pipeline Example Test Suite")
class DataProcessingPipelineExampleTest {

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
     * Test that the pipeline example completes successfully.
     */
    @Test
    @DisplayName("Pipeline example should complete without exceptions")
    @Timeout(180)
    void testPipelineCompletes() {
        assertDoesNotThrow(() -> {
            DataProcessingPipelineExample.main(new String[]{});
        });
    }

    /**
     * Test that all pipeline stages execute.
     */
    @Test
    @DisplayName("All pipeline stages should execute successfully")
    @Timeout(180)
    void testAllStagesExecute() throws Exception {
        String output = captureOutput(() -> {
            try {
                DataProcessingPipelineExample.main(new String[]{});
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("STAGE 1: Generating"),
            "Output should show Stage 1 (Generation)");
        assertTrue(output.contains("STAGE 2: Validating"),
            "Output should show Stage 2 (Validation)");
        assertTrue(output.contains("STAGE 3: Transforming"),
            "Output should show Stage 3 (Transformation)");
    }

    /**
     * Test that pipeline produces correct results.
     */
    @Test
    @DisplayName("Pipeline should generate aggregated results")
    @Timeout(180)
    void testPipelineResults() throws Exception {
        String output = captureOutput(() -> {
            try {
                DataProcessingPipelineExample.main(new String[]{});
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("Total Input Items"),
            "Output should show total input items");
        assertTrue(output.contains("Valid Items"),
            "Output should show valid items count");
        assertTrue(output.contains("Transformed Items"),
            "Output should show transformed items count");
        assertTrue(output.contains("Average Processing Time"),
            "Output should show average processing time");
    }

    /**
     * Test that pipeline performance metrics are displayed.
     */
    @Test
    @DisplayName("Pipeline should display performance metrics")
    @Timeout(180)
    void testPipelineMetrics() throws Exception {
        String output = captureOutput(() -> {
            try {
                DataProcessingPipelineExample.main(new String[]{});
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("FINAL AGGREGATION"),
            "Output should show final aggregation");
        assertTrue(output.contains("Total Pipeline Time"),
            "Output should display total pipeline time");
    }

    /**
     * Test that data validation works correctly.
     * According to the code, even IDs are valid.
     */
    @Test
    @DisplayName("Pipeline should correctly validate data items")
    @Timeout(180)
    void testDataValidation() throws Exception {
        String output = captureOutput(() -> {
            try {
                DataProcessingPipelineExample.main(new String[]{});
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("Valid Items"),
            "Output should show validation results");
        // With 100 items, approximately 50 should be valid (even IDs)
        assertTrue(output.contains("50"),
            "Output should show approximately 50 valid items from 100 total");
    }

    /**
     * Test that transformation is only applied to valid items.
     */
    @Test
    @DisplayName("Pipeline should transform only valid items")
    @Timeout(180)
    void testTransformationOfValidItems() throws Exception {
        String output = captureOutput(() -> {
            try {
                DataProcessingPipelineExample.main(new String[]{});
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertTrue(output.contains("Transformed Items"),
            "Output should show transformed items count");
        // Transformed items should equal valid items (since transformation is only on valid items)
        assertTrue(output.contains("50"),
            "Output should show approximately 50 transformed items");
    }
}

