package com.smartcity.model;

public class ThreadDemo {

    public static void main(String[] args) {

        System.out.println(
                "===== SMART CITY COMPLAINT PROCESSING ====="
        );

        ComplaintProcessing complaint1 =
                new ComplaintProcessing(
                        "Street Light Not Working"
                );

        ComplaintProcessing complaint2 =
                new ComplaintProcessing(
                        "Garbage Collection Issue"
                );

        ComplaintProcessing complaint3 =
                new ComplaintProcessing(
                        "Water Supply Problem"
                );


        Thread t1 =
                new Thread(
                        complaint1,
                        "Complaint-Thread-1"
                );

        Thread t2 =
                new Thread(
                        complaint2,
                        "Complaint-Thread-2"
                );

        Thread t3 =
                new Thread(
                        complaint3,
                        "Complaint-Thread-3"
                );


        // Starting multiple threads
        t1.start();
        t2.start();
        t3.start();


        // Waiting for all threads
        try {

            t1.join();
            t2.join();
            t3.join();

        } catch (InterruptedException e) {

            System.out.println(
                    "Main thread interrupted."
            );

            Thread.currentThread().interrupt();
        }


        System.out.println(
                "\nTotal Complaints Processed: "
                        + ComplaintProcessing.getComplaintCount()
        );

        System.out.println(
                "===== PROCESSING COMPLETED ====="
        );
    }
}