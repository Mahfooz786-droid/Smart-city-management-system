package com.smartcity.ui;

import com.smartcity.exception.SmartCityException;
import com.smartcity.model.Citizen;
import com.smartcity.model.CityService;
import com.smartcity.model.Complaint;
import com.smartcity.model.Feedback;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Admin dashboard. Sidebar navigation switches between three views:
 * citizens, complaints, feedback. Every DB call runs on a
 * {@link SwingWorker} so the EDT stays responsive.
 *
 * <p>Routes:
 * <ul>
 *   <li>Citizens  → CitizenService.getAllCitizens()</li>
 *   <li>Complaints → ComplaintService.getAllComplaints()</li>
 *   <li>Feedback  → FeedbackService.getAllFeedback()</li>
 * </ul>
 */
public class DashboardFrame extends JFrame {

    private final JButton navCitizens   = UITheme.sidebarButton("Citizens", "\u25D4");
    private final JButton navComplaints = UITheme.sidebarButton("Complaints", "\u26A1");
    private final JButton navFeedback   = UITheme.sidebarButton("Feedback", "\u2605");
    private final JButton navExit       = UITheme.sidebarButton("Logout", "\u23FB");

    private final JLabel kpiCitizens   = new JLabel("--");
    private final JLabel kpiComplaints = new JLabel("--");
    private final JLabel kpiFeedback   = new JLabel("--");

    private final JPanel viewHost = new JPanel(new BorderLayout());

    public DashboardFrame() {
        setTitle("Smart City - Admin Dashboard");
        setSize(1100, 720);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 1100, 720, 18, 18));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        add(buildMain(), BorderLayout.CENTER);

        navCitizens.addActionListener(e -> showCitizensView());
        navComplaints.addActionListener(e -> showComplaintsView());
        navFeedback.addActionListener(e -> showFeedbackView());
        navExit.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        showCitizensView();
    }

    // ===================================================================
    // SIDEBAR
    // ===================================================================
    private JComponent buildSidebar() {
        UITheme.GradientPanel sidebar = new UITheme.GradientPanel(UITheme.NAVY_DARK, UITheme.NAVY_MID);
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(240, 720));

        JPanel header = new JPanel(new BorderLayout(8, 4));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(28, 24, 24, 24));
        JLabel logo = new JLabel("\u25CF"); logo.setFont(UITheme.font(Font.BOLD, 22)); logo.setForeground(UITheme.ACCENT);
        JLabel brand = new JLabel("SMART CITY"); brand.setFont(UITheme.font(Font.BOLD, 14)); brand.setForeground(Color.WHITE);
        header.add(logo, BorderLayout.WEST); header.add(brand, BorderLayout.CENTER);
        sidebar.add(header, BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setOpaque(false);
        nav.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel sectionLabel = new JLabel("MANAGE");
        sectionLabel.setFont(UITheme.smallFont());
        sectionLabel.setForeground(new Color(0x94, 0xA3, 0xB8));
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 0));
        nav.add(sectionLabel);
        nav.add(navCitizens);
        nav.add(navComplaints);
        nav.add(navFeedback);
        nav.add(Box.createVerticalStrut(20));
        nav.add(navExit);
        sidebar.add(nav, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        UITheme.IconBadge badge = new UITheme.IconBadge("A", UITheme.ACCENT, UITheme.NAVY_DARK, 36);
        JPanel uinfo = new JPanel(); uinfo.setLayout(new BoxLayout(uinfo, BoxLayout.Y_AXIS)); uinfo.setOpaque(false);
        JLabel uname = new JLabel("Administrator"); uname.setFont(UITheme.h2Font()); uname.setForeground(Color.WHITE);
        JLabel urole = new JLabel("admin@smartcity"); urole.setFont(UITheme.smallFont()); urole.setForeground(new Color(0x94, 0xA3, 0xB8));
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
        JLabel title = new JLabel("Admin Dashboard"); title.setFont(UITheme.titleFont()); title.setForeground(UITheme.TEXT);
        JLabel subtitle = new JLabel("Manage citizens, complaints & feedback across the city");
        subtitle.setFont(UITheme.smallFont()); subtitle.setForeground(UITheme.TEXT_MUTED);
        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 2)); titles.setOpaque(false);
        titles.add(title); titles.add(subtitle);
        topBar.add(titles, BorderLayout.CENTER);
        main.add(topBar, BorderLayout.NORTH);

        JPanel contentHost = new JPanel(new BorderLayout());
        contentHost.setOpaque(false);
        contentHost.setBorder(BorderFactory.createEmptyBorder(0, 28, 24, 28));
        JPanel kpi = new JPanel(new GridLayout(1, 3, 12, 0)); kpi.setOpaque(false);
        kpi.setBorder(BorderFactory.createEmptyBorder(4, 0, 16, 0));
        kpi.add(wrapKpi("CITIZENS",   kpiCitizens,   UITheme.ACCENT));
        kpi.add(wrapKpi("COMPLAINTS", kpiComplaints, UITheme.AMBER));
        kpi.add(wrapKpi("FEEDBACK",   kpiFeedback,   UITheme.CORAL));
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
        // Swap the placeholder "--" JLabel for our real mutable one
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
    // VIEW: CITIZENS
    // ===================================================================
    private void showCitizensView() {
        setActiveNav(navCitizens);
        JPanel view = new JPanel(new BorderLayout(0, 16)); view.setOpaque(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); actions.setOpaque(false);
        JButton add = UITheme.primaryButton("+ Add Citizen");
        JButton update = UITheme.secondaryButton("Update Citizen");
        JButton delete = UITheme.dangerButton("Delete Citizen");
        JButton refresh = UITheme.secondaryButton("Refresh");
        actions.add(add); actions.add(update); actions.add(delete); actions.add(refresh);
        view.add(actions, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{}, new Object[]{"ID", "Name", "Email", "Phone", "Address"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        view.add(UITheme.styledTable(table), BorderLayout.CENTER);

        add.addActionListener(e -> showAddCitizenDialog(model));
        update.addActionListener(e -> showUpdateCitizenDialog(model));
        delete.addActionListener(e -> showDeleteCitizenDialog(model, table));
        refresh.addActionListener(e -> loadCitizens(model));

        viewHost.removeAll();
        viewHost.add(view, BorderLayout.CENTER);
        viewHost.revalidate();
        viewHost.repaint();
        loadCitizens(model);
    }

    private void loadCitizens(DefaultTableModel model) {
        model.setRowCount(0);
        SwingWorker<List<Citizen>, Void> w = new SwingWorker<>() {
            @Override protected List<Citizen> doInBackground() {
                return GuiContext.services().citizenService().getAllCitizens();
            }
            @Override protected void done() {
                try {
                    List<Citizen> list = get();
                    for (Citizen c : list) {
                        model.addRow(new Object[]{c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()});
                    }
                    kpiCitizens.setText(String.valueOf(list.size()));
                } catch (Exception ex) {
                    showDbError(ex, "citizens");
                }
            }
        };
        w.execute();
    }

    private void showAddCitizenDialog(DefaultTableModel model) {
        JTextField name = UITheme.modernTextField("Full name");
        JTextField email = UITheme.modernTextField("you@example.com");
        JTextField phone = UITheme.modernTextField("10-digit phone");
        JPasswordField password = UITheme.modernPasswordField("Choose a password");
        JTextField address = UITheme.modernTextField("Residential address");
        Object[] fields = {"Name:", name, "Email:", email, "Phone:", phone, "Password:", password, "Address:", address};
        int result = JOptionPane.showConfirmDialog(this, UITheme.formPanel(fields),
                "Add Citizen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            SwingWorker<Void, Void> w = new SwingWorker<>() {
                @Override protected Void doInBackground() {
                    GuiContext.services().citizenService().addCitizen(
                            name.getText(), email.getText(), phone.getText(),
                            new String(password.getPassword()), address.getText());
                    return null;
                }
                @Override protected void done() {
                    try { get(); JOptionPane.showMessageDialog(DashboardFrame.this, "Citizen Added Successfully!"); loadCitizens(model); }
                    catch (Exception ex) { showDbError(ex, "add citizen"); }
                }
            };
            w.execute();
        }
    }

    private void showUpdateCitizenDialog(DefaultTableModel model) {
        String idInput = JOptionPane.showInputDialog(this, "Enter Citizen ID:");
        if (idInput == null) return;
        try {
            int id = Integer.parseInt(idInput);
            JTextField name = UITheme.modernTextField("New name");
            JTextField email = UITheme.modernTextField("New email");
            JTextField phone = UITheme.modernTextField("New phone");
            JTextField address = UITheme.modernTextField("New address");
            Object[] fields = {"New Name:", name, "New Email:", email, "New Phone:", phone, "New Address:", address};
            int result = JOptionPane.showConfirmDialog(this, UITheme.formPanel(fields),
                    "Update Citizen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                SwingWorker<Void, Void> w = new SwingWorker<>() {
                    @Override protected Void doInBackground() {
                        GuiContext.services().citizenService().updateCitizen(
                                id, name.getText(), email.getText(), phone.getText(), address.getText());
                        return null;
                    }
                    @Override protected void done() {
                        try { get(); JOptionPane.showMessageDialog(DashboardFrame.this, "Citizen Updated Successfully!"); loadCitizens(model); }
                        catch (Exception ex) { showDbError(ex, "update citizen"); }
                    }
                };
                w.execute();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Citizen ID.",
                    "Invalid ID", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showDeleteCitizenDialog(DefaultTableModel model, JTable table) {
        String idInput = JOptionPane.showInputDialog(this, "Enter Citizen ID to Delete:");
        if (idInput == null) return;
        try {
            int id = Integer.parseInt(idInput);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete Citizen ID " + id + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                SwingWorker<Void, Void> w = new SwingWorker<>() {
                    @Override protected Void doInBackground() {
                        GuiContext.services().citizenService().deleteCitizen(id);
                        return null;
                    }
                    @Override protected void done() {
                        try { get(); JOptionPane.showMessageDialog(DashboardFrame.this, "Citizen Deleted Successfully!"); loadCitizens(model); }
                        catch (Exception ex) { showDbError(ex, "delete citizen"); }
                    }
                };
                w.execute();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Citizen ID.",
                    "Invalid ID", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ===================================================================
    // VIEW: COMPLAINTS
    // ===================================================================
    private void showComplaintsView() {
        setActiveNav(navComplaints);
        JPanel view = new JPanel(new BorderLayout(0, 16)); view.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); actions.setOpaque(false);
        JButton updateStatus = UITheme.primaryButton("Update Status");
        JButton refresh = UITheme.secondaryButton("Refresh");
        actions.add(updateStatus); actions.add(refresh);
        view.add(actions, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{}, new Object[]{"ID", "Citizen", "Subject", "Description", "Status"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        view.add(UITheme.styledTable(table), BorderLayout.CENTER);

        updateStatus.addActionListener(e -> showUpdateStatusDialog(model));
        refresh.addActionListener(e -> loadComplaints(model));

        viewHost.removeAll();
        viewHost.add(view, BorderLayout.CENTER);
        viewHost.revalidate();
        viewHost.repaint();
        loadComplaints(model);
    }

    private void loadComplaints(DefaultTableModel model) {
        model.setRowCount(0);
        SwingWorker<List<Complaint>, Void> w = new SwingWorker<>() {
            @Override protected List<Complaint> doInBackground() {
                return GuiContext.services().complaintService().getAllComplaints();
            }
            @Override protected void done() {
                try {
                    List<Complaint> list = get();
                    for (Complaint c : list) {
                        model.addRow(new Object[]{c.getId(), c.getCitizenName(),
                                c.getSubject(), c.getDescription(), c.getStatus().dbValue()});
                    }
                    kpiComplaints.setText(String.valueOf(list.size()));
                } catch (Exception ex) { showDbError(ex, "complaints"); }
            }
        };
        w.execute();
    }

    private void showUpdateStatusDialog(DefaultTableModel model) {
        String idInput = JOptionPane.showInputDialog(this, "Enter Complaint ID:");
        if (idInput == null) return;
        try {
            int id = Integer.parseInt(idInput);
            String[] statuses = {"Pending", "In Progress", "Resolved"};
            String selected = (String) JOptionPane.showInputDialog(this, "Select Complaint Status:",
                    "Update Status", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);
            if (selected != null) {
                Complaint.Status newStatus = Complaint.Status.fromDb(selected);
                SwingWorker<Void, Void> w = new SwingWorker<>() {
                    @Override protected Void doInBackground() {
                        // Admin-only — isAdmin=true is enforced at the service layer.
                        GuiContext.services().complaintService().updateStatus(id, newStatus, true);
                        return null;
                    }
                    @Override protected void done() {
                        try { get(); JOptionPane.showMessageDialog(DashboardFrame.this, "Complaint Status Updated Successfully!"); loadComplaints(model); }
                        catch (Exception ex) { showDbError(ex, "update complaint status"); }
                    }
                };
                w.execute();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric Complaint ID.",
                    "Invalid ID", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ===================================================================
    // VIEW: FEEDBACK
    // ===================================================================
    private void showFeedbackView() {
        setActiveNav(navFeedback);
        JPanel view = new JPanel(new BorderLayout(0, 16)); view.setOpaque(false);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); actions.setOpaque(false);
        JButton refresh = UITheme.secondaryButton("Refresh");
        actions.add(refresh);
        view.add(actions, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{}, new Object[]{"Citizen", "Rating", "Comment", "Date"}) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model); table.setRowHeight(48);
        view.add(UITheme.styledTable(table), BorderLayout.CENTER);

        refresh.addActionListener(e -> loadFeedback(model));

        viewHost.removeAll();
        viewHost.add(view, BorderLayout.CENTER);
        viewHost.revalidate();
        viewHost.repaint();
        loadFeedback(model);
    }

    private void loadFeedback(DefaultTableModel model) {
        model.setRowCount(0);
        SwingWorker<List<Feedback>, Void> w = new SwingWorker<>() {
            @Override protected List<Feedback> doInBackground() {
                return GuiContext.services().feedbackService().getAllFeedback();
            }
            @Override protected void done() {
                try {
                    List<Feedback> list = get();
                    for (Feedback f : list) {
                        model.addRow(new Object[]{f.getCitizenName(), f.getRating() + "/5",
                                f.getComment(),
                                f.getCreatedAt() == null ? "" : f.getCreatedAt().toLocalDate().toString()});
                    }
                    kpiFeedback.setText(String.valueOf(list.size()));
                } catch (Exception ex) { showDbError(ex, "feedback"); }
            }
        };
        w.execute();
    }

    // ===================================================================
    // HELPERS
    // ===================================================================
    private void setActiveNav(JButton active) {
        // The sidebar buttons store their active flag via the setActive()
        // method added at construction time by UITheme.sidebarButton().
        for (JButton b : new JButton[]{navCitizens, navComplaints, navFeedback, navExit}) {
            try {
                b.getClass().getMethod("setActive", boolean.class)
                 .invoke(b, b == active);
            } catch (Exception ignore) {}
        }
    }

    private void showDbError(Exception ex, String op) {
        Throwable root = ex.getCause() != null ? ex.getCause() : ex;
        String msg = root instanceof SmartCityException ? root.getMessage() : "Error while loading " + op + ".";
        JOptionPane.showMessageDialog(this, msg, "Operation Failed", JOptionPane.ERROR_MESSAGE);
        root.printStackTrace();
    }
}
