package GUI;

import file_connection.EmployeesFileManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import roles_classes.Employee;

public class EmployeePanel extends JPanel implements ActionListener {
    private ZuzaZooApp parent;
    private JButton startShiftButton;
    private JButton endShiftButton;
    private JButton backButton;
    
    public EmployeePanel(ZuzaZooApp parent) {
        this.parent = parent;
        
        // Set layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Create logo label
        JLabel label = new JLabel();
        ImageIcon image = new ImageIcon("src\\GUI\\logo-min.jpg");
        Image img = image.getImage().getScaledInstance(900, 470, Image.SCALE_SMOOTH);
        ImageIcon logo = new ImageIcon(img);
        
        // Get employee's first name
        String firstName = "Employee";  // Default value
        String employeeId = parent.getCurrentUserId();
        if (employeeId != null && !employeeId.isEmpty()) {
            for (Employee employee : EmployeesFileManager.getEmployees()) {
                if (employee.getId().equals(employeeId)) {
                    firstName = employee.getF_name();
                    break;
                }
            }
        }
        
        label.setText("Welcome " + firstName + " (Employee)");
        label.setForeground(Color.RED);
        label.setFont(new Font("MV Boil", Font.PLAIN, 40));
        label.setIcon(logo);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setIconTextGap(-90);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        // Create buttons
        startShiftButton = new JButton("Start Shift");
        endShiftButton = new JButton("End Shift");
        backButton = new JButton("Back");
        
        // Set button alignment
        startShiftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endShiftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set button size
        Dimension buttonSize = new Dimension(200, 40);
        startShiftButton.setPreferredSize(buttonSize);
        startShiftButton.setMaximumSize(buttonSize);
        endShiftButton.setPreferredSize(buttonSize);
        endShiftButton.setMaximumSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        backButton.setMaximumSize(buttonSize);
        
        // Add action listeners
        startShiftButton.addActionListener(this);
        endShiftButton.addActionListener(this);
        backButton.addActionListener(this);
        
        // Add buttons to button panel with consistent spacing
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(startShiftButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(endShiftButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        // Add components to panel with consistent spacing
        setBackground(Color.white);
        add(label);
        add(Box.createVerticalStrut(60));
        add(buttonPanel);
        add(Box.createVerticalGlue());
        
        // Check initial state
        updateButtonStates();
    }
    
    private void updateButtonStates() {
        String employeeId = parent.getCurrentUserId();
        if (employeeId != null && !employeeId.isEmpty()) {
            Employee employee = EmployeesFileManager.getEmployeeById(employeeId);
            if (employee != null && employee.isLogged()) {
                startShiftButton.setEnabled(false);
                endShiftButton.setEnabled(true);
                backButton.setEnabled(false);
            } else {
                startShiftButton.setEnabled(true);
                endShiftButton.setEnabled(false);
                backButton.setEnabled(true);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("Back")) {
            System.out.println("Switching to Main menu");
            parent.showMainMenu();
        } else if (command.equals("Start Shift")) {
            startShift();
        } else if (command.equals("End Shift")) {
            endShift();
        }
    }

    private void startShift() {
        // Get the current employee ID from the parent app
        String employeeId = parent.getCurrentUserId();
        
        if (employeeId == null || employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Error: No employee is currently logged in.", 
                "Start Shift Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the employee object
        Employee employee = EmployeesFileManager.getEmployeeById(employeeId);
        
        if (employee == null) {
            JOptionPane.showMessageDialog(this, 
                "Error: Could not find employee with ID " + employeeId, 
                "Start Shift Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create initialization dialog
        JDialog initDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Initializing Shift", true);
        initDialog.setUndecorated(true);
        initDialog.setLayout(new BorderLayout());
        
        // Add title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(51, 51, 51));
        JLabel titleLabel = new JLabel("Initializing Shift");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(titleLabel);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("Preparing your workstation...");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Initializing...");
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(progressBar);
        
        initDialog.add(titlePanel, BorderLayout.NORTH);
        initDialog.add(mainPanel, BorderLayout.CENTER);
        initDialog.setSize(300, 150);
        initDialog.setLocationRelativeTo(this);
        
        // Create timer for progress bar
        Timer progressTimer = new Timer(50, null);
        final long startTime = System.currentTimeMillis();
        final long duration = 5000; // 5 seconds
        
        progressTimer.addActionListener(evt -> {
            long now = System.currentTimeMillis();
            long elapsed = now - startTime;
            int progress = (int)((elapsed / (float)duration) * 100);
            
            if (progress >= 100) {
                progressTimer.stop();
                initDialog.dispose();
                
                // Set up the dialog handler before showing the shift started dialog
                EmployeePanel.setDialogHandler(this);
                
                // Show the shift started dialog and start the second delay
                showShiftStartedDialog(employee);
            } else {
                progressBar.setValue(progress);
                if (progress < 33) {
                    messageLabel.setText("Preparing your workstation...");
                } else if (progress < 66) {
                    messageLabel.setText("Checking cleaning schedules...");
                } else {
                    messageLabel.setText("Finalizing initialization...");
                }
            }
        });
        
        // Start the timer and show dialog
        progressTimer.start();
        initDialog.setVisible(true);
    }

    private void endShift() {
        String employeeId = parent.getCurrentUserId();
        if (employeeId == null || employeeId.isEmpty()) {
            return;
        }
        
        Employee employee = EmployeesFileManager.getEmployeeById(employeeId);
        if (employee == null) {
            return;
        }
        
        // End the employee's shift
        employee.setIsLogged(false);
        employee.setAvailable(false);
        EmployeesFileManager.removeAvailableEmployee(employee);
        
        // Update button states
        updateButtonStates();
        
        // Show confirmation dialog
        JDialog shiftEndDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Shift Complete", true);
        shiftEndDialog.setUndecorated(true);
        shiftEndDialog.setLayout(new BorderLayout());
        
        // Add title panel
        JPanel shiftEndTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        shiftEndTitlePanel.setBackground(new Color(51, 51, 51));
        JLabel shiftEndTitleLabel = new JLabel("Shift Complete");
        shiftEndTitleLabel.setForeground(Color.WHITE);
        shiftEndTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        shiftEndTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        shiftEndTitlePanel.add(shiftEndTitleLabel);
        
        // Content panel
        JPanel shiftEndContentPanel = new JPanel();
        shiftEndContentPanel.setLayout(new BoxLayout(shiftEndContentPanel, BoxLayout.Y_AXIS));
        shiftEndContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel shiftEndMessageLabel = new JLabel("<html><div style='text-align: center; width: 100%'>Your shift has ended.<br>Thank you for your work.</div></html>");
        shiftEndMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        shiftEndMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton shiftEndOkButton = new JButton("OK");
        shiftEndOkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shiftEndOkButton.addActionListener(evt -> shiftEndDialog.dispose());
        
        shiftEndContentPanel.add(shiftEndMessageLabel);
        shiftEndContentPanel.add(Box.createVerticalStrut(20));
        shiftEndContentPanel.add(shiftEndOkButton);
        
        shiftEndDialog.add(shiftEndTitlePanel, BorderLayout.NORTH);
        shiftEndDialog.add(shiftEndContentPanel, BorderLayout.CENTER);
        
        shiftEndDialog.setSize(350, 150);
        shiftEndDialog.setLocationRelativeTo(this);
        shiftEndDialog.setVisible(true);
    }

    // Reference to the current EmployeePanel instance
    private static EmployeePanel currentPanel;
    
    // Sets the current panel
    public static void setDialogHandler(EmployeePanel panel) {
        currentPanel = panel;
    }
    
    // Shows the cleaning dialog
    public static void showCleaningDialogForEmployee(Employee employee) {
        if (currentPanel != null) {
            SwingUtilities.invokeLater(() -> {
                currentPanel.showCleaningDialog(employee);
            });
        }
    }

    private void showCleaningDialog(Employee employee) {
        // Create a dialog for the cleaning task
        JDialog cleaningDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Cleaning Assignment", true);
        cleaningDialog.setUndecorated(true);
        cleaningDialog.setLayout(new BorderLayout());
        cleaningDialog.setSize(400, 200);
        cleaningDialog.setLocationRelativeTo(this);
        
        // Add a title panel at the top
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(51, 51, 51));
        JLabel titleLabel = new JLabel("Cleaning Assignment");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        
        // Add the title panel to the dialog
        cleaningDialog.add(titlePanel, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; width: 100%'>You have been assigned to clean a cage.</div></html>");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Create clean button with consistent styling
        JButton cleanButton = new JButton("Clean Cage");
        cleanButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set preferred size for the button
        Dimension buttonSize = new Dimension(200, 40);
        cleanButton.setPreferredSize(buttonSize);
        cleanButton.setMaximumSize(buttonSize);
        
        cleanButton.addActionListener(evt -> {
            // Regular clean cage functionality
            cleanButton.setEnabled(false);
            cleanButton.setText("Cleaning in progress...");
            
            // Create and configure progress bar
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            progressBar.setString("Cleaning in progress...");
            progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(progressBar);
            mainPanel.revalidate();
            mainPanel.repaint();
            
            // Start progress bar timer
            final long startTime = System.currentTimeMillis();
            Timer progressTimer = new Timer(15, null); // 15ms intervals for smooth animation
            progressTimer.addActionListener(progressEvt -> {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                int progressPercent = (int)((elapsedTime / 1500.0) * 100);
                
                if (progressPercent < 100) {
                    progressBar.setValue(progressPercent);
                } else {
                    progressBar.setValue(100);
                    progressTimer.stop();
                }
            });
            progressTimer.start();
            
            // Signal that the cleaning is complete
            employee.completeCurrentCleaning();
            
            Timer timer = new Timer(1500, timerEvt -> {
                cleaningDialog.dispose();
                
                // Create custom completion dialog
                JDialog completionDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Cleaning Complete", true);
                completionDialog.setUndecorated(true);
                completionDialog.setLayout(new BorderLayout());
                
                // Add title panel
                JPanel completionTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                completionTitlePanel.setBackground(new Color(51, 51, 51));
                JLabel completionTitleLabel = new JLabel("Cleaning Complete");
                completionTitleLabel.setForeground(Color.WHITE);
                completionTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
                completionTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                completionTitlePanel.add(completionTitleLabel);
                
                // Content panel
                JPanel completionContentPanel = new JPanel();
                completionContentPanel.setLayout(new BoxLayout(completionContentPanel, BoxLayout.Y_AXIS));
                completionContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                
                JLabel completionMessageLabel = new JLabel("<html><div style='text-align: center; width: 100%'>Cage cleaning completed successfully!</div></html>");
                completionMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                completionMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                
                JButton completionOkButton = new JButton("OK");
                completionOkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                completionOkButton.addActionListener(e -> completionDialog.dispose());
                
                completionContentPanel.add(completionMessageLabel);
                completionContentPanel.add(Box.createVerticalStrut(20));
                completionContentPanel.add(completionOkButton);
                
                completionDialog.add(completionTitlePanel, BorderLayout.NORTH);
                completionDialog.add(completionContentPanel, BorderLayout.CENTER);
                
                completionDialog.setSize(350, 150);
                completionDialog.setLocationRelativeTo(this);
                completionDialog.setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        });
        
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(cleanButton);
        
        cleaningDialog.add(mainPanel, BorderLayout.CENTER);
        cleaningDialog.setVisible(true);
    }

    private void showShiftStartedDialog(Employee employee) {
        JDialog shiftStartedDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Shift Started", true);
        shiftStartedDialog.setUndecorated(true);
        shiftStartedDialog.setLayout(new BorderLayout());
        
        // Add title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(51, 51, 51));
        JLabel titleLabel = new JLabel("Shift Started");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; width: 100%'>You have successfully started your shift!<br>" +
            "You are now in the queue for cleaning assignments.<br>" +
            "When a cage needs cleaning, you will be notified.</div></html>");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JProgressBar finalizeBar = new JProgressBar(0, 100);
        finalizeBar.setStringPainted(true);
        finalizeBar.setString("Finalizing shift start...");
        finalizeBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        finalizeBar.setVisible(false);
        
        // Button
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> {
            okButton.setEnabled(false);
            finalizeBar.setVisible(true);
            contentPanel.revalidate();
            contentPanel.repaint();
            
            // Start the 1-second delay
            Timer finalizeTimer = new Timer(20, null);
            final long startTime = System.currentTimeMillis();
            final long duration = 1000; // 1 second
            
            finalizeTimer.addActionListener(evt -> {
                long now = System.currentTimeMillis();
                long elapsed = now - startTime;
                int progress = (int)((elapsed / (float)duration) * 100);
                
                if (progress >= 100) {
                    finalizeTimer.stop();
                    
                    // Now set the employee as logged in and available
                    employee.setIsLogged(true);
                    employee.setAvailable(true);
                    EmployeesFileManager.addAvailableEmployee(employee);
                    
                    // Update button states
                    updateButtonStates();
                    
                    shiftStartedDialog.dispose();
                } else {
                    finalizeBar.setValue(progress);
                }
            });
            
            finalizeTimer.start();
        });
        
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(finalizeBar);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(okButton);
        
        shiftStartedDialog.add(titlePanel, BorderLayout.NORTH);
        shiftStartedDialog.add(contentPanel, BorderLayout.CENTER);
        
        shiftStartedDialog.setSize(400, 250);
        shiftStartedDialog.setLocationRelativeTo(this);
        shiftStartedDialog.setVisible(true);
    }
}