package com.smartcity;

import com.smartcity.ui.CitizenLoginFrame;
import com.smartcity.ui.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(
                    "Smart City Management System"
            );

            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );
            frame.setLocationRelativeTo(null);

            // Title
            JLabel titleLabel = new JLabel(
                    "SMART CITY MANAGEMENT SYSTEM",
                    SwingConstants.CENTER
            );

            titleLabel.setFont(
                    new Font("Arial", Font.BOLD, 22)
            );

            JLabel subtitleLabel = new JLabel(
                    "Select Login Type",
                    SwingConstants.CENTER
            );

            subtitleLabel.setFont(
                    new Font("Arial", Font.PLAIN, 16)
            );

            // Buttons
            JButton adminButton =
                    new JButton("Admin Login");

            JButton citizenButton =
                    new JButton("Citizen Login");

            JButton exitButton =
                    new JButton("Exit");

            // Button Panel
            JPanel buttonPanel =
                    new JPanel(
                            new GridLayout(3, 1, 15, 15)
                    );

            buttonPanel.setBorder(
                    BorderFactory.createEmptyBorder(
                            30, 100, 30, 100
                    )
            );

            buttonPanel.add(adminButton);
            buttonPanel.add(citizenButton);
            buttonPanel.add(exitButton);

            // Top Panel
            JPanel topPanel =
                    new JPanel(
                            new GridLayout(2, 1)
                    );

            topPanel.add(titleLabel);
            topPanel.add(subtitleLabel);

            // Layout
            frame.setLayout(
                    new BorderLayout()
            );

            frame.add(
                    topPanel,
                    BorderLayout.NORTH
            );

            frame.add(
                    buttonPanel,
                    BorderLayout.CENTER
            );


            adminButton.addActionListener(e -> {

                frame.dispose();

                LoginFrame loginFrame =
                        new LoginFrame();

                loginFrame.setVisible(true);
            });


            citizenButton.addActionListener(e -> {

                frame.dispose();

                CitizenLoginFrame citizenLoginFrame =
                        new CitizenLoginFrame();

                citizenLoginFrame.setVisible(true);
            });



            exitButton.addActionListener(e -> {

                System.exit(0);

            });

            // Show window
            frame.setVisible(true);
        });
    }
}