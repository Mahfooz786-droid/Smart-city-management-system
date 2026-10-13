package com.smartcity.ui;

import com.smartcity.database.ComplaintDAO;
import com.smartcity.database.FeedbackDAO;
import com.smartcity.database.ServiceDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CitizenDashboardFrame extends JFrame {

    private int citizenId;
    private String citizenName;

    public CitizenDashboardFrame(
            int citizenId,
            String citizenName) {

        this.citizenId = citizenId;
        this.citizenName = citizenName;

        // ==========================================
        // UI SETTINGS
        // ==========================================

        UIManager.put(
                "Button.font",
                new Font("Arial", Font.BOLD, 15)
        );

        UIManager.put(
                "Label.font",
                new Font("Arial", Font.PLAIN, 16)
        );

        UIManager.put(
                "OptionPane.messageFont",
                new Font("Arial", Font.PLAIN, 14)
        );

        // ==========================================
        // FRAME
        // ==========================================

        setTitle(
                "Smart City - Citizen Dashboard"
        );

        setSize(700, 620);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        getContentPane().setBackground(
                new Color(240, 244, 248)
        );

        // ==========================================
        // TITLE
        // ==========================================

        JLabel titleLabel =
                new JLabel(
                        "CITIZEN DASHBOARD",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 10, 10, 10
                )
        );

        JLabel welcomeLabel =
                new JLabel(
                        "Welcome, " + citizenName,
                        SwingConstants.CENTER
                );

        welcomeLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        18
                )
        );

        welcomeLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 10, 20, 10
                )
        );

        // ==========================================
        // BUTTONS
        // ==========================================

        JButton complaintButton =
                new JButton(
                        "Register Complaint"
                );

        JButton viewComplaintButton =
                new JButton(
                        "View My Complaints"
                );

        JButton serviceButton =
                new JButton(
                        "City Services"
                );

        JButton feedbackButton =
                new JButton(
                        "Give Feedback"
                );

        JButton logoutButton =
                new JButton(
                        "Logout"
                );

        // ==========================================
        // BUTTON PANEL
        // ==========================================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                5,
                                1,
                                12,
                                12
                        )
                );

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 120, 30, 120
                )
        );

        buttonPanel.setBackground(
                new Color(240, 244, 248)
        );

        buttonPanel.add(
                complaintButton
        );

        buttonPanel.add(
                viewComplaintButton
        );

        buttonPanel.add(
                serviceButton
        );

        buttonPanel.add(
                feedbackButton
        );

        buttonPanel.add(
                logoutButton
        );

        // ==========================================
        // BUTTON STYLING
        // ==========================================

        JButton[] buttons = {

                complaintButton,
                viewComplaintButton,
                serviceButton,
                feedbackButton,
                logoutButton
        };

        for (JButton button : buttons) {

            button.setFocusPainted(false);

            button.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            15
                    )
            );

            button.setPreferredSize(
                    new Dimension(
                            350,
                            48
                    )
            );
        }

        // ==========================================
        // TOP PANEL
        // ==========================================

        JPanel topPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        topPanel.setBackground(
                new Color(240, 244, 248)
        );

        topPanel.add(titleLabel);
        topPanel.add(welcomeLabel);

        // ==========================================
        // FRAME LAYOUT
        // ==========================================

        setLayout(
                new BorderLayout()
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        add(
                buttonPanel,
                BorderLayout.CENTER
        );


        // ==========================================
        // REGISTER COMPLAINT
        // ==========================================

        complaintButton.addActionListener(e -> {

            JTextField subjectField =
                    new JTextField();

            JTextArea descriptionArea =
                    new JTextArea(
                            5,
                            25
                    );

            descriptionArea.setLineWrap(true);

            descriptionArea.setWrapStyleWord(true);

            descriptionArea.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            14
                    )
            );

            JScrollPane descriptionScrollPane =
                    new JScrollPane(
                            descriptionArea
                    );

            Object[] fields = {

                    "Subject:",
                    subjectField,

                    "Description:",
                    descriptionScrollPane
            };

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            fields,
                            "Register Complaint",
                            JOptionPane.OK_CANCEL_OPTION
                    );

            if (result ==
                    JOptionPane.OK_OPTION) {

                try {

                    String subject =
                            subjectField
                                    .getText()
                                    .trim();

                    String description =
                            descriptionArea
                                    .getText()
                                    .trim();

                    if (subject.isEmpty()
                            || description.isEmpty()) {

                        throw new IllegalArgumentException(
                                "Please fill all fields."
                        );
                    }

                    ComplaintDAO.addComplaint(
                            citizenId,
                            subject,
                            description
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Complaint Registered Successfully!\n"
                                    + "Status: Pending",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (
                        IllegalArgumentException ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Invalid Input",
                            JOptionPane.WARNING_MESSAGE
                    );

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Error while registering complaint.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    ex.printStackTrace();
                }
            }
        });


        // ==========================================
        // VIEW MY COMPLAINTS
        // ==========================================

        viewComplaintButton.addActionListener(e -> {

            try {

                List<String> complaints =
                        ComplaintDAO
                                .getComplaintsByCitizenId(
                                        citizenId
                                );

                if (complaints.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "You have no complaints.",
                            "My Complaints",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } else {

                    StringBuilder data =
                            new StringBuilder();

                    for (String complaint :
                            complaints) {

                        data.append(complaint)
                                .append("\n\n")
                                .append(
                                        "--------------------------------"
                                )
                                .append("\n\n");
                    }

                    JTextArea textArea =
                            new JTextArea(
                                    data.toString()
                            );

                    textArea.setEditable(false);

                    textArea.setFont(
                            new Font(
                                    "Monospaced",
                                    Font.PLAIN,
                                    14
                            )
                    );

                    textArea.setRows(20);

                    textArea.setColumns(60);

                    JScrollPane scrollPane =
                            new JScrollPane(
                                    textArea
                            );

                    JOptionPane.showMessageDialog(
                            this,
                            scrollPane,
                            "My Complaints",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error while loading complaints.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // ==========================================
        // CITY SERVICES
        // ==========================================

        serviceButton.addActionListener(e -> {

            try {

                List<String> services =
                        ServiceDAO.getAllServices();

                if (services.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "No City Services Found.",
                            "City Services",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } else {

                    StringBuilder data =
                            new StringBuilder();

                    for (String service :
                            services) {

                        data.append(service)
                                .append("\n\n")
                                .append(
                                        "--------------------------------"
                                )
                                .append("\n\n");
                    }

                    JTextArea textArea =
                            new JTextArea(
                                    data.toString()
                            );

                    textArea.setEditable(false);

                    textArea.setFont(
                            new Font(
                                    "Monospaced",
                                    Font.PLAIN,
                                    14
                            )
                    );

                    textArea.setRows(20);

                    textArea.setColumns(60);

                    JScrollPane scrollPane =
                            new JScrollPane(
                                    textArea
                            );

                    JOptionPane.showMessageDialog(
                            this,
                            scrollPane,
                            "Available City Services",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error while loading city services.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // ==========================================
        // GIVE FEEDBACK
        // ==========================================

        feedbackButton.addActionListener(e -> {

            String[] ratings = {

                    "1",
                    "2",
                    "3",
                    "4",
                    "5"
            };

            String selectedRating =
                    (String) JOptionPane.showInputDialog(
                            this,
                            "Select Rating:",
                            "Give Feedback",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            ratings,
                            "5"
                    );

            if (selectedRating == null) {
                return;
            }

            JTextArea commentArea =
                    new JTextArea(
                            5,
                            25
                    );

            commentArea.setLineWrap(true);

            commentArea.setWrapStyleWord(true);

            commentArea.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            14
                    )
            );

            JScrollPane commentScrollPane =
                    new JScrollPane(
                            commentArea
                    );

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            commentScrollPane,
                            "Write Your Comment",
                            JOptionPane.OK_CANCEL_OPTION
                    );

            if (result ==
                    JOptionPane.OK_OPTION) {

                try {

                    String comment =
                            commentArea
                                    .getText()
                                    .trim();

                    if (comment.isEmpty()) {

                        throw new IllegalArgumentException(
                                "Please enter your comment."
                        );
                    }

                    int rating =
                            Integer.parseInt(
                                    selectedRating
                            );

                    FeedbackDAO.addFeedback(
                            citizenId,
                            rating,
                            comment
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Thank you! Your feedback has been submitted.",
                            "Feedback Submitted",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (
                        IllegalArgumentException ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Invalid Input",
                            JOptionPane.WARNING_MESSAGE
                    );

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Error while submitting feedback.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    ex.printStackTrace();
                }
            }
        });


        // ==========================================
        // LOGOUT
        // ==========================================

        logoutButton.addActionListener(e -> {

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to logout?",
                            "Confirm Logout",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirm ==
                    JOptionPane.YES_OPTION) {

                dispose();

                new CitizenLoginFrame()
                        .setVisible(true);
            }
        });
    }
}