package com.smartcity.ui;

import com.smartcity.database.CitizenService;
import com.smartcity.database.ComplaintDAO;
import com.smartcity.database.FeedbackDAO;
import com.smartcity.model.Citizen;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private CitizenService citizenService;

    public DashboardFrame() {

        citizenService = new CitizenService();

        // ==========================================
        // UI STYLING
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
        // FRAME SETTINGS
        // ==========================================

        setTitle(
                "Smart City Management System - Admin Dashboard"
        );

        setSize(700, 700);

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

        JLabel titleLabel = new JLabel(
                "SMART CITY MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 10, 20, 10
                )
        );

        // ==========================================
        // BUTTONS
        // ==========================================

        JButton addButton =
                new JButton("Add Citizen");

        JButton viewButton =
                new JButton("View Citizens");

        JButton updateButton =
                new JButton("Update Citizen");

        JButton deleteButton =
                new JButton("Delete Citizen");

        JButton viewComplaintsButton =
                new JButton("View All Complaints");

        JButton updateComplaintButton =
                new JButton("Update Complaint Status");

        JButton viewFeedbackButton =
                new JButton("View All Feedback");

        JButton exitButton =
                new JButton("Exit");

        // ==========================================
        // BUTTON PANEL
        // ==========================================

        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(8, 1, 12, 12)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 100, 30, 100
                )
        );

        panel.setBackground(
                new Color(240, 244, 248)
        );

        panel.add(addButton);
        panel.add(viewButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(viewComplaintsButton);
        panel.add(updateComplaintButton);
        panel.add(viewFeedbackButton);
        panel.add(exitButton);

        // ==========================================
        // BUTTON STYLING
        // ==========================================

        JButton[] buttons = {

                addButton,
                viewButton,
                updateButton,
                deleteButton,
                viewComplaintsButton,
                updateComplaintButton,
                viewFeedbackButton,
                exitButton
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
                    new Dimension(350, 45)
            );
        }

        // ==========================================
        // FRAME LAYOUT
        // ==========================================

        setLayout(
                new BorderLayout()
        );

        add(
                titleLabel,
                BorderLayout.NORTH
        );

        add(
                panel,
                BorderLayout.CENTER
        );


        // ==========================================
        // ADD CITIZEN
        // ==========================================

        addButton.addActionListener(e -> {

            JTextField nameField =
                    new JTextField();

            JTextField emailField =
                    new JTextField();

            JTextField phoneField =
                    new JTextField();

            JPasswordField passwordField =
                    new JPasswordField();

            JTextField addressField =
                    new JTextField();

            Object[] fields = {

                    "Name:",
                    nameField,

                    "Email:",
                    emailField,

                    "Phone:",
                    phoneField,

                    "Password:",
                    passwordField,

                    "Address:",
                    addressField
            };

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            fields,
                            "Add Citizen",
                            JOptionPane.OK_CANCEL_OPTION
                    );

            if (result == JOptionPane.OK_OPTION) {

                try {

                    String name =
                            nameField.getText().trim();

                    String email =
                            emailField.getText().trim();

                    String phone =
                            phoneField.getText().trim();

                    String password =
                            new String(
                                    passwordField.getPassword()
                            );

                    String address =
                            addressField.getText().trim();


                    // Input validation

                    if (name.isEmpty()
                            || email.isEmpty()
                            || phone.isEmpty()
                            || password.isEmpty()
                            || address.isEmpty()) {

                        throw new IllegalArgumentException(
                                "All fields are required."
                        );
                    }

                    if (!email.contains("@")) {

                        throw new IllegalArgumentException(
                                "Please enter a valid email."
                        );
                    }

                    if (!phone.matches("\\d{10}")) {

                        throw new IllegalArgumentException(
                                "Phone number must contain exactly 10 digits."
                        );
                    }


                    citizenService.addCitizen(
                            name,
                            email,
                            phone,
                            password,
                            address
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Citizen Added Successfully!"
                    );

                } catch (IllegalArgumentException ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Invalid Input",
                            JOptionPane.WARNING_MESSAGE
                    );

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Error while adding citizen.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                    ex.printStackTrace();
                }
            }
        });


        // ==========================================
        // VIEW CITIZENS
        // COLLECTIONS + GENERICS
        // ==========================================

        viewButton.addActionListener(e -> {

            try {

                List<Citizen> citizens =
                        citizenService.getAllCitizens();

                if (citizens.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "No Citizens Found."
                    );

                } else {

                    StringBuilder data =
                            new StringBuilder();

                    for (Citizen citizen : citizens) {

                        data.append(
                                        "Citizen ID: "
                                )
                                .append(
                                        citizen.getCitizenId()
                                )

                                .append("\nName: ")
                                .append(
                                        citizen.getName()
                                )

                                .append("\nEmail: ")
                                .append(
                                        citizen.getEmail()
                                )

                                .append("\nPhone: ")
                                .append(
                                        citizen.getPhone()
                                )

                                .append("\nAddress: ")
                                .append(
                                        citizen.getAddress()
                                )

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
                            "Citizen List",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error while loading citizens.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // ==========================================
        // UPDATE CITIZEN
        // ==========================================

        updateButton.addActionListener(e -> {

            String idInput =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Citizen ID:"
                    );

            if (idInput == null) {
                return;
            }

            try {

                int citizenId =
                        Integer.parseInt(idInput);

                if (citizenId <= 0) {

                    throw new IllegalArgumentException(
                            "Citizen ID must be greater than 0."
                    );
                }

                JTextField nameField =
                        new JTextField();

                JTextField emailField =
                        new JTextField();

                JTextField phoneField =
                        new JTextField();

                JTextField addressField =
                        new JTextField();

                Object[] fields = {

                        "New Name:",
                        nameField,

                        "New Email:",
                        emailField,

                        "New Phone:",
                        phoneField,

                        "New Address:",
                        addressField
                };

                int result =
                        JOptionPane.showConfirmDialog(
                                this,
                                fields,
                                "Update Citizen",
                                JOptionPane.OK_CANCEL_OPTION
                        );

                if (result ==
                        JOptionPane.OK_OPTION) {

                    String name =
                            nameField.getText().trim();

                    String email =
                            emailField.getText().trim();

                    String phone =
                            phoneField.getText().trim();

                    String address =
                            addressField.getText().trim();


                    if (name.isEmpty()
                            || email.isEmpty()
                            || phone.isEmpty()
                            || address.isEmpty()) {

                        throw new IllegalArgumentException(
                                "All fields are required."
                        );
                    }

                    if (!email.contains("@")) {

                        throw new IllegalArgumentException(
                                "Please enter a valid email."
                        );
                    }

                    if (!phone.matches("\\d{10}")) {

                        throw new IllegalArgumentException(
                                "Phone number must contain exactly 10 digits."
                        );
                    }


                    citizenService.updateCitizen(
                            citizenId,
                            name,
                            email,
                            phone,
                            address
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Citizen Updated Successfully!"
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid numeric Citizen ID.",
                        "Invalid ID",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error while updating citizen.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // ==========================================
        // DELETE CITIZEN
        // ==========================================

        deleteButton.addActionListener(e -> {

            String idInput =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Citizen ID to Delete:"
                    );

            if (idInput == null) {
                return;
            }

            try {

                int citizenId =
                        Integer.parseInt(idInput);

                if (citizenId <= 0) {

                    throw new IllegalArgumentException(
                            "Citizen ID must be greater than 0."
                    );
                }

                int confirm =
                        JOptionPane.showConfirmDialog(
                                this,
                                "Are you sure you want to delete Citizen ID "
                                        + citizenId + "?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );

                if (confirm ==
                        JOptionPane.YES_OPTION) {

                    citizenService.deleteCitizen(
                            citizenId
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Citizen Deleted Successfully!"
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid numeric Citizen ID.",
                        "Invalid ID",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error while deleting citizen.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // ==========================================
        // VIEW ALL COMPLAINTS
        // ==========================================

        viewComplaintsButton.addActionListener(e -> {

            try {

                List<String> complaints =
                        ComplaintDAO.getAllComplaints();

                if (complaints.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "No Complaints Found."
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
                            "All Complaints",
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
        // UPDATE COMPLAINT STATUS
        // ==========================================

        updateComplaintButton.addActionListener(e -> {

            String idInput =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Complaint ID:"
                    );

            if (idInput == null) {
                return;
            }

            try {

                int complaintId =
                        Integer.parseInt(idInput);

                if (complaintId <= 0) {

                    throw new IllegalArgumentException(
                            "Complaint ID must be greater than 0."
                    );
                }

                String[] statuses = {

                        "Pending",
                        "In Progress",
                        "Resolved"
                };

                String selectedStatus =
                        (String) JOptionPane.showInputDialog(
                                this,
                                "Select Complaint Status:",
                                "Update Status",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                statuses,
                                statuses[0]
                        );

                if (selectedStatus != null) {

                    ComplaintDAO.updateComplaintStatus(
                            complaintId,
                            selectedStatus
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Complaint Status Updated Successfully!"
                    );
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid numeric Complaint ID.",
                        "Invalid ID",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error while updating complaint.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // ==========================================
        // VIEW ALL FEEDBACK
        // ==========================================

        viewFeedbackButton.addActionListener(e -> {

            try {

                List<String> feedbackList =
                        FeedbackDAO.getAllFeedback();

                if (feedbackList.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "No Feedback Found."
                    );

                } else {

                    StringBuilder data =
                            new StringBuilder();

                    for (String feedback :
                            feedbackList) {

                        data.append(feedback)
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
                            "All Feedback",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error while loading feedback.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });


        // ==========================================
        // EXIT
        // ==========================================

        exitButton.addActionListener(e -> {

            System.exit(0);

        });
    }
}