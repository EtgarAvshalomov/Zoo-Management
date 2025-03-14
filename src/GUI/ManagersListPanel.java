package GUI;

import file_connection.ManagerFileManager;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import roles_classes.Manager;

public class ManagersListPanel extends JPanel implements ActionListener {
    private ZuzaZooApp parent;
    private JTable managersTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    
    public ManagersListPanel(ZuzaZooApp parent) {
        this.parent = parent;
        
        // Set layout to match other panels
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.white);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create title label with consistent styling
        JLabel titleLabel = new JLabel("Managers");
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("MV Boil", Font.PLAIN, 30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.white);
        tablePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create table model with column names
        String[] columnNames = {"ID", "First Name", "Last Name", "Birth Date", "Salary", "Seniority", "Budget"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        managersTable = new JTable(tableModel);
        managersTable.setRowHeight(25);
        managersTable.getTableHeader().setReorderingAllowed(false);
        managersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < managersTable.getColumnCount(); i++) {
            managersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(managersTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel with consistent styling
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create buttons with consistent sizing
        JButton refreshButton = new JButton("Refresh Data");
        deleteButton = new JButton("Delete Selected Manager");
        JButton backButton = new JButton("Back to Manager Menu");
        
        // Disables the delete button initially
        deleteButton.setEnabled(false);
        
        // Set button alignment
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set button size to match other panels
        Dimension buttonSize = new Dimension(200, 40);
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.setMaximumSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        backButton.setMaximumSize(buttonSize);
        
        // Add action listeners
        refreshButton.addActionListener(this);
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);
        
        // Add selection listener to table
        managersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                deleteButton.setEnabled(managersTable.getSelectedRow() != -1);
            }
        });
        
        // Add buttons to button panel with consistent spacing
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        // Add components to main panel with consistent spacing
        add(Box.createVerticalStrut(20)); // Add top margin
        add(headerPanel);
        add(Box.createVerticalStrut(20));
        add(tablePanel);
        add(Box.createVerticalStrut(20));
        add(buttonPanel);
        add(Box.createVerticalStrut(20)); // Add bottom margin
    }
    
    public void onShow() {
        loadManagersData();
    }
    
    private void loadManagersData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try {
            // Load managers from file
            List<Manager> managerList = ManagerFileManager.getManagers();
            
            if (managerList.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No managers found in the system.", 
                    "Manager List", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Format for displaying dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            // Add managers to table
            for (Manager manager : managerList) {
                String id = manager.getId();
                String firstName = manager.getF_name();
                String lastName = manager.getL_name();
                String birthDate = dateFormat.format(manager.getB_date());
                String salary = String.format("%.2f", manager.getSalary());
                String seniority = String.format("%.1f", manager.getSeniority());
                String budget = String.format("%.2f", manager.getBudget());
                
                tableModel.addRow(new Object[]{id, firstName, lastName, birthDate, salary, seniority, budget});
            }
            
            // Auto-resize columns
            managersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            
        } catch (Exception err) {
            System.out.println("Failed to read Managers From file: " + err.getMessage());
            err.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading managers: " + err.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("Back to Manager Menu")) {
            parent.showManagerInterface();
        } else if (command.equals("Refresh Data")) {
            loadManagersData();
        } else if (command.equals("Delete Selected Manager")) {
            deleteSelectedManager();
        }
    }
    
    private void deleteSelectedManager() {
        int selectedRow = managersTable.getSelectedRow();
        if (selectedRow != -1) {
            String managerId = (String) tableModel.getValueAt(selectedRow, 0);
            
            // Check if trying to delete self
            if (managerId.equals(parent.getCurrentUserId())) {
                JOptionPane.showMessageDialog(this,
                    "Please don't delete yourself :)",
                    "Self Deletion Prevention",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String firstName = (String) tableModel.getValueAt(selectedRow, 1);
            String lastName = (String) tableModel.getValueAt(selectedRow, 2);
            
            // Show confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete manager " + firstName + " " + lastName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Get the manager object
                    Manager manager = ManagerFileManager.getManagerById(managerId);
                    if (manager != null) {
                        // Remove from the list and file
                        ManagerFileManager.removeManager(manager);
                        
                        // Remove from the table
                        tableModel.removeRow(selectedRow);
                        
                        // Disable delete button
                        deleteButton.setEnabled(false);
                        
                        JOptionPane.showMessageDialog(
                            this,
                            "Manager successfully deleted.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error deleting manager: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
} 