package com.smartcity.ui;

import com.smartcity.exception.AuthenticationException;
import com.smartcity.exception.ValidationException;
import com.smartcity.model.Citizen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Citizen login screen. Auth is delegated to
 * {@link com.smartcity.service.CitizenAuthService#login(String, String)}
 * on a background thread via {@link SwingWorker}.
 */
public class CitizenLoginFrame extends JFrame {

    private final JTextField emailField = UITheme.modernTextField("you@example.com");
    private final JPasswordField passwordField = UITheme.modernPasswordField("Your password");
    private final JButton loginButton = UITheme.primaryButton("Sign In");
    private final JButton clearButton = UITheme.secondaryButton("Clear");

    public CitizenLoginFrame() {
        setTitle("Smart City - Citizen Login");
        setSize(900, 560);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 900, 560, 18, 18));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== LEFT HERO =====
        UITheme.GradientPanel left = new UITheme.GradientPanel(UITheme.NAVY_DARK, UITheme.NAVY_MID);
        left.setLayout(new BorderLayout());
        left.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel brand = new JLabel("●  SMART CITY");
        brand.setFont(UITheme.font(Font.BOLD, 16));
        brand.setForeground(UITheme.CORAL);

        JTextArea tag = new JTextArea("Citizen\nPortal");
        tag.setFont(UITheme.font(Font.BOLD, 38));
        tag.setForeground(Color.WHITE);
        tag.setOpaque(false);
        tag.setEditable(false);

        JTextArea blurb = new JTextArea(
                "Register complaints, request city services, track your requests " +
                "and share feedback to help your city serve you better.");
        blurb.setFont(UITheme.bodyFont());
        blurb.setForeground(new Color(0xCB, 0xD5, 0xE1));
        blurb.setOpaque(false);
        blurb.setEditable(false);
        blurb.setLineWrap(true);
        blurb.setWrapStyleWord(true);
        blurb.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel lc = new JPanel();
        lc.setLayout(new BoxLayout(lc, BoxLayout.Y_AXIS));
        lc.setOpaque(false);
        lc.add(brand);
        lc.add(Box.createVerticalStrut(40));
        lc.add(tag);
        lc.add(blurb);

        left.add(lc, BorderLayout.CENTER);
        JLabel footnote = new JLabel("Built for citizens \u00B7 By Exorcisms");
        footnote.setFont(UITheme.smallFont());
        footnote.setForeground(new Color(0x94, 0xA3, 0xB8));
        left.add(footnote, BorderLayout.SOUTH);

        // ===== RIGHT FORM =====
        JPanel right = new JPanel();
        right.setBackground(UITheme.CARD);
        right.setLayout(new GridBagLayout());
        right.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 4, 0);

        JButton back = new JButton("\u2190 Back");
        back.setFont(UITheme.smallFont());
        back.setForeground(UITheme.TEXT_MUTED);
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setFocusPainted(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.setHorizontalAlignment(SwingConstants.LEFT);
        back.addActionListener(e -> { dispose(); new Main().main(new String[0]); });
        right.add(back, gbc);

        gbc.gridy++; gbc.insets = new Insets(20, 0, 4, 0);
        JLabel title = new JLabel("Citizen Login");
        title.setFont(UITheme.titleFont());
        title.setForeground(UITheme.TEXT);
        right.add(title, gbc);

        gbc.gridy++; gbc.insets = new Insets(4, 0, 24, 0);
        JLabel sub = new JLabel("Sign in with your registered email and password");
        sub.setFont(UITheme.smallFont());
        sub.setForeground(UITheme.TEXT_MUTED);
        right.add(sub, gbc);

        gbc.gridy++; gbc.insets = new Insets(8, 0, 4, 0);
        JLabel el = new JLabel("EMAIL");
        el.setFont(UITheme.smallFont());
        el.setForeground(UITheme.TEXT_MUTED);
        right.add(el, gbc);

        gbc.gridy++; gbc.insets = new Insets(2, 0, 16, 0);
        emailField.setPreferredSize(new Dimension(320, 46));
        right.add(emailField, gbc);

        gbc.gridy++; gbc.insets = new Insets(4, 0, 4, 0);
        JLabel pl = new JLabel("PASSWORD");
        pl.setFont(UITheme.smallFont());
        pl.setForeground(UITheme.TEXT_MUTED);
        right.add(pl, gbc);

        gbc.gridy++; gbc.insets = new Insets(2, 0, 8, 0);
        passwordField.setPreferredSize(new Dimension(320, 46));
        right.add(passwordField, gbc);

        gbc.gridy++; gbc.insets = new Insets(8, 0, 0, 0);
        JPanel actions = new JPanel(new BorderLayout(10, 0));
        actions.setOpaque(false);
        loginButton.setPreferredSize(new Dimension(220, 46));
        clearButton.setPreferredSize(new Dimension(90, 46));
        actions.add(loginButton, BorderLayout.CENTER);
        actions.add(clearButton, BorderLayout.EAST);
        right.add(actions, gbc);

        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        };
        emailField.addKeyListener(enter);
        passwordField.addKeyListener(enter);
        loginButton.addActionListener(e -> doLogin());
        clearButton.addActionListener(e -> {
            emailField.setText("");
            passwordField.setText("");
        });

        setLayout(new BorderLayout());
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
    }

    /**
     * Runs citizen login on a background thread so BCrypt verification
     * doesn't block the EDT.
     */
    private void doLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        loginButton.setEnabled(false);
        loginButton.setText("Signing in...");

        SwingWorker<Citizen, Void> worker = new SwingWorker<>() {
            @Override protected Citizen doInBackground() {
                return GuiContext.services().citizenAuthService().login(email, password);
            }
            @Override protected void done() {
                loginButton.setEnabled(true);
                loginButton.setText("Sign In");
                try {
                    Citizen c = get();
                    JOptionPane.showMessageDialog(CitizenLoginFrame.this,
                            "Welcome, " + c.getName() + "!");
                    dispose();
                    new CitizenDashboardFrame(c.getId(), c.getName()).setVisible(true);
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    String msg = cause instanceof AuthenticationException || cause instanceof ValidationException
                            ? cause.getMessage()
                            : "Login failed. Please try again.";
                    JOptionPane.showMessageDialog(CitizenLoginFrame.this, msg,
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
