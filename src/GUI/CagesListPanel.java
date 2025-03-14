package GUI;

import enums.CreatureType;
import enums.SpeciesType;
import file_connection.CageFileManager;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import zoo_classes.Cage;

public class CagesListPanel extends JPanel implements ActionListener {
    private ZuzaZooApp parent;
    private JTable cagesTable;
    private DefaultTableModel tableModel;
    private JButton removeButton;
    private JButton viewAnimalsButton;
    
    public CagesListPanel(ZuzaZooApp parent) {
        this.parent = parent;
        
        // Set layout to match other panels
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.white);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create title label with consistent styling
        JLabel titleLabel = new JLabel("Cages");
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("MV Boil", Font.PLAIN, 30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Create table model with columns
        String[] columnNames = {"ID", "Species", "Type", "Capacity", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            // Make table read-only
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        // Create table
        cagesTable = new JTable(tableModel);
        cagesTable.setRowHeight(25);
        cagesTable.getTableHeader().setReorderingAllowed(false);
        cagesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        cagesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        cagesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cagesTable.getColumnCount(); i++) {
            cagesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Add selection listener
        cagesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = cagesTable.getSelectedRow() != -1;
                removeButton.setEnabled(hasSelection);
                viewAnimalsButton.setEnabled(hasSelection);
            }
        });
        
        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(cagesTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Create button panel with consistent styling
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create buttons with consistent sizing
        JButton addButton = new JButton("Add New Cage");
        removeButton = new JButton("Remove Selected Cage");
        viewAnimalsButton = new JButton("View Animals in Cage");
        JButton refreshButton = new JButton("Refresh Data");
        JButton backButton = new JButton("Back to Manager Menu");
        
        // Initially disable buttons that require selection
        removeButton.setEnabled(false);
        viewAnimalsButton.setEnabled(false);
        
        // Set button alignment
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewAnimalsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Set button size to match other panels
        Dimension buttonSize = new Dimension(200, 40);
        addButton.setPreferredSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        removeButton.setPreferredSize(buttonSize);
        removeButton.setMaximumSize(buttonSize);
        viewAnimalsButton.setPreferredSize(buttonSize);
        viewAnimalsButton.setMaximumSize(buttonSize);
        refreshButton.setPreferredSize(buttonSize);
        refreshButton.setMaximumSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        backButton.setMaximumSize(buttonSize);
        
        // Add action listeners
        addButton.addActionListener(this);
        removeButton.addActionListener(this);
        viewAnimalsButton.addActionListener(this);
        refreshButton.addActionListener(this);
        backButton.addActionListener(this);
        
        // Add buttons to button panel with consistent spacing
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(viewAnimalsButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.white);
        tablePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add components to main panel with consistent spacing
        add(Box.createVerticalStrut(20));
        add(headerPanel);
        add(Box.createVerticalStrut(20));
        add(tablePanel);
        add(Box.createVerticalStrut(20));
        add(buttonPanel);
        add(Box.createVerticalStrut(20));
    }
    
    private void showAddCageDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Add New Cage", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Species dropdown
        JLabel speciesLabel = new JLabel("Species:");
        JComboBox<SpeciesType> speciesCombo = new JComboBox<>(SpeciesType.values());
        
        // Type label
        JLabel typeLabel = new JLabel("Type:");
        JLabel typeValueLabel = new JLabel("");
        
        // Update type when species changes
        speciesCombo.addActionListener(e -> {
            SpeciesType selectedSpecies = (SpeciesType)speciesCombo.getSelectedItem();
            CreatureType type;
            
            // Determine creature type based on species
            if (selectedSpecies == SpeciesType.EAGLE || selectedSpecies == SpeciesType.PARROT || 
                selectedSpecies == SpeciesType.OWL || selectedSpecies == SpeciesType.DRAGON) {
                type = CreatureType.FLYING;
            } else if (selectedSpecies == SpeciesType.SHARK || selectedSpecies == SpeciesType.PENGUIN || 
                       selectedSpecies == SpeciesType.FISH || selectedSpecies == SpeciesType.TURTLE || 
                       selectedSpecies == SpeciesType.MERMAID) {
                type = CreatureType.SEA;
            } else {
                type = CreatureType.LAND;
            }
            
            typeValueLabel.setText(type.toString());
        });
        
        // Trigger initial type update
        speciesCombo.setSelectedIndex(0);
        
        // Capacity field
        JLabel capacityLabel = new JLabel("Capacity:");
        JTextField capacityField = new JTextField(10);
        
        // Add components to form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(speciesLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(speciesCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(typeValueLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(capacityLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(capacityField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        
        addButton.addActionListener(e -> {
            try {
                int capacity = Integer.parseInt(capacityField.getText());
                SpeciesType species = (SpeciesType)speciesCombo.getSelectedItem();
                CreatureType type = CreatureType.valueOf(typeValueLabel.getText());
                
                Cage newCage = new Cage(species, type, capacity);
                CageFileManager.addCage(newCage);
                loadCagesData();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Cage added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid capacity number", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    // Remove selected cage
    private void removeCage() {
        int selectedRow = cagesTable.getSelectedRow();
        if (selectedRow != -1) {
            String cageId = tableModel.getValueAt(selectedRow, 0).toString();
            
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove this cage?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                for (Cage cage : CageFileManager.getCages()) {
                    if (cage.getId().toString().equals(cageId)) {
                        CageFileManager.removeCage(cage);
                        loadCagesData();
                        break;
                    }
                }
            }
        }
    }
    
    // View animals in selected cage
    private void viewAnimalsInCage() {
        int selectedRow = cagesTable.getSelectedRow();
        if (selectedRow != -1) {
            String cageId = tableModel.getValueAt(selectedRow, 0).toString();
            
            for (Cage cage : CageFileManager.getCages()) {
                if (cage.getId().toString().equals(cageId)) {
                    parent.showCageAnimalsPanel(cage);
                    break;
                }
            }
        }
    }
    
    // Loads the data when the panel is visible
    public void onShow() {
        loadCagesData();
    }
    
    // Loads the cages data
    private void loadCagesData() {
        tableModel.setRowCount(0);
        
        List<Cage> cages = CageFileManager.getCages();
        if (cages.isEmpty()) {
            tableModel.addRow(new Object[]{"No cages found", "", "", "", ""});
        } else {
            for (Cage cage : cages) {
                tableModel.addRow(new Object[]{
                    cage.getId().toString(),
                    cage.getSpecies(),
                    cage.getType(),
                    cage.getMaxCapacity(),
                    cage.getIsClean() ? "Clean" : "Dirty"
                });
            }
        }
        
        cagesTable.revalidate();
        cagesTable.repaint();
    }

    // Handles button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch (command) {
            case "Back to Manager Menu":
                parent.showManagerInterface();
                break;
            case "Refresh Data":
                loadCagesData();
                break;
            case "Add New Cage":
                showAddCageDialog();
                break;
            case "Remove Selected Cage":
                removeCage();
                break;
            case "View Animals in Cage":
                viewAnimalsInCage();
                break;
        }
    }
} 