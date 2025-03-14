package GUI;

import connections.Cleaning;
import file_connection.CleaningFileManager;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class CleaningsListPanel extends JPanel implements ActionListener {
    private ZuzaZooApp parent;
    private JTable cleaningsTable;
    private DefaultTableModel tableModel;
    
    public CleaningsListPanel(ZuzaZooApp parent) {
        this.parent = parent;
        
        // Set layout to match other panels
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        
        // Create title/logo panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.white);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create title label with consistent styling
        JLabel titleLabel = new JLabel("Cleanings");
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
        String[] columnNames = {"Cage ID", "Employee ID", "Start Time", "End Time", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Make table read-only
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        // Create table
        cleaningsTable = new JTable(tableModel);
        cleaningsTable.setRowHeight(25);
        cleaningsTable.getTableHeader().setReorderingAllowed(false);
        cleaningsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cleaningsTable.getColumnCount(); i++) {
            cleaningsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(cleaningsTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel with consistent styling
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create buttons with consistent sizing
        JButton refreshButton = new JButton("Refresh Data");
        JButton backButton = new JButton("Back to Manager Menu");
        
        // Set button alignment
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set button size to match other panels
        Dimension buttonSize = new Dimension(200, 40);
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.setMaximumSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        backButton.setMaximumSize(buttonSize);
        
        // Add action listeners
        refreshButton.addActionListener(this);
        backButton.addActionListener(this);
        
        // Add buttons to button panel with consistent spacing
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        // Add components to main panel with consistent spacing
        add(headerPanel);
        add(tablePanel);
        add(Box.createVerticalStrut(20));
        add(buttonPanel);
        add(Box.createVerticalStrut(20)); // Add bottom margin
    }
    
    // Add this method to load data when the panel becomes visible
    public void onShow() {
        loadCleaningsData();
    }
    
    private void loadCleaningsData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try {
            // Load cleanings from file
            CleaningFileManager.loadCleaningsFromFile("src\\text_files\\cleanings.txt");
            List<Cleaning> cleaningList = CleaningFileManager.getCleanings();
            
            if (cleaningList.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No cleanings found in the system.", 
                    "Cleaning List", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Format for displaying dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            // Add cleanings to table
            for (Cleaning cleaning : cleaningList) {
                String cageId = cleaning.getCageId().toString();
                String employeeId = cleaning.getEmployeeId();
                String startTime = cleaning.getStartTime().format(formatter);
                String endTime = cleaning.getEndTime() != null ? 
                                 cleaning.getEndTime().format(formatter) : "In Progress";
                String status = cleaning.getEndTime() != null ? "Completed" : "In Progress";
                
                tableModel.addRow(new Object[]{cageId, employeeId, startTime, endTime, status});
            }
            
            // Auto-resize columns
            cleaningsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            
        } catch (Exception err) {
            System.out.println("Failed to read Cleanings From file: " + err.getMessage());
            err.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading cleanings: " + err.getMessage(), 
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
            loadCleaningsData();
        }
    }
} 