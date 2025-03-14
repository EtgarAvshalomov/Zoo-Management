package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import file_connection.EmployeesFileManager;
import file_connection.ManagerFileManager;
import roles_classes.Employee;
import roles_classes.Manager;

public class MainMenuPanel extends JPanel implements ActionListener {
    private ZuzaZooApp parent;
    
    // Login components
    private JTextField idField;
    private JDialog loginDialog;
    
    public MainMenuPanel(ZuzaZooApp parent) {
        this.parent = parent;
        
        // Set layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Create logo label
        JLabel label = new JLabel();
        ImageIcon image = new ImageIcon("src\\GUI\\logo-min.jpg");
        Image img = image.getImage().getScaledInstance(900, 470, Image.SCALE_SMOOTH);
        ImageIcon logo = new ImageIcon(img);
        
        label.setText("Welcome to ZuzaZoo");
        label.setForeground(Color.blue);
        label.setFont(new Font("MV Boil", Font.PLAIN, 40));
        label.setIcon(logo);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setIconTextGap(-90);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create buttons
        JButton login_button = new JButton("Login");
        JButton exit_button = new JButton("Exit");
        
        // Set button alignment
        login_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set button size
        Dimension buttonSize = new Dimension(200, 40);
        login_button.setPreferredSize(buttonSize);
        login_button.setMaximumSize(buttonSize);
        exit_button.setPreferredSize(buttonSize);
        exit_button.setMaximumSize(buttonSize);
        
        // Add action listeners
        login_button.addActionListener(this);
        exit_button.addActionListener(this);
        
        // Add components to panel
        setBackground(Color.white);
        add(label);
        add(Box.createVerticalStrut(70));
        add(login_button);
        add(Box.createVerticalStrut(10));
        add(exit_button);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("Login")) {
            showLoginDialog();
        } else if (command.equals("Exit")) {
            System.out.println("Exit Successfully");
            parent.stopMusic();
            System.exit(0);
        }
    }
    
    private void showLoginDialog() {
        // Create a dialog for login
        loginDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Login", true);
        loginDialog.setUndecorated(true); // Remove all window decorations
        loginDialog.setLayout(new BorderLayout());
        loginDialog.setSize(350, 180);
        loginDialog.setLocationRelativeTo(this);
        
        // Add a title panel at the top
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(51, 51, 51));
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(titleLabel);
        
        // Add the title panel to the dialog
        loginDialog.add(titlePanel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ID field with fixed height panel
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        idPanel.setPreferredSize(new Dimension(300, 40)); // Fixed height
        idPanel.setMinimumSize(new Dimension(300, 40));
        idPanel.setMaximumSize(new Dimension(300, 40));
        
        JLabel idLabel = new JLabel("Enter your ID:");
        idField = new JTextField(15);
        idPanel.add(idLabel);
        idPanel.add(idField);
        
        // Buttons panel with fixed height
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(300, 50)); // Fixed height
        buttonPanel.setMinimumSize(new Dimension(300, 50));
        buttonPanel.setMaximumSize(new Dimension(300, 50));
        
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");
        
        loginButton.addActionListener(e -> {
            String userId = idField.getText().trim();
            
            if (userId.isEmpty()) {
                JOptionPane.showMessageDialog(loginDialog, 
                    "Please enter your ID", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if ID exists in managers
            String managerId = null;
            for (Manager manager : ManagerFileManager.getManagers()) {
                if (manager.getId().equals(userId)) {
                    managerId = userId;
                    break;
                }
            }
            
            // Check if ID exists in employees
            String employeeId = null;
            if (managerId == null) {
                for (Employee employee : EmployeesFileManager.getEmployees()) {
                    if (employee.getId().equals(userId)) {
                        employeeId = userId;
                        break;
                    }
                }
            }
            
            // Navigate to appropriate interface based on role
            if (managerId != null) {
                parent.setCurrentUser(userId, "MANAGER");
                loginDialog.dispose(); // Close the dialog
                parent.showManagerInterface();
                return;
            }
            
            if (employeeId != null) {
                parent.setCurrentUser(userId, "EMPLOYEE");
                loginDialog.dispose(); // Close the dialog
                parent.showEmployeeInterface();
                return;
            }
            
            // If ID not found
            JOptionPane.showMessageDialog(loginDialog, 
                "Invalid ID. Please try again.", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
        });
        
        cancelButton.addActionListener(e -> loginDialog.dispose());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        formPanel.add(idPanel);
        formPanel.add(Box.createVerticalStrut(20)); // More space between components
        formPanel.add(buttonPanel);
        
        loginDialog.add(formPanel, BorderLayout.CENTER);
        loginDialog.setVisible(true);
    }
}