package GUI;

import file_connection.EmployeesFileManager;
import file_connection.ManagerFileManager;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import roles_classes.Employee;
import roles_classes.Manager;

public class ManagerPanel extends JPanel implements ActionListener {
    private ZuzaZooApp parent;
    
    public ManagerPanel(ZuzaZooApp parent) {
        this.parent = parent;
        
        // Set layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Create logo label
        JLabel label = new JLabel();
        ImageIcon image = new ImageIcon("src\\GUI\\logo-min.jpg");
        Image img = image.getImage().getScaledInstance(900, 470, Image.SCALE_SMOOTH);
        ImageIcon logo = new ImageIcon(img);
        
        // Get manager's first name
        String firstName = "Manager";  // Default value
        String managerId = parent.getCurrentUserId();
        if (managerId != null && !managerId.isEmpty()) {
            for (Manager manager : ManagerFileManager.getManagers()) {
                if (manager.getId().equals(managerId)) {
                    firstName = manager.getF_name();
                    break;
                }
            }
        }
        
        label.setText("Welcome " + firstName + " (Manager)");
        label.setForeground(Color.RED);
        label.setFont(new Font("MV Boil", Font.PLAIN, 40));
        label.setIcon(logo);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setIconTextGap(-90);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create buttons
        JButton register_button = new JButton("Register New User");
        JButton getmanagers_button = new JButton("Managers");
        JButton getemployees_button = new JButton("Employees");
        JButton getcages_button = new JButton("Cages");
        JButton getcleanings_button = new JButton("Cleanings");
        JButton back_button = new JButton("Back");
        
        // Create a panel for buttons to ensure proper alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set button alignment
        register_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        getmanagers_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        getemployees_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        getcages_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        getcleanings_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        back_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set button size
        Dimension buttonSize = new Dimension(200, 40);
        register_button.setPreferredSize(buttonSize);
        register_button.setMaximumSize(buttonSize);
        getmanagers_button.setPreferredSize(buttonSize);
        getmanagers_button.setMaximumSize(buttonSize);
        getemployees_button.setPreferredSize(buttonSize);
        getemployees_button.setMaximumSize(buttonSize);
        getcages_button.setPreferredSize(buttonSize);
        getcages_button.setMaximumSize(buttonSize);
        getcleanings_button.setPreferredSize(buttonSize);
        getcleanings_button.setMaximumSize(buttonSize);
        back_button.setPreferredSize(buttonSize);
        back_button.setMaximumSize(buttonSize);
        
        // Add action listeners
        register_button.addActionListener(this);
        getmanagers_button.addActionListener(this);
        getemployees_button.addActionListener(this);
        getcages_button.addActionListener(this);
        getcleanings_button.addActionListener(this);
        back_button.addActionListener(this);
        
        // Add buttons to button panel with consistent spacing
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(register_button);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(getmanagers_button);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(getemployees_button);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(getcages_button);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(getcleanings_button);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(back_button);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        // Add components to main panel with consistent spacing
        setBackground(Color.white);
        add(label);
        // Use consistent spacing of 20 pixels
        add(Box.createVerticalStrut(20));
        add(buttonPanel);
        // Add space at the bottom to help center the content
        add(Box.createVerticalGlue());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("Back")) {
            System.out.println("Switching to Main menu");
            parent.showMainMenu();
        } else if (command.equals("Register New User")) {
            showRegistrationDialog();
        } else if (command.equals("Managers")) {
            parent.showManagersListPanel();
        } else if (command.equals("Employees")) {
            parent.showEmployeesListPanel();
        } else if (command.equals("Cages")) {
            parent.showCagesListPanel();
        } else if (command.equals("Cleanings")) {
            parent.showCleaningsPanel();
        }
    }
    
    private void showRegistrationDialog() {
        // Create a dialog for registration
        JDialog registerDialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Register New User", true);
        registerDialog.setUndecorated(true); // Remove all window decorations
        registerDialog.setLayout(new BorderLayout());
        registerDialog.setSize(450, 350);
        registerDialog.setLocationRelativeTo(this);
        
        // Add a title panel at the top
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(51, 51, 51));
        JLabel titleLabel = new JLabel("Register New User");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(titleLabel);
        
        // Add the title panel to the dialog
        registerDialog.add(titlePanel, BorderLayout.NORTH);
        
        // Main form panel
        JPanel mainFormPanel = new JPanel(new BorderLayout());
        mainFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form fields panel with GridBagLayout for better alignment
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Form fields
        JTextField idField = new JTextField(15);
        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField birthdateField = new JTextField(15);
        JTextField salaryField = new JTextField(15);
        JTextField seniorityField = new JTextField(15);
        JTextField budgetField = new JTextField(15);
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Employee", "Manager"});
        
        // Initially disable budget field since Employee is default selection
        budgetField.setEnabled(false);
        
        // Add role selection listener to enable/disable budget field
        roleComboBox.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            budgetField.setEnabled(selectedRole.equals("Manager"));
            if (!selectedRole.equals("Manager")) {
                budgetField.setText("");
            }
        });

        // Add budget validation (non negative numbers only)
        budgetField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = budgetField.getText();
                
                // Allow digits, dot, and control characters
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                    return;
                }
                
                // Allow only one decimal point
                if (c == '.' && currentText.contains(".")) {
                    e.consume();
                }
            }
        });
        
        // Add ID input validation (9 digits only)
        idField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = idField.getText();
                
                // Allow only digits and control characters (backspace, delete)
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                    return;
                }
                
                // Limit to 9 digits
                if (currentText.length() >= 9 && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });
        
        // Add salary validation (non negative numbers only)
        salaryField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = salaryField.getText();
                
                // Allow digits, dot, and control characters
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                    return;
                }
                
                // Allow only one decimal point
                if (c == '.' && currentText.contains(".")) {
                    e.consume();
                }
            }
        });
        
        // Add seniority validation (non negative numbers only)
        seniorityField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String currentText = seniorityField.getText();
                
                // Allow digits, dot, and control characters
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                    return;
                }
                
                // Allow only one decimal point
                if (c == '.' && currentText.contains(".")) {
                    e.consume();
                }
            }
        });
        
        // Labels
        JLabel idLabel = new JLabel("ID:", SwingConstants.LEFT);
        JLabel firstNameLabel = new JLabel("First Name:", SwingConstants.LEFT);
        JLabel lastNameLabel = new JLabel("Last Name:", SwingConstants.LEFT);
        JLabel birthdateLabel = new JLabel("Birthdate (dd/MM/yyyy):", SwingConstants.LEFT);
        JLabel salaryLabel = new JLabel("Salary:", SwingConstants.LEFT);
        JLabel seniorityLabel = new JLabel("Seniority:", SwingConstants.LEFT);
        JLabel budgetLabel = new JLabel("Budget (Manager only):", SwingConstants.LEFT);
        JLabel roleLabel = new JLabel("Role:", SwingConstants.LEFT);
        
        // Add labels to left column
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(idLabel, gbc);
        
        gbc.gridy = 1;
        formPanel.add(firstNameLabel, gbc);
        
        gbc.gridy = 2;
        formPanel.add(lastNameLabel, gbc);
        
        gbc.gridy = 3;
        formPanel.add(birthdateLabel, gbc);
        
        gbc.gridy = 4;
        formPanel.add(salaryLabel, gbc);
        
        gbc.gridy = 5;
        formPanel.add(seniorityLabel, gbc);
        
        gbc.gridy = 6;
        formPanel.add(budgetLabel, gbc);
        
        gbc.gridy = 7;
        formPanel.add(roleLabel, gbc);
        
        // Add fields to right column
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        formPanel.add(idField, gbc);
        
        gbc.gridy = 1;
        formPanel.add(firstNameField, gbc);
        
        gbc.gridy = 2;
        formPanel.add(lastNameField, gbc);
        
        gbc.gridy = 3;
        formPanel.add(birthdateField, gbc);
        
        gbc.gridy = 4;
        formPanel.add(salaryField, gbc);
        
        gbc.gridy = 5;
        formPanel.add(seniorityField, gbc);
        
        gbc.gridy = 6;
        formPanel.add(budgetField, gbc);
        
        gbc.gridy = 7;
        formPanel.add(roleComboBox, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");
        
        registerButton.addActionListener(e -> {
            // Validate and process registration
            try {
                String id = idField.getText().trim();
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String birthdate = birthdateField.getText().trim();
                
                // Basic validation
                if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || birthdate.isEmpty() || 
                    salaryField.getText().trim().isEmpty() || seniorityField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "Please fill in all required fields", 
                        "Registration Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate ID length
                if (id.length() != 9) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "ID must be exactly 9 digits", 
                        "Registration Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate ID format (all digits)
                if (!id.matches("\\d{9}")) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "ID must contain only digits", 
                        "Registration Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Parse and validate salary
                float salary = Float.parseFloat(salaryField.getText().trim());
                if (salary < 0) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "Salary must be a non-negative number", 
                        "Registration Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Parse and validate seniority
                float seniority = Float.parseFloat(seniorityField.getText().trim());
                if (seniority < 0) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "Seniority must be a non-negative number", 
                        "Registration Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String role = (String)roleComboBox.getSelectedItem();
                
                // Check if ID already exists
                boolean idExists = false;
                for (Employee emp : EmployeesFileManager.getEmployees()) {
                    if (emp.getId().equals(id)) {
                        idExists = true;
                        break;
                    }
                }
                
                for (Manager mgr : ManagerFileManager.getManagers()) {
                    if (mgr.getId().equals(id)) {
                        idExists = true;
                        break;
                    }
                }
                
                if (idExists) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "ID already exists. Please choose a different ID.", 
                        "Registration Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Parse and validate birthdate
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateFormat.setLenient(false); // This makes the date parser strict
                Date birthDate;
                try {
                    birthDate = dateFormat.parse(birthdate);
                    
                    // Additional date validation
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(birthDate);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH) + 1; // Calendar months are 0-based
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    
                    // Validate year range
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    if (year < 1900 || year > currentYear) {
                        throw new ParseException("Invalid year", 0);
                    }
                    
                    // Validate month
                    if (month < 1 || month > 12) {
                        throw new ParseException("Invalid month", 0);
                    }
                    
                    // Validate day based on month
                    int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (day < 1 || day > maxDays) {
                        throw new ParseException("Invalid day for the selected month", 0);
                    }
                    
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(registerDialog, 
                        "Invalid date format or date. Please use dd/MM/yyyy and ensure the date is valid.", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Process registration based on role
                if (role.equals("Manager")) {
                    // Validate budget
                    if (budgetField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(registerDialog, 
                            "Please enter a budget for the manager", 
                            "Registration Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Parse and validate budget
                    float budget = Float.parseFloat(budgetField.getText().trim());
                    if (budget < 0) {
                        JOptionPane.showMessageDialog(registerDialog, 
                            "Budget must be a non-negative number", 
                            "Registration Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    Manager newManager = new Manager(id, firstName, lastName, birthDate, salary, seniority, budget);
                    
                    // Add the manager
                    ManagerFileManager.addManager(newManager);
                    
                    try {
                        // Write to file to persist the new manager
                        ManagerFileManager.writeManagersToFile("src\\text_files\\managers.txt");
                        JOptionPane.showMessageDialog(registerDialog, 
                            "Manager registration successful!", 
                            "Registration Complete", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(registerDialog, 
                            "Error saving manager: " + ex.getMessage(), 
                            "Registration Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Create and add new employee
                    Employee newEmployee = new Employee(id, firstName, lastName, birthDate, salary, seniority);
                    EmployeesFileManager.addEmployee(newEmployee);
                    
                    try {
                        // Write to file to persist the new employee
                        EmployeesFileManager.writeEmployeesToFile("src\\text_files\\employees.txt");
                        JOptionPane.showMessageDialog(registerDialog, 
                            "Employee registration successful!", 
                            "Registration Complete", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(registerDialog, 
                            "Error saving employee: " + ex.getMessage(), 
                            "Registration Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                registerDialog.dispose();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(registerDialog, 
                    "Please enter valid numbers for salary and seniority", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(registerDialog, 
                    "Error during registration: " + ex.getMessage(), 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> registerDialog.dispose());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        mainFormPanel.add(formPanel, BorderLayout.CENTER);
        
        registerDialog.add(mainFormPanel, BorderLayout.CENTER);
        registerDialog.add(buttonPanel, BorderLayout.SOUTH);
        registerDialog.setVisible(true);
    }
}