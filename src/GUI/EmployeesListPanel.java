package GUI;

import file_connection.EmployeesFileManager;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import roles_classes.Employee;

public class EmployeesListPanel extends JPanel implements ActionListener {
    private ZuzaZooApp parent;
    private JTable employeesTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    
    public EmployeesListPanel(ZuzaZooApp parent) {
        this.parent = parent;
        
        // Set layout to match other panels
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        
        // Create title/logo panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.white);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create title label with consistent styling
        JLabel titleLabel = new JLabel("Employees");
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
        String[] columnNames = {"ID", "First Name", "Last Name", "Birth Date", "Salary", "Seniority", "Available", "Logged In"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        employeesTable = new JTable(tableModel);
        employeesTable.setRowHeight(25);
        employeesTable.getTableHeader().setReorderingAllowed(false);
        employeesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < employeesTable.getColumnCount(); i++) {
            employeesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(employeesTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel with consistent styling
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create buttons with consistent sizing
        JButton refreshButton = new JButton("Refresh Data");
        deleteButton = new JButton("Delete Selected Employee");
        JButton backButton = new JButton("Back to Manager Menu");
        
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
        
        // Initially disable delete button
        deleteButton.setEnabled(false);
        
        // Add action listeners
        refreshButton.addActionListener(this);
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);
        
        // Add selection listener to table
        employeesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                deleteButton.setEnabled(employeesTable.getSelectedRow() != -1);
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
        add(Box.createVerticalStrut(20));
        add(headerPanel);
        add(Box.createVerticalStrut(20));
        add(tablePanel);
        add(Box.createVerticalStrut(20));
        add(buttonPanel);
        add(Box.createVerticalStrut(20));
    }
    
    // Loads the data when the panel is visible
    public void onShow() {
        loadEmployeesData();
    }
    
    private void loadEmployeesData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try {
            // Load employees from file
            List<Employee> employeeList = EmployeesFileManager.getEmployees();
            
            if (employeeList.isEmpty()) {
                EmployeesFileManager.loadEmployeesFromFile("src\\text_files\\employees.txt");
                employeeList = EmployeesFileManager.getEmployees();
            }
            
            if (employeeList.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No employees found in the system.", 
                    "Employee List", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Format for displaying dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            // Add employees to table
            for (Employee employee : employeeList) {
                String id = employee.getId();
                String firstName = employee.getF_name();
                String lastName = employee.getL_name();
                String birthDate = dateFormat.format(employee.getB_date());
                String salary = String.format("%.2f", employee.getSalary());
                String seniority = String.format("%.1f", employee.getSeniority());
                String available = employee.isAvailable() ? "Yes" : "No";
                String loggedIn = employee.isLogged() ? "Yes" : "No";
                
                tableModel.addRow(new Object[]{id, firstName, lastName, birthDate, salary, seniority, available, loggedIn});
            }
            
            // Auto-resize columns
            employeesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            
        } catch (Exception err) {
            System.out.println("Failed to read Employees From file: " + err.getMessage());
            err.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading employees: " + err.getMessage(), 
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
            loadEmployeesData();
        } else if (command.equals("Delete Selected Employee")) {
            deleteSelectedEmployee();
        }
    }
    
    private void deleteSelectedEmployee() {
        int selectedRow = employeesTable.getSelectedRow();
        if (selectedRow != -1) {
            String employeeId = (String) tableModel.getValueAt(selectedRow, 0);
            String firstName = (String) tableModel.getValueAt(selectedRow, 1);
            String lastName = (String) tableModel.getValueAt(selectedRow, 2);
            
            // Show confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete employee " + firstName + " " + lastName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Get the employee object
                    Employee employee = EmployeesFileManager.getEmployeeById(employeeId);
                    if (employee != null) {
                        // Remove from the list and file
                        EmployeesFileManager.removeEmployee(employee);
                        EmployeesFileManager.writeEmployeesToFile("src\\text_files\\employees.txt");
                        
                        // Remove from the table
                        tableModel.removeRow(selectedRow);
                        
                        // Disable delete button
                        deleteButton.setEnabled(false);
                        
                        JOptionPane.showMessageDialog(
                            this,
                            "Employee successfully deleted.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error deleting employee: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
} 