package com.smartcity.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * A small thread-pool abstraction around {@link ExecutorService} for
 * submitting {@link ComplaintProcessing} tasks.
 *
 * <p>Keeps long-running complaint work off the Swing EDT in the GUI module,
 * and off the request thread in the Web module (where it can be used for
 * bulk processing jobs). Uses a fixed pool so we never spawn an unbounded
 * number of threads under load.
 *
 * <p>This class is intentionally simple — it owns a single
 * {@link ExecutorService} for the lifetime of the JVM. Callers that need
 * finer control can construct their own pool and submit
 * {@link ComplaintProcessing} tasks directly.
 */
public class ComplaintProcessingExecutor implements AutoCloseable {

    private final ExecutorService pool;

    public ComplaintProcessingExecutor(int poolSize) {
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    /** Submits a complaint for asynchronous processing. */
    public Future<?> submit(ComplaintProcessing task) {
        return pool.submit(task);
    }

    /**
     * Submits many complaints and waits for all of them to finish (or for
     * the timeout to elapse).
     *
     * @param tasks    the complaints to process
     * @param timeout  how long to wait
     * @param unit     unit of {@code timeout}
     * @return {@code true} if all tasks terminated normally,
     *         {@code false} if the timeout elapsed first
     * @throws InterruptedException if the current thread was interrupted
     *                              while waiting; the interrupt flag is
     *                              preserved.
     */
    public boolean processAll(List<ComplaintProcessing> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        List<Future<?>> futures = new ArrayList<>(tasks.size());
        for (ComplaintProcessing t : tasks) {
            futures.add(pool.submit(t));
        }

        boolean allDone = pool.awaitTermination(timeout, unit);
        // awaitTermination with a fixed pool that was never shut down returns false;
        // use a per-future check instead.
        boolean allDoneByFuture = true;
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (Exception e) {
                allDoneByFuture = false;
            }
        }
        return allDoneByFuture;
    }

    /** Shuts the pool down. Safe to call more than once. */
    @Override
    public void close() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
