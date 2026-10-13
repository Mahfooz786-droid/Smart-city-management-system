package com.smartcity.ui;

import com.smartcity.exception.SmartCityException;
import com.smartcity.model.CityService;
import com.smartcity.model.Complaint;
import com.smartcity.model.Feedback;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Citizen dashboard. Sidebar navigation switches between three views:
 * My Complaints, City Services, Give Feedback. DB ops run on
 * {@link SwingWorker} so the EDT stays responsive.
 */
public class CitizenDashboardFrame extends JFrame {

    private final int citizenId;
    private final String citizenName;

    private final JButton navComplaints = UITheme.sidebarButton("My Complaints", "\u26A1");
    private final JButton navServices   = UITheme.sidebarButton("City Services", "\u2605");
    private final JButton navFeedback   = UITheme.sidebarButton("Give Feedback", "\u2706");
    private final JButton navLogout      = UITheme.sidebarButton("Logout", "\u2192");

    private final JLabel kpiComplaints = new JLabel("--");
    private final JLabel kpiServices    = new JLabel("--");

    private final JPanel viewHost = new JPanel(new BorderLayout());

    public CitizenDashboardFrame(int citizenId, String citizenName) {
        this.citizenId = citizenId;
        this.citizenName = citizenName;

        setTitle("Smart City - Citizen Dashboard");
        setSize(1100, 720);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 1100, 720, 18, 18));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        add(buildMain(), BorderLayout.CENTER);

        navComplaints.addActionListener(e -> showMyComplaintsView());
        navServices.addActionListener(e -> showServicesView());
        navFeedback.addActionListener(e -> showFeedbackView());
        navLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new CitizenLoginFrame().setVisible(true);
            }
        });

        showMyComplaintsView();
    }

    // ===================================================================
    // SIDEBAR (mirrors DashboardFrame but with coral accent)
    // ===================================================================
    private JComponent buildSidebar() {
        UITheme.GradientPanel sidebar = new UITheme.GradientPanel(UITheme.NAVY_DARK, UITheme.NAVY_MID);
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(240, 720));

        JPanel header = new JPanel(new BorderLayout(8, 4));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(28, 24, 24, 24));
        JLabel logo = new JLabel("\u25CF"); logo.setFont(UITheme.font(Font.BOLD, 22)); logo.setForeground(UITheme.CORAL);
        JLabel brand = new JLabel("SMART CITY"); brand.setFont(UITheme.font(Font.BOLD, 14)); brand.setForeground(Color.WHITE);
        header.add(logo, BorderLayout.WEST); header.add(brand, BorderLayout.CENTER);
        sidebar.add(header, BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setOpaque(false);
        nav.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel sectionLabel = new JLabel("CITIZEN");
        sectionLabel.setFont(UITheme.smallFont());
        sectionLabel.setForeground(new Color(0x94, 0xA3, 0xB8));
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 0));
        nav.add(sectionLabel);
        nav.add(navComplaints);
        nav.add(navServices);
        nav.add(navFeedback);
        nav.add(Box.createVerticalStrut(20));
        nav.add(navLogout);
        sidebar.add(nav, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        UITheme.IconBadge badge = new UITheme.IconBadge(
                String.valueOf(citizenName.charAt(0)).toUpperCase(),
                UITheme.CORAL, UITheme.NAVY_DARK, 36);
        JPanel uinfo = new JPanel(); uinfo.setLayout(new BoxLayout(uinfo, BoxLayout.Y_AXIS)); uinfo.setOpaque(false);
        JLabel uname = new JLabel(citizenName); uname.setFont(UITheme.h2Font()); uname.setForeground(Color.WHITE);
        JLabel urole = new JLabel("Citizen #" + citizenId); urole.setFont(UITheme.smallFont()); urole.setForeground(new Color(0x94, 0xA3, 0xB8));
        uinfo.add(uname); uinfo.add(urole);
        footer.add(badge); footer.add(uinfo);
        sidebar.add(footer, BorderLayout.SOUTH);
        return sidebar;
    }

    // ===================================================================
    // MAIN
    // ===================================================================
    private JComponent buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BG);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(24, 28, 4, 28));
        JLabel title = new JLabel("Citizen Dashboard"); title.setFont(UITheme.titleFont()); title.setForeground(UITheme.TEXT);
        JLabel subtitle = new JLabel("Welcome, " + citizenName + " - here are your city tools");
        subtitle.setFont(UITheme.smallFont()); subtitle.setForeground(UITheme.TEXT_MUTED);
        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 2)); titles.setOpaque(false);
        titles.add(title); titles.add(subtitle);
        topBar.add(titles, BorderLayout.CENTER);
        main.add(topBar, BorderLayout.NORTH);

        JPanel contentHost = new JPanel(new BorderLayout());
        contentHost.setOpaque(false);
        contentHost.setBorder(BorderFactory.createEmptyBorder(0, 28, 24, 28));
        JPanel kpi = new JPanel(new GridLayout(1, 2, 12, 0)); kpi.setOpaque(false);
        kpi.setBorder(BorderFactory.createEmptyBorder(4, 0, 16, 0));
        kpi.add(wrapKpi("MY COMPLAINTS", kpiComplaints, UITheme.AMBER));
        kpi.add(wrapKpi("CITY SERVICES", kpiServices, UITheme.ACCENT));
        JPanel contentWithKpi = new JPanel(new BorderLayout()); contentWithKpi.setOpaque(false);
        contentWithKpi.add(kpi, BorderLayout.NORTH);
        viewHost.setOpaque(false);
        contentWithKpi.add(viewHost, BorderLayout.CENTER);
        contentHost.add(contentWithKpi, BorderLayout.CENTER);
        main.add(contentHost, BorderLayout.CENTER);
        return main;
    }

    private JPanel wrapKpi(String label, JLabel valueLabel, Color accent) {
        JPanel tile = UITheme.statTile(label, "--", accent);
        Component[] comps = tile.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JLabel l && l.getFont().isBold()) {
                tile.remove(i);
                tile.add(valueLabel, i);
                break;
            }
        }
        return tile;
    }

    // ===================================================================
    // VIEW: MY COMPLAINTS
    // ===================================================================
    private void showMyComplaintsView() {
        setActiveNav(navComplaints);
        JPanel view = new JPanel(new BorderLayout(0, 16)); view.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); actions.setOpaque(false);
        JButton register = UITheme.primaryButton("+ Register Complaint");
        JButton refresh = UITheme.secondaryButton("Refresh");
        actions.add(register); actions.add(refresh);
        view.add(actions, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{}, new Object[]{"ID", "Subject", "Description", "Status", "Filed"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model); table.setRowHeight(48);
        view.add(UITheme.styledTable(table), BorderLayout.CENTER);

        register.addActionListener(e -> showRegisterComplaintDialog(model));
        refresh.addActionListener(e -> loadMyComplaints(model));

        viewHost.removeAll();
        viewHost.add(view, BorderLayout.CENTER);
        viewHost.revalidate();
        viewHost.repaint();
        loadMyComplaints(model);
    }

    private void loadMyComplaints(DefaultTableModel model) {
        model.setRowCount(0);
        SwingWorker<List<Complaint>, Void> w = new SwingWorker<>() {
            @Override protected List<Complaint> doInBackground() {
                return GuiContext.services().complaintService().getComplaintsForCitizen(citizenId);
            }
            @Override protected void done() {
                try {
                    List<Complaint> list = get();
                    for (Complaint c : list) {
                        model.addRow(new Object[]{c.getId(), c.getSubject(), c.getDescription(),
                                c.getStatus().dbValue(),
                                c.getCreatedAt() == null ? "" : c.getCreatedAt().toLocalDate().toString()});
                    }
                    kpiComplaints.setText(String.valueOf(list.size()));
                } catch (Exception ex) { showDbError(ex, "complaints"); }
            }
        };
        w.execute();
    }

    private void showRegisterComplaintDialog(DefaultTableModel model) {
        JTextField subject = UITheme.modernTextField("Brief subject");
        JTextArea description = new JTextArea(5, 25);
        description.setLineWrap(true); description.setWrapStyleWord(true);
        description.setFont(UITheme.inputFont());
        description.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        JScrollPane sc = new JScrollPane(description); sc.setPreferredSize(new Dimension(320, 120));
        Object[] fields = {"Subject:", subject, "Description:", sc};
        int result = JOptionPane.showConfirmDialog(this, UITheme.formPanel(fields),
                "Register Complaint", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            SwingWorker<Void, Void> w = new SwingWorker<>() {
                @Override protected Void doInBackground() {
                    GuiContext.services().complaintService().registerComplaint(
                            citizenId, subject.getText(), description.getText());
                    return null;
                }
                @Override protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(CitizenDashboardFrame.this,
                                "Complaint Registered Successfully! Status: Pending");
                        loadMyComplaints(model);
                    } catch (Exception ex) { showDbError(ex, "register complaint"); }
                }
            };
            w.execute();
        }
    }

    // ===================================================================
    // VIEW: CITY SERVICES
    // ===================================================================
    private void showServicesView() {
        setActiveNav(navServices);
        JPanel view = new JPanel(new BorderLayout(0, 16)); view.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); actions.setOpaque(false);
        JButton refresh = UITheme.secondaryButton("Refresh");
        actions.add(refresh);
        view.add(actions, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{}, new Object[]{"Service", "Department", "Description", "Status"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model); table.setRowHeight(48);
        view.add(UITheme.styledTable(table), BorderLayout.CENTER);

        refresh.addActionListener(e -> loadServices(model));

        viewHost.removeAll();
        viewHost.add(view, BorderLayout.CENTER);
        viewHost.revalidate();
        viewHost.repaint();
        loadServices(model);
    }

    private void loadServices(DefaultTableModel model) {
        model.setRowCount(0);
        SwingWorker<List<CityService>, Void> w = new SwingWorker<>() {
            @Override protected List<CityService> doInBackground() {
                return GuiContext.services().cityServiceService().getAllServices();
            }
            @Override protected void done() {
                try {
                    List<CityService> list = get();
                    for (CityService s : list) {
                        model.addRow(new Object[]{s.getName(), s.getDepartment(),
                                s.getDescription(), s.getStatus().dbValue()});
                    }
                    kpiServices.setText(String.valueOf(list.size()));
                } catch (Exception ex) { showDbError(ex, "services"); }
            }
        };
        w.execute();
    }

    // ===================================================================
    // VIEW: GIVE FEEDBACK
    // ===================================================================
    private void showFeedbackView() {
        setActiveNav(navFeedback);
        JPanel view = new JPanel(new BorderLayout(0, 16)); view.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); actions.setOpaque(false);
        JButton submit = UITheme.primaryButton("Submit Feedback");
        actions.add(submit);
        view.add(actions, BorderLayout.NORTH);

        JPanel infoCard = new JPanel(new BorderLayout(20, 0));
        infoCard.setOpaque(false);
        JLabel heading = new JLabel("We value your voice"); heading.setFont(UITheme.h1Font()); heading.setForeground(UITheme.TEXT);
        JLabel body = new JLabel("<html><p style='font-size:12px;color:#64748B'>" +
                "Rate your experience with city services and share your comments. " +
                "Your feedback helps administrators prioritise improvements.</p></html>");
        infoCard.add(heading, BorderLayout.NORTH); infoCard.add(body, BorderLayout.CENTER);
        view.add(infoCard, BorderLayout.CENTER);

        submit.addActionListener(e -> showFeedbackDialog());

        viewHost.removeAll();
        viewHost.add(view, BorderLayout.CENTER);
        viewHost.revalidate();
        viewHost.repaint();
    }

    private void showFeedbackDialog() {
        String[] ratings = {"1 - Poor", "2 - Fair", "3 - Good", "4 - Very Good", "5 - Excellent"};
        String selected = (String) JOptionPane.showInputDialog(this, "Select Rating:",
                "Give Feedback", JOptionPane.QUESTION_MESSAGE, null, ratings, ratings[4]);
        if (selected == null) return;
        JTextArea commentArea = new JTextArea(5, 25);
        commentArea.setLineWrap(true); commentArea.setWrapStyleWord(true);
        commentArea.setFont(UITheme.inputFont());
        commentArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        JScrollPane sp = new JScrollPane(commentArea); sp.setPreferredSize(new Dimension(320, 120));
        int result = JOptionPane.showConfirmDialog(this, sp, "Write Your Comment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int rating = Integer.parseInt(selected.split(" - ")[0]);
            SwingWorker<Void, Void> w = new SwingWorker<>() {
                @Override protected Void doInBackground() {
                    GuiContext.services().feedbackService().submitFeedback(
                            citizenId, rating, commentArea.getText());
                    return null;
                }
                @Override protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(CitizenDashboardFrame.this,
                                "Thank you! Your feedback has been submitted.");
                    } catch (Exception ex) { showDbError(ex, "submit feedback"); }
                }
            };
            w.execute();
        }
    }

    // ===================================================================
    // HELPERS
    // ===================================================================
    private void setActiveNav(JButton active) {
        for (JButton b : new JButton[]{navComplaints, navServices, navFeedback, navLogout}) {
            try {
                b.getClass().getMethod("setActive", boolean.class)
                 .invoke(b, b == active);
            } catch (Exception ignore) {}
        }
    }

    private void showDbError(Exception ex, String op) {
        Throwable root = ex.getCause() != null ? ex.getCause() : ex;
        String msg = root instanceof SmartCityException ? root.getMessage() : "Error during " + op + ".";
        JOptionPane.showMessageDialog(this, msg, "Operation Failed", JOptionPane.ERROR_MESSAGE);
        root.printStackTrace();
    }
}
