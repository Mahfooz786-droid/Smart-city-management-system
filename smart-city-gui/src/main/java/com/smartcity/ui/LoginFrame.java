package com.smartcity.ui;

import com.smartcity.exception.AuthenticationException;
import com.smartcity.exception.ValidationException;
import com.smartcity.model.Administrator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Admin login screen. Auth is delegated to {@link com.smartcity.service.AdminAuthService}
 * on a background thread via {@link SwingWorker} so the EDT never blocks.
 */
public class LoginFrame extends JFrame {

    private final JTextField usernameField = UITheme.modernTextField("admin");
    private final JPasswordField passwordField = UITheme.modernPasswordField("admin123");
    private final JButton loginButton = UITheme.primaryButton("Sign In");

    public LoginFrame() {
        setTitle("Smart City - Administrator Login");
        setSize(900, 560);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 900, 560, 18, 18));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== LEFT HERO =====
        UITheme.GradientPanel left = new UITheme.GradientPanel(UITheme.NAVY_DARK, UITheme.NAVY_MID);
        left.setLayout(new BorderLayout());
        left.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel brand = new JLabel("●  SMART CITY");
        brand.setFont(UITheme.font(Font.BOLD, 16));
        brand.setForeground(UITheme.ACCENT);

        JTextArea tag = new JTextArea("Admin\nConsole");
        tag.setFont(UITheme.font(Font.BOLD, 38));
        tag.setForeground(Color.WHITE);
        tag.setOpaque(false);
        tag.setEditable(false);
        tag.setLineWrap(true);

        JTextArea blurb = new JTextArea(
                "Manage citizen records, monitor complaints, update service " +
                "status and review citizen feedback - all from one place.");
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

        JLabel footnote = new JLabel("Secure \u00B7 BCrypt-hashed \u00B7 Trusted by City Administrators");
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
        back.addActionListener(e -> {
            dispose();
            new Main().main(new String[0]); // re-launch
            // Note: calling main() starts a new launcher; we keep the original
            // process. For a cleaner restart pattern one would store the
            // previous frame, but for an educational GUI this is sufficient.
        });
        right.add(back, gbc);

        gbc.gridy++; gbc.insets = new Insets(20, 0, 4, 0);
        JLabel title = new JLabel("Administrator Login");
        title.setFont(UITheme.titleFont());
        title.setForeground(UITheme.TEXT);
        right.add(title, gbc);

        gbc.gridy++; gbc.insets = new Insets(4, 0, 24, 0);
        JLabel sub = new JLabel("Enter your credentials to access the admin dashboard");
        sub.setFont(UITheme.smallFont());
        sub.setForeground(UITheme.TEXT_MUTED);
        right.add(sub, gbc);

        gbc.gridy++; gbc.insets = new Insets(8, 0, 4, 0);
        JLabel ul = new JLabel("USERNAME");
        ul.setFont(UITheme.smallFont());
        ul.setForeground(UITheme.TEXT_MUTED);
        right.add(ul, gbc);

        gbc.gridy++; gbc.insets = new Insets(2, 0, 16, 0);
        usernameField.setPreferredSize(new Dimension(320, 46));
        right.add(usernameField, gbc);

        gbc.gridy++; gbc.insets = new Insets(4, 0, 4, 0);
        JLabel pl = new JLabel("PASSWORD");
        pl.setFont(UITheme.smallFont());
        pl.setForeground(UITheme.TEXT_MUTED);
        right.add(pl, gbc);

        gbc.gridy++; gbc.insets = new Insets(2, 0, 8, 0);
        passwordField.setPreferredSize(new Dimension(320, 46));
        right.add(passwordField, gbc);

        gbc.gridy++; gbc.insets = new Insets(0, 0, 24, 0);
        JLabel hint = new JLabel("Hint: admin / admin123");
        hint.setFont(UITheme.smallFont());
        hint.setForeground(UITheme.TEXT_MUTED);
        right.add(hint, gbc);

        gbc.gridy++; gbc.insets = new Insets(4, 0, 0, 0);
        loginButton.setPreferredSize(new Dimension(320, 46));
        right.add(loginButton, gbc);

        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        };
        usernameField.addKeyListener(enter);
        passwordField.addKeyListener(enter);
        loginButton.addActionListener(e -> doLogin());

        setLayout(new BorderLayout());
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
    }

    /**
     * Runs the admin login on a background thread so the EDT stays
     * responsive (BCrypt verification is intentionally slow).
     */
    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        loginButton.setEnabled(false);
        loginButton.setText("Signing in...");

        SwingWorker<Administrator, Void> worker = new SwingWorker<>() {
            @Override protected Administrator doInBackground() {
                return GuiContext.adminAuth().login(username, password);
            }
            @Override protected void done() {
                loginButton.setEnabled(true);
                loginButton.setText("Sign In");
                try {
                    Administrator admin = get();
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Welcome, " + admin.getName() + "!");
                    dispose();
                    new DashboardFrame().setVisible(true);
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    String msg = cause instanceof AuthenticationException || cause instanceof ValidationException
                            ? cause.getMessage()
                            : "Login failed. Please try again.";
                    JOptionPane.showMessageDialog(LoginFrame.this, msg,
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
