package org.example.collections.virtualthread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.time.Duration;
import java.time.Instant;

/**
 * Real-world example: High-performance web server simulator using virtual threads.
 * Demonstrates how virtual threads enable handling thousands of concurrent connections
 * with minimal resource overhead.
 *
 * This mimics handling HTTP requests where each request involves:
 * - Connection setup
 * - Request processing (database query simulation)
 * - Response generation
 * - Connection cleanup
 */
public class HighPerformanceServerExample {

    /**
     * Represents an HTTP request with timing and statistics.
     */
    static class HTTPRequest {
        final int requestId;
        final String endpoint;
        final long arrivalTime;

        HTTPRequest(int requestId, String endpoint) {
            this.requestId = requestId;
            this.endpoint = endpoint;
            this.arrivalTime = System.currentTimeMillis();
        }
    }

    /**
     * Represents a request handler that processes HTTP requests.
     */
    static class RequestHandler implements Runnable {
        private final HTTPRequest request;
        private final AtomicLong processedRequests;
        private final AtomicLong totalResponseTime;

        RequestHandler(HTTPRequest request, AtomicLong processedRequests, AtomicLong totalResponseTime) {
            this.request = request;
            this.processedRequests = processedRequests;
            this.totalResponseTime = totalResponseTime;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            try {
                // Phase 1: Receive and parse request
                Thread.sleep(10);

                // Phase 2: Process request (simulate database query)
                long processingTime = (long) (Math.random() * 100);
                Thread.sleep(processingTime);

                // Phase 3: Generate response
                Thread.sleep(5);

                long endTime = System.currentTimeMillis();
                long responseTime = endTime - startTime;

                processedRequests.incrementAndGet();
                totalResponseTime.addAndGet(responseTime);

                if (request.requestId % 100 == 0) {
                    System.out.printf("[REQ-%04d] %s - Response: %d ms on %s%n",
                        request.requestId, request.endpoint, responseTime,
                        Thread.currentThread().getName());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.printf("[REQ-%04d] Interrupted%n", request.requestId);
            }
        }
    }

    /**
     * Simulates a high-performance web server handling concurrent requests using virtual threads.
     *
     * @param concurrentRequests number of concurrent requests to simulate
     * @param requestsPerSecond rate at which new requests arrive
     */
    public static void runServer(int concurrentRequests, int requestsPerSecond) throws InterruptedException {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.printf("║ Server: Handling %d concurrent requests @ %d req/s        ║%n",
            concurrentRequests, requestsPerSecond);
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        final String[] endpoints = {
            "/api/users", "/api/products", "/api/orders",
            "/api/search", "/api/analytics", "/health"
        };

        AtomicLong processedRequests = new AtomicLong(0);
        AtomicLong totalResponseTime = new AtomicLong(0);

        Instant startTime = Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            // Simulate incoming requests
            for (int i = 0; i < concurrentRequests; i++) {
                String endpoint = endpoints[i % endpoints.length];
                HTTPRequest request = new HTTPRequest(i, endpoint);

                // Submit request handler
                executor.submit(new RequestHandler(request, processedRequests, totalResponseTime));

                // Simulate request arrival rate
                if (i % requestsPerSecond == 0 && i > 0) {
                    Thread.sleep(1000);
                }
            }

            // Graceful shutdown
            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.MINUTES);

            Instant endTime = Instant.now();
            Duration totalDuration = Duration.between(startTime, endTime);

            // Display server statistics
            long processed = processedRequests.get();
            long totalTime = totalResponseTime.get();

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║               SERVER STATISTICS                            ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝\n");

            System.out.printf("Total Requests Processed: %,d%n", processed);
            System.out.printf("Total Execution Time: %d ms%n", totalDuration.toMillis());
            System.out.printf("Average Response Time: %.2f ms%n", (double) totalTime / processed);
            System.out.printf("Requests Per Second: %.2f%n",
                (processed * 1000.0) / totalDuration.toMillis());
            System.out.printf("Thread Count: %d (virtual threads)%n%n",
                Thread.activeCount());
        }
    }

    /**
     * Demonstrates performance comparison between different load scenarios.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   High-Performance Web Server with Virtual Threads         ║");
        System.out.println("║                                                            ║");
        System.out.println("║ This example demonstrates how virtual threads enable       ║");
        System.out.println("║ efficient handling of thousands of I/O-bound requests      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        // Scenario 1: Light load
        System.out.println("\n\n📊 SCENARIO 1: Light Load");
        runServer(100, 10);
        Thread.sleep(1000);

        // Scenario 2: Medium load
        System.out.println("\n\n📊 SCENARIO 2: Medium Load");
        runServer(500, 50);
        Thread.sleep(1000);

        // Scenario 3: Heavy load
        System.out.println("\n\n📊 SCENARIO 3: Heavy Load");
        runServer(2000, 200);

        System.out.println("\n\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  ✓ Server simulation completed successfully!               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }
}

