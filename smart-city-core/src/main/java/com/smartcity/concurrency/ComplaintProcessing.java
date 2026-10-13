package com.smartcity.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A thread-safe processor for incoming complaints.
 *
 * <p><b>Why this class exists / what was broken before.</b><br>
 * The original {@code ComplaintProcessing} used an instance
 * {@code synchronized} method to guard a {@code static int complaintCount}
 * field. That is incorrect: synchronising an instance method locks on
 * {@code this}, so two different {@code ComplaintProcessing} instances
 * could both be inside {@code processComplaint()} simultaneously and
 * both increment the static counter — losing updates.
 *
 * <p><b>Fix.</b><br>
 * The counter is now an {@link AtomicInteger} (lock-free, atomic
 * incrementAndGet). Long-running work is delegated to an
 * {@link java.util.concurrent.ExecutorService} (see
 * {@link ComplaintProcessingExecutor}) so the Swing EDT is never blocked.
 *
 * <p><b>Interrupt handling.</b><br>
 * {@link InterruptedException} is caught and the interrupt flag is restored
 * via {@code Thread.currentThread().interrupt();} so any outer blocking
 * call still sees the interruption.
 */
public class ComplaintProcessing implements Runnable {

    /** Shared, lock-free counter — visible to all worker threads. */
    private static final AtomicInteger complaintCount = new AtomicInteger(0);

    /** Resets the counter. Used by tests so they can run deterministically. */
    public static void resetCount() {
        complaintCount.set(0);
    }

    /** Returns the number of complaints processed since the last reset. */
    public static int getComplaintCount() {
        return complaintCount.get();
    }

    private final String complaint;

    public ComplaintProcessing(String complaint) {
        this.complaint = complaint;
    }

    @Override
    public void run() {
        try {
            processComplaint();
        } catch (InterruptedException e) {
            // Restore the interrupt flag — never swallow InterruptedException.
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Increments the counter (atomically), simulates processing time,
     * then logs completion.
     *
     * <p>No {@code synchronized} keyword needed: {@code AtomicInteger}
     * guarantees atomic read-modify-write across threads.
     */
    public void processComplaint() throws InterruptedException {
        int myNumber = complaintCount.incrementAndGet();

        System.out.println(Thread.currentThread().getName()
                + " started processing #" + myNumber + ": " + complaint);

        // Simulate DB write / business work
        Thread.sleep(50);

        System.out.println(Thread.currentThread().getName()
                + " completed #" + myNumber + ": " + complaint);
    }
}
