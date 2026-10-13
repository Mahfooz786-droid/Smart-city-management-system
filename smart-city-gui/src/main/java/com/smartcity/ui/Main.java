package com.smartcity.ui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.smartcity.config.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * GUI entry point. Presents a hero welcome screen with two large
 * role-selection tiles that route to the admin or citizen login screens.
 *
 * <p>Wires up FlatLaf (modern Swing LAF) and shares a single
 * {@link com.smartcity.config.DatabaseConfig} instance with the rest of
 * the GUI via the {@link GuiContext} holder.
 */
public class Main {

    public static void main(String[] args) {
        try {
            FlatIntelliJLaf.setup();
        } catch (Throwable t) {
            // FlatLaf optional — fall back to the platform LAF silently.
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignore) {}
        }

        // Initialise shared context with DB config from env vars.
        GuiContext.init(new DatabaseConfig());

        SwingUtilities.invokeLater(Main::showLauncher);
    }

    private static void showLauncher() {
        JFrame frame = new JFrame("Smart City Management System");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, 900, 600, 18, 18));

        // ===== LEFT HERO PANEL =====
        UITheme.GradientPanel left = new UITheme.GradientPanel(UITheme.NAVY_DARK, UITheme.NAVY_MID);
        left.setLayout(new BorderLayout());
        left.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel brand = new JLabel("●  SMART CITY");
        brand.setFont(UITheme.font(Font.BOLD, 16));
        brand.setForeground(UITheme.ACCENT);

        JTextArea tagline = new JTextArea("Empowering citizens.\nConnecting services.\nBuilding smarter cities.");
        tagline.setFont(UITheme.font(Font.BOLD, 30));
        tagline.setForeground(Color.WHITE);
        tagline.setOpaque(false);
        tagline.setEditable(false);
        tagline.setLineWrap(true);
        tagline.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        JTextArea subtitle = new JTextArea(
                "A centralised platform for citizens, services and city administrators. " +
                "Log in to register complaints, track city services and manage records.");
        subtitle.setFont(UITheme.bodyFont());
        subtitle.setForeground(new Color(0xCB, 0xD5, 0xE1));
        subtitle.setOpaque(false);
        subtitle.setEditable(false);
        subtitle.setLineWrap(true);
        subtitle.setWrapStyleWord(true);
        subtitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel leftCenter = new JPanel();
        leftCenter.setLayout(new BoxLayout(leftCenter, BoxLayout.Y_AXIS));
        leftCenter.setOpaque(false);
        leftCenter.add(brand);
        leftCenter.add(tagline);
        leftCenter.add(subtitle);

        JPanel miniStats = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        miniStats.setOpaque(false);
        miniStats.add(miniStat("24/7", "Online"));
        miniStats.add(miniStat("6", "City Services"));
        miniStats.add(miniStat("100%", "Citizen-First"));

        left.add(leftCenter, BorderLayout.CENTER);
        left.add(miniStats, BorderLayout.SOUTH);

        // ===== RIGHT PANEL =====
        JPanel right = new JPanel();
        right.setBackground(UITheme.CARD);
        right.setLayout(new GridBagLayout());
        right.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 6, 0);

        JLabel hello = new JLabel("Welcome back");
        hello.setFont(UITheme.font(Font.PLAIN, 14));
        hello.setForeground(UITheme.TEXT_MUTED);
        right.add(hello, gbc);

        gbc.gridy++; gbc.insets = new Insets(0, 0, 20, 0);
        JLabel choose = new JLabel("Choose your portal");
        choose.setFont(UITheme.titleFont());
        choose.setForeground(UITheme.TEXT);
        right.add(choose, gbc);

        gbc.gridy++; gbc.insets = new Insets(8, 0, 8, 0);
        JLabel chooseSub = new JLabel("Select a role to continue. Your session will be tailored accordingly.");
        chooseSub.setFont(UITheme.smallFont());
        chooseSub.setForeground(UITheme.TEXT_MUTED);
        right.add(chooseSub, gbc);

        gbc.gridy++; gbc.insets = new Insets(20, 0, 12, 0);
        JButton adminTile = roleTile("⚑", "Administrator",
                "Manage citizens, complaints & services", UITheme.ACCENT);
        adminTile.addActionListener(e -> {
            frame.dispose();
            new LoginFrame().setVisible(true);
        });
        right.add(adminTile, gbc);

        gbc.gridy++; gbc.insets = new Insets(0, 0, 12, 0);
        JButton citizenTile = roleTile("☆", "Citizen",
                "Register complaints & request city services", UITheme.CORAL);
        citizenTile.addActionListener(e -> {
            frame.dispose();
            new CitizenLoginFrame().setVisible(true);
        });
        right.add(citizenTile, gbc);

        gbc.gridy++; gbc.insets = new Insets(20, 0, 0, 0);
        JButton exitButton = new JButton("Exit application");
        exitButton.setFont(UITheme.smallFont());
        exitButton.setForeground(UITheme.TEXT_MUTED);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
        right.add(exitButton, gbc);

        frame.setLayout(new BorderLayout());
        frame.add(left, BorderLayout.WEST);
        frame.add(right, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JButton roleTile(String glyph, String title, String subtitle, Color accent) {
        JButton tile = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ButtonModel m = getModel();
                g2.setColor(m.isRollover() ? new Color(0xF8, 0xFA, 0xFC) : UITheme.CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
                g2.setColor(UITheme.BORDER);
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 2, getHeight() - 2, 14, 14));
                g2.setColor(accent);
                g2.fill(new RoundRectangle2D.Double(0, 0, 4, getHeight() - 1, 2, 2));
                int badgeSize = 44;
                int bx = 24;
                int by = (getHeight() - badgeSize) / 2;
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 30));
                g2.fillOval(bx, by, badgeSize, badgeSize);
                g2.setColor(accent);
                g2.setFont(UITheme.font(Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                int gw = fm.stringWidth(glyph);
                g2.drawString(glyph, bx + (badgeSize - gw) / 2,
                        by + (badgeSize - fm.getHeight()) / 2 + fm.getAscent());
                g2.setColor(UITheme.TEXT);
                g2.setFont(UITheme.h1Font());
                int tx = bx + badgeSize + 18;
                g2.drawString(title, tx, getHeight() / 2 - 4);
                g2.setColor(UITheme.TEXT_MUTED);
                g2.setFont(UITheme.smallFont());
                g2.drawString(subtitle, tx, getHeight() / 2 + 18);
                g2.setColor(UITheme.TEXT_MUTED);
                g2.setFont(UITheme.font(Font.BOLD, 18));
                g2.drawString("›", getWidth() - 26, getHeight() / 2 + 6);
                g2.dispose();
            }
        };
        tile.setLayout(null);
        tile.setFocusPainted(false);
        tile.setBorderPainted(false);
        tile.setContentAreaFilled(false);
        tile.setOpaque(false);
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tile.setPreferredSize(new Dimension(360, 90));
        return tile;
    }

    private static JComponent miniStat(String value, String label) {
        JPanel p = new JPanel(new BorderLayout(2, 0));
        p.setOpaque(false);
        JLabel v = new JLabel(value); v.setFont(UITheme.font(Font.BOLD, 16)); v.setForeground(Color.WHITE);
        JLabel l = new JLabel(label); l.setFont(UITheme.smallFont()); l.setForeground(new Color(0xCB, 0xD5, 0xE1));
        p.add(v, BorderLayout.WEST); p.add(l, BorderLayout.CENTER);
        return p;
    }
}
