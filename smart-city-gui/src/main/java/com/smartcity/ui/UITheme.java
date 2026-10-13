package com.smartcity.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Centralised visual identity for the Smart City Swing GUI.
 *
 * <p>Single source of truth for the colour palette, typography scale and
 * reusable component factories (gradient panels, card panels, primary /
 * secondary / danger buttons, modern text fields, sidebar nav buttons,
 * KPI tiles, styled tables).
 *
 * <p>Designed to look modern without depending on FlatLaf at the typography
 * layer — we use the system default sans-serif so the app renders cleanly
 * on any JDK out of the box. FlatLaf is wired in Main for the LAF if
 * available on the classpath.
 */
public final class UITheme {

    // ===================================================================
    // COLOUR PALETTE
    // ===================================================================
    public static final Color NAVY_DARK    = new Color(0x0F, 0x1B, 0x2D);
    public static final Color NAVY_MID     = new Color(0x18, 0x27, 0x42);
    public static final Color BG           = new Color(0xF4, 0xF6, 0xF9);
    public static final Color CARD         = new Color(0xFF, 0xFF, 0xFF);
    public static final Color ACCENT      = new Color(0x14, 0xB8, 0xA6);
    public static final Color ACCENT_DARK = new Color(0x0D, 0x94, 0x86);
    public static final Color CORAL       = new Color(0xFF, 0x6B, 0x6B);
    public static final Color AMBER       = new Color(0xF5, 0x9E, 0x0B);
    public static final Color TEXT_MUTED  = new Color(0x64, 0x74, 0x8B);
    public static final Color TEXT        = new Color(0x1E, 0x29, 0x3B);
    public static final Color BORDER      = new Color(0xE2, 0xE8, 0xF0);

    // ===================================================================
    // TYPOGRAPHY
    // ===================================================================
    private static final String FONT_FAMILY = "Segoe UI";

    public static Font font(int style, float size) { return new Font(FONT_FAMILY, style, (int) size); }
    public static Font titleFont()   { return font(Font.BOLD, 26); }
    public static Font h1Font()      { return font(Font.BOLD, 22); }
    public static Font h2Font()      { return font(Font.BOLD, 16); }
    public static Font bodyFont()    { return font(Font.PLAIN, 14); }
    public static Font smallFont()   { return font(Font.PLAIN, 12); }
    public static Font buttonFont() { return font(Font.BOLD, 14); }
    public static Font inputFont()   { return font(Font.PLAIN, 14); }

    // ===================================================================
    // CUSTOM SURFACES
    // ===================================================================

    /** A panel that paints a smooth vertical gradient between two colours. */
    public static class GradientPanel extends JPanel {
        private final Color top, bottom;
        public GradientPanel(Color top, Color bottom) { super(); setOpaque(false); this.top = top; this.bottom = bottom; }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Round-cornered icon badge used on sidebars. */
    public static class IconBadge extends JComponent {
        private final String glyph; private final Color bg, fg;
        public IconBadge(String glyph, Color bg, Color fg, int size) {
            this.glyph = glyph; this.bg = bg; this.fg = fg;
            setPreferredSize(new Dimension(size, size));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int s = Math.min(getWidth(), getHeight());
            g2.setColor(bg); g2.fillOval(0, 0, s, s);
            g2.setColor(fg); g2.setFont(font(Font.BOLD, s * 0.5f));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(glyph, (s - fm.stringWidth(glyph)) / 2, (s - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    // ===================================================================
    // COMPONENT FACTORIES
    // ===================================================================

    public static JButton primaryButton(String text) {
        JButton b = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ButtonModel m = getModel();
                g2.setColor(m.isRollover() ? ACCENT_DARK : ACCENT);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(Color.WHITE); g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        styleButton(b);
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ButtonModel m = getModel();
                g2.setColor(m.isRollover() ? new Color(0xF1, 0xF5, 0xF9) : CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(BORDER);
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.setColor(TEXT); g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        styleButton(b);
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ButtonModel m = getModel();
                g2.setColor(m.isRollover() ? new Color(0xE5, 0x4F, 0x4F) : CORAL);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(Color.WHITE); g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        styleButton(b);
        return b;
    }

    private static void styleButton(JButton b) {
        b.setFont(buttonFont());
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setBorder(new EmptyBorder(0, 0, 0, 0));
        b.setPreferredSize(new Dimension(220, 46));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static JTextField modernTextField(String placeholder) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.setColor(BORDER);
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 2, getHeight() - 2, 12, 12));
                g2.dispose();
                super.paintComponent(g);
                if (getText().isEmpty() && getToolTipText() != null) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g3.setColor(TEXT_MUTED); g3.setFont(getFont());
                    g3.drawString(getToolTipText(), 14, getHeight() / 2 + getFont().getSize() / 3);
                    g3.dispose();
                }
            }
        };
        f.setFont(inputFont());
        f.setToolTipText(placeholder);
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(8, 14, 8, 14));
        f.setPreferredSize(new Dimension(280, 44));
        return f;
    }

    public static JPasswordField modernPasswordField(String placeholder) {
        JPasswordField f = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.setColor(BORDER);
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 2, getHeight() - 2, 12, 12));
                g2.dispose();
                super.paintComponent(g);
                if (getPassword().length == 0 && placeholder != null) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g3.setColor(TEXT_MUTED); g3.setFont(getFont());
                    g3.drawString(placeholder, 14, getHeight() / 2 + getFont().getSize() / 3);
                    g3.dispose();
                }
            }
        };
        f.setFont(inputFont());
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(8, 14, 8, 14));
        f.setPreferredSize(new Dimension(280, 44));
        return f;
    }

    public static JButton sidebarButton(String text, String glyph) {
        // Custom-painted sidebar nav button with active-state indicator.
        JButton b = new JButton() {
            private boolean active;
            public void setActive(boolean a) { this.active = a; repaint(); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ButtonModel m = getModel();
                if (active) {
                    g2.setColor(new Color(0xFF, 0xFF, 0xFF, 26));
                    g2.fill(new RoundRectangle2D.Double(8, 4, getWidth() - 16, getHeight() - 8, 10, 10));
                    g2.setColor(ACCENT);
                    g2.fill(new RoundRectangle2D.Double(8, 4, 4, getHeight() - 8, 2, 2));
                } else if (m.isRollover()) {
                    g2.setColor(new Color(0xFF, 0xFF, 0xFF, 14));
                    g2.fill(new RoundRectangle2D.Double(8, 4, getWidth() - 16, getHeight() - 8, 10, 10));
                }
                g2.setColor(Color.WHITE);
                g2.setFont(font(Font.PLAIN, 16));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(glyph, 28, getHeight() / 2 + fm.getAscent() / 2);
                g2.setFont(getFont());
                fm = g2.getFontMetrics();
                g2.drawString(text, 28 + 22, getHeight() / 2 + fm.getAscent() / 2);
                g2.dispose();
            }
        };
        b.setFont(h2Font());
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(220, 48));
        return b;
    }

    public static JPanel statTile(String label, String value, Color accent) {
        JPanel p = new JPanel(new BorderLayout(4, 2)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
                g2.setColor(BORDER);
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 2, getHeight() - 2, 14, 14));
                g2.setColor(accent);
                g2.fill(new RoundRectangle2D.Double(0, 0, 4, getHeight() - 1, 2, 2));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(14, 18, 14, 18));
        JLabel l = new JLabel(label); l.setFont(smallFont()); l.setForeground(TEXT_MUTED);
        JLabel v = new JLabel(value); v.setFont(font(Font.BOLD, 22)); v.setForeground(TEXT);
        p.add(l, BorderLayout.NORTH); p.add(v, BorderLayout.CENTER);
        return p;
    }

    public static JScrollPane styledTable(JTable table) {
        table.setRowHeight(36);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(0xE6, 0xFA, 0xF7));
        table.setSelectionForeground(TEXT);
        table.setFont(bodyFont());
        table.setForeground(TEXT);
        table.setBackground(CARD);
        table.getTableHeader().setFont(h2Font());
        table.getTableHeader().setBackground(new Color(0xF1, 0xF5, 0xF9));
        table.getTableHeader().setForeground(TEXT);
        table.setIntercellSpacing(new Dimension(0, 0));
        JScrollPane sp = new JScrollPane(table);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(new EmptyBorder(0, 0, 0, 0));
        return sp;
    }

    public static JPanel formPanel(Object[] fields) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;
        for (Object o : fields) {
            if (o instanceof String s) {
                JLabel l = new JLabel(s); l.setFont(h2Font()); l.setForeground(TEXT);
                gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
                p.add(l, gbc);
            } else if (o instanceof JComponent c) {
                gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1;
                p.add(c, gbc);
                row++;
            }
        }
        return p;
    }

    private UITheme() {}
}
