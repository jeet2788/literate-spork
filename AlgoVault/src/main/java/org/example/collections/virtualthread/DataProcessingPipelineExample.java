package org.example.collections.virtualthread;

import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.time.Duration;
import java.time.Instant;

/**
 * Advanced example: Parallel data processing pipeline using virtual threads.
 * Demonstrates how to build efficient multi-stage data processing workflows.
 *
 * Pipeline stages:
 * 1. Data Generation - Creates synthetic data
 * 2. Validation - Validates incoming data
 * 3. Transformation - Transforms data format
 * 4. Aggregation - Aggregates results
 */
public class DataProcessingPipelineExample {

    /**
     * Represents a data item flowing through the pipeline.
     */
    static class DataItem {
        final int id;
        final String rawData;
        final long timestamp;

        DataItem(int id, String rawData) {
            this.id = id;
            this.rawData = rawData;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return String.format("DataItem(id=%d, data=%s, ts=%d)", id, rawData, timestamp);
        }
    }

    /**
     * Represents processed data with validation and transformation results.
     */
    static class ProcessedDataItem extends DataItem {
        final boolean isValid;
        final String transformedData;
        final long processingTime;

        ProcessedDataItem(DataItem item, boolean isValid, String transformedData, long processingTime) {
            super(item.id, item.rawData);
            this.isValid = isValid;
            this.transformedData = transformedData;
            this.processingTime = processingTime;
        }
    }

    /**
     * Stage 1: Generate raw data items.
     */
    static class GenerationStage implements Callable<DataItem> {
        private final int itemId;

        GenerationStage(int itemId) {
            this.itemId = itemId;
        }

        @Override
        public DataItem call() throws Exception {
            Thread.sleep((long) (Math.random() * 10));
            String data = "RAW_DATA_" + itemId + "_" + System.nanoTime();
            return new DataItem(itemId, data);
        }
    }

    /**
     * Stage 2: Validate data items.
     */
    static class ValidationStage implements Callable<ProcessedDataItem> {
        private final DataItem item;

        ValidationStage(DataItem item) {
            this.item = item;
        }

        @Override
        public ProcessedDataItem call() throws Exception {
            long start = System.currentTimeMillis();
            Thread.sleep((long) (Math.random() * 20));

            // Validate: even IDs are valid
            boolean isValid = item.id % 2 == 0;

            long processingTime = System.currentTimeMillis() - start;
            return new ProcessedDataItem(item, isValid,
                "VALIDATED_" + item.rawData, processingTime);
        }
    }

    /**
     * Stage 3: Transform valid data.
     */
    static class TransformationStage implements Callable<ProcessedDataItem> {
        private final ProcessedDataItem item;

        TransformationStage(ProcessedDataItem item) {
            this.item = item;
        }

        @Override
        public ProcessedDataItem call() throws Exception {
            if (!item.isValid) {
                return item;
            }

            long start = System.currentTimeMillis();
            Thread.sleep((long) (Math.random() * 30));

            String transformed = item.transformedData.toUpperCase() + "_TRANSFORMED";
            long processingTime = System.currentTimeMillis() - start;

            return new ProcessedDataItem(item, true, transformed,
                item.processingTime + processingTime);
        }
    }

    /**
     * Runs the complete three-stage pipeline.
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int totalItems = 100;

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     Parallel Data Processing Pipeline with Virtual Threads ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        Instant pipelineStart = Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            // ==================== STAGE 1: GENERATION ====================
            System.out.println("📊 STAGE 1: Generating " + totalItems + " data items...\n");
            Instant stage1Start = Instant.now();

            java.util.List<Future<DataItem>> generationFutures = new java.util.ArrayList<>();
            for (int i = 0; i < totalItems; i++) {
                generationFutures.add(executor.submit(new GenerationStage(i)));
            }

            java.util.List<DataItem> generatedItems = new java.util.ArrayList<>();
            for (Future<DataItem> future : generationFutures) {
                generatedItems.add(future.get());
            }

            Duration stage1Duration = Duration.between(stage1Start, Instant.now());
            System.out.printf("✓ Generated %d items in %d ms\n\n",
                generatedItems.size(), stage1Duration.toMillis());

            // ==================== STAGE 2: VALIDATION ====================
            System.out.println("✓ STAGE 2: Validating data items...\n");
            Instant stage2Start = Instant.now();

            java.util.List<Future<ProcessedDataItem>> validationFutures = new java.util.ArrayList<>();
            for (DataItem item : generatedItems) {
                validationFutures.add(executor.submit(new ValidationStage(item)));
            }

            java.util.List<ProcessedDataItem> validatedItems = new java.util.ArrayList<>();
            int validCount = 0;
            for (Future<ProcessedDataItem> future : validationFutures) {
                ProcessedDataItem processed = future.get();
                validatedItems.add(processed);
                if (processed.isValid) validCount++;
            }

            Duration stage2Duration = Duration.between(stage2Start, Instant.now());
            System.out.printf("✓ Validated %d items (%d valid) in %d ms\n\n",
                validatedItems.size(), validCount, stage2Duration.toMillis());

            // ==================== STAGE 3: TRANSFORMATION ====================
            System.out.println("⚙ STAGE 3: Transforming valid data...\n");
            Instant stage3Start = Instant.now();

            java.util.List<Future<ProcessedDataItem>> transformationFutures = new java.util.ArrayList<>();
            for (ProcessedDataItem item : validatedItems) {
                transformationFutures.add(executor.submit(new TransformationStage(item)));
            }

            java.util.List<ProcessedDataItem> finalItems = new java.util.ArrayList<>();
            int transformedCount = 0;
            for (Future<ProcessedDataItem> future : transformationFutures) {
                ProcessedDataItem transformed = future.get();
                finalItems.add(transformed);
                if (transformed.isValid) transformedCount++;
            }

            Duration stage3Duration = Duration.between(stage3Start, Instant.now());
            System.out.printf("✓ Transformed %d items in %d ms\n\n",
                transformedCount, stage3Duration.toMillis());

            // ==================== FINAL AGGREGATION ====================
            System.out.println("📈 FINAL AGGREGATION:\n");

            long totalProcessingTime = 0;
            for (ProcessedDataItem item : finalItems) {
                totalProcessingTime += item.processingTime;
            }

            double avgProcessingTime = (double) totalProcessingTime / totalItems;

            System.out.printf("Total Input Items: %d%n", totalItems);
            System.out.printf("Valid Items: %d (%.1f%%)%n", validCount,
                (validCount * 100.0) / totalItems);
            System.out.printf("Transformed Items: %d (%.1f%%)%n", transformedCount,
                (transformedCount * 100.0) / validCount);
            System.out.printf("Average Processing Time per Item: %.2f ms%n", avgProcessingTime);

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            Duration totalDuration = Duration.between(pipelineStart, Instant.now());

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.printf("║ Total Pipeline Time: %d ms                         ║%n",
                totalDuration.toMillis());
            System.out.printf("║ Stage 1 (Gen): %d ms | Stage 2 (Val): %d ms | Stage 3: %d ms ║%n",
                stage1Duration.toMillis(), stage2Duration.toMillis(), stage3Duration.toMillis());
            System.out.println("║ Pipeline execution completed successfully!                 ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        }
    }
}

