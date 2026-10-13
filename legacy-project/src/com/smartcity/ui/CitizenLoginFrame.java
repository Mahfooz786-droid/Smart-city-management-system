package com.smartcity.ui;

import com.smartcity.database.CitizenLoginService;
import com.smartcity.model.Citizen;

import javax.swing.*;
import java.awt.*;

public class CitizenLoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public CitizenLoginFrame() {

        setTitle("Smart City - Citizen Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel(
                "CITIZEN LOGIN",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        JLabel emailLabel =
                new JLabel("Email:");

        JLabel passwordLabel =
                new JLabel("Password:");

        emailField =
                new JTextField();

        passwordField =
                new JPasswordField();

        JButton loginButton =
                new JButton("Login");

        JButton clearButton =
                new JButton("Clear");

        JPanel formPanel =
                new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 40, 20, 40
                )
        );

        formPanel.add(emailLabel);
        formPanel.add(emailField);

        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        formPanel.add(loginButton);
        formPanel.add(clearButton);

        setLayout(new BorderLayout());

        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        // LOGIN
        loginButton.addActionListener(e -> {

            String email =
                    emailField.getText().trim();

            String password =
                    new String(
                            passwordField.getPassword()
                    );

            if (email.isEmpty() ||
                    password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter email and password."
                );

                return;
            }

            Citizen citizen =
                    CitizenLoginService.loginCitizen(
                            email,
                            password
                    );

            if (citizen != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful!\nWelcome, "
                                + citizen.getName()
                );

                new CitizenDashboardFrame(
                        citizen.getCitizenId(),
                        citizen.getName()
                ).setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // CLEAR
        clearButton.addActionListener(e -> {

            emailField.setText("");
            passwordField.setText("");

        });
    }
}