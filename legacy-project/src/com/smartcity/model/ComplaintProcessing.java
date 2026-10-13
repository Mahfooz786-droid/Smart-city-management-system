package com.smartcity.model;

public class ComplaintProcessing implements Runnable {

    private static int complaintCount = 0;

    private String complaint;

    public ComplaintProcessing(String complaint) {
        this.complaint = complaint;
    }

    // Synchronized method
    // Only one thread can process this method at a time
    public synchronized void processComplaint() {

        complaintCount++;

        System.out.println(
                Thread.currentThread().getName()
                        + " started processing: "
                        + complaint
        );

        try {

            // Simulating complaint processing time
            Thread.sleep(500);

        } catch (InterruptedException e) {

            System.out.println(
                    "Complaint processing interrupted: "
                            + complaint
            );

            Thread.currentThread().interrupt();
        }

        System.out.println(
                Thread.currentThread().getName()
                        + " completed: "
                        + complaint
        );
    }

    @Override
    public void run() {

        processComplaint();
    }

    public static int getComplaintCount() {

        return complaintCount;
    }
}