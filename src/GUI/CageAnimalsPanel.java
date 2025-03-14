package GUI;

import connections.Residance;
import file_connection.AnimalFileManager;
import file_connection.ResidenceFileManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import zoo_classes.Animal;
import zoo_classes.Cage;

public class CageAnimalsPanel extends JPanel implements ActionListener {
    private ZuzaZooApp parent;
    private JTable animalsTable;
    private DefaultTableModel tableModel;
    private Cage currentCage;
    private JButton deleteButton;
    private JLabel titleLabel;
    
    public CageAnimalsPanel(ZuzaZooApp parent) {
        this.parent = parent;
        
        // Set layout to match other panels
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.white);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create title label with consistent styling
        titleLabel = new JLabel("<html><div style='text-align: center;'>Animals in Cage<br>No Cage Selected</div></html>");
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("MV Boil", Font.PLAIN, 30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.white);
        tablePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create table model with columns
        String[] columnNames = {"ID", "Name", "Species", "Weight"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table
        animalsTable = new JTable(tableModel);
        animalsTable.setRowHeight(25);
        animalsTable.getTableHeader().setReorderingAllowed(false);
        animalsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        animalsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        animalsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < animalsTable.getColumnCount(); i++) {
            animalsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Add selection listener for delete button
        animalsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                deleteButton.setEnabled(animalsTable.getSelectedRow() != -1);
            }
        });
        
        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(animalsTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel with consistent styling
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create buttons with consistent sizing
        JButton addButton = new JButton("Add Animal to Cage");
        deleteButton = new JButton("Delete Selected Animal");
        JButton refreshButton = new JButton("Refresh Data");
        JButton backButton = new JButton("Back to Cages List");
        
        // Initially disable delete button
        deleteButton.setEnabled(false);
        
        // Set button alignment
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set button size to match other panels
        Dimension buttonSize = new Dimension(200, 40);
        addButton.setPreferredSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.setMaximumSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        backButton.setMaximumSize(buttonSize);
        
        // Add action listeners
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        refreshButton.addActionListener(this);
        backButton.addActionListener(this);
        
        // Add buttons to button panel with consistent spacing
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(refreshButton);
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
    
    public void setCage(Cage cage) {
        this.currentCage = cage;
        if (cage != null) {
            titleLabel.setText("<html><div style='text-align: center;'>Animals in Cage<br>" + cage.getId() + "</div></html>");
        } else {
            titleLabel.setText("<html><div style='text-align: center;'>Animals in Cage<br>No Cage Selected</div></html>");
        }
        loadAnimalsData();
    }
    
    private void loadAnimalsData() {
        tableModel.setRowCount(0);
        
        if (currentCage == null) {
            tableModel.addRow(new Object[]{"No cage selected", "", "", ""});
            return;
        }
        
        boolean foundAnimals = false;
        for (Animal animal : AnimalFileManager.getAnimals()) {
            if (ResidenceFileManager.isAnimalInCage(animal.getId(), currentCage.getId())) {
                tableModel.addRow(new Object[]{
                    animal.getId().toString(),
                    animal.getName(),
                    animal.getSpecies(),
                    animal.getWeight()
                });
                foundAnimals = true;
            }
        }
        
        if (!foundAnimals) {
            tableModel.addRow(new Object[]{"No animals", "in this", "cage", ""});
        }
    }
    
    private void deleteSelectedAnimal() {
        int selectedRow = animalsTable.getSelectedRow();
        if (selectedRow != -1) {
            String animalId = tableModel.getValueAt(selectedRow, 0).toString();
            
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove this animal?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                for (Animal animal : AnimalFileManager.getAnimals()) {
                    if (animal.getId().toString().equals(animalId)) {
                        // End the residence
                        for (Residance residence : ResidenceFileManager.getResidences()) {
                            if (residence.getAnimalId().equals(animal.getId()) && 
                                residence.getCageId().equals(currentCage.getId()) && 
                                residence.getEndTime() == null) {
                                residence.endResidance();
                                break;
                            }
                        }
                        // Remove the animal
                        AnimalFileManager.removeAnimal(animal);
                        loadAnimalsData();
                        break;
                    }
                }
            }
        }
    }
    
    // Shows the add animal dialog
    private void showAddAnimalDialog() {
        if (currentCage == null) return;
        
        // Check current cage capacity
        int currentCapacity = ResidenceFileManager.getCapacity(currentCage.getId());
        if (currentCapacity >= currentCage.getMaxCapacity()) {
            JOptionPane.showMessageDialog(this,
                "Cannot add more animals. Cage is at maximum capacity (" + currentCage.getMaxCapacity() + ").",
                "Cage Full",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Add Animal to Cage", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name field
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        
        // Weight field
        JLabel weightLabel = new JLabel("Weight:");
        JTextField weightField = new JTextField(15);
        
        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(weightLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(weightField, gbc);
        
        // Add capacity information
        JLabel capacityLabel = new JLabel(String.format("Cage Capacity: %d/%d", currentCapacity, currentCage.getMaxCapacity()));
        capacityLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(capacityLabel, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        
        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                float weight = Float.parseFloat(weightField.getText().trim());
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a name", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check capacity again before adding
                if (ResidenceFileManager.getCapacity(currentCage.getId()) >= currentCage.getMaxCapacity()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Cannot add more animals. Cage is at maximum capacity.",
                        "Cage Full",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Create new animal with the cage's species type
                Animal newAnimal = new Animal(name, weight, currentCage.getSpecies());
                AnimalFileManager.addAnimal(newAnimal);
                
                // Create new residence for the animal in this cage
                ResidenceFileManager.addResidence(new Residance(newAnimal.getId(), currentCage.getId()));
                
                loadAnimalsData();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Animal added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid weight number", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    // Handles button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "Back to Cages List":
                parent.showCagesListPanel();
                break;
            case "Refresh Data":
                loadAnimalsData();
                break;
            case "Add Animal to Cage":
                showAddAnimalDialog();
                break;
            case "Delete Selected Animal":
                deleteSelectedAnimal();
                break;
        }
    }
} 