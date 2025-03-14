/* IDs of the project creators:
Etgar Avshalomov 211390869
Ben Gofman 208136911
Liad Mandil 315096073
Maxim Prokopchuk 337875397
*/

package GUI;

import connections.Species;
import file_connection.AnimalFileManager;
import file_connection.CageFileManager;
import file_connection.CleaningFileManager;
import file_connection.EmployeesFileManager;
import file_connection.ManagerFileManager;
import file_connection.ResidenceFileManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import threads.CleaningAssignmentThread;
import threads.FileWriteThread;
import zoo_classes.Cage;

public class ZuzaZooApp extends JFrame {    

    // Constants
    private static final String MAIN_MENU = "MAIN_MENU";
    private static final String MANAGER_INTERFACE = "MANAGER_INTERFACE";
    private static final String EMPLOYEE_INTERFACE = "EMPLOYEE_INTERFACE";
    
    // UI Components
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JToggleButton muteButton;
    private ImageIcon soundIcon;
    private ImageIcon muteIcon;
    
    // Panels
    private MainMenuPanel mainMenuPanel;
    private ManagerPanel managerPanel;
    private EmployeePanel employeePanel;
    private CleaningsListPanel cleaningsPanel;
    private EmployeesListPanel employeesListPanel;
    private CagesListPanel cagesListPanel;
    private CageAnimalsPanel cageAnimalsPanel;
    private ManagersListPanel managersListPanel;
    
    // Audio
    private static Clip backgroundMusic;
    
    // Dimensions
    private final int WIDTH = 900;
    private final int HEIGHT = 800;
    
    // Add these fields to the ZuzaZooApp class
    private String currentUserId = "";
    private String currentUserRole = ""; // "MANAGER" or "EMPLOYEE"
    
    public ZuzaZooApp() {
        // Set up the frame
        super("ZuzaZoo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        
        // Load icons for mute button
        soundIcon = new ImageIcon("src\\GUI\\sound.png");
        muteIcon = new ImageIcon("src\\GUI\\mute.png");
        
        // Scale icons to wanted size (30x30)
        Image soundImg = soundIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Image muteImg = muteIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        soundIcon = new ImageIcon(soundImg);
        muteIcon = new ImageIcon(muteImg);
        
        // Create mute button
        muteButton = new JToggleButton(soundIcon);
        muteButton.setSelectedIcon(muteIcon);
        muteButton.setContentAreaFilled(false);
        muteButton.setBorderPainted(false);
        muteButton.setFocusPainted(false);
        muteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        muteButton.addActionListener(e -> {
            if (muteButton.isSelected()) {
                stopMusic();
            } else {
                playMusic("src\\GUI\\Zoo Music Loop.wav");
            }
        });
        
        // Create a wrapper panel for the mute button
        JPanel mutePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mutePanel.setOpaque(false);
        mutePanel.add(muteButton);
        
        // Initialize card layout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Create main container with BorderLayout
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(cardPanel, BorderLayout.CENTER);
        mainContainer.add(mutePanel, BorderLayout.SOUTH);
        
        // Create panels
        mainMenuPanel = new MainMenuPanel(this);
        managerPanel = new ManagerPanel(this);
        employeePanel = new EmployeePanel(this);
        cleaningsPanel = new CleaningsListPanel(this);
        employeesListPanel = new EmployeesListPanel(this);
        cagesListPanel = new CagesListPanel(this);
        cageAnimalsPanel = new CageAnimalsPanel(this);
        managersListPanel = new ManagersListPanel(this);
        
        // Add panels to card layout
        cardPanel.add(mainMenuPanel, MAIN_MENU);
        cardPanel.add(managerPanel, MANAGER_INTERFACE);
        cardPanel.add(employeePanel, EMPLOYEE_INTERFACE);
        cardPanel.add(cleaningsPanel, "CleaningsPanel");
        employeesListPanel.setName("EmployeesListPanel");
        cardPanel.add(employeesListPanel, "EmployeesListPanel");
        cagesListPanel.setName("CagesListPanel");
        cardPanel.add(cagesListPanel, "CagesListPanel");
        cageAnimalsPanel.setName("CageAnimalsPanel");
        cardPanel.add(cageAnimalsPanel, "CageAnimalsPanel");
        managersListPanel.setName("ManagersListPanel");
        cardPanel.add(managersListPanel, "ManagersListPanel");
        
        // Add card panel to frame
        add(mainContainer);
        
        // Start with Main Menu
        cardLayout.show(cardPanel, MAIN_MENU);
        
        // Play background music
        playMusic("src\\GUI\\Zoo Music Loop.wav");
        
        // Display the frame
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // Navigation methods
    public void showMainMenu() {
        clearCurrentUser();
        cardLayout.show(cardPanel, MAIN_MENU);
    }
    
    public void showManagerInterface() {
        managerPanel = new ManagerPanel(this);
        cardPanel.add(managerPanel, MANAGER_INTERFACE);
        cardLayout.show(cardPanel, MANAGER_INTERFACE);
    }
    
    public void showEmployeeInterface() {
        employeePanel = new EmployeePanel(this);
        cardPanel.add(employeePanel, EMPLOYEE_INTERFACE);
        cardLayout.show(cardPanel, EMPLOYEE_INTERFACE);
    }
    
    // Music methods
    private void playMusic(String filePath) {
        try {
            // If music is already playing, don't start it again
            if (backgroundMusic != null && backgroundMusic.isRunning()) {
                return;
            }
            
            // If we have a previous clip that's stopped, close it
            if (backgroundMusic != null) {
                backgroundMusic.close();
            }
            
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.start(); // Starts playing
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loops the music if desired
            muteButton.setSelected(false); // Update button state
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
            muteButton.setSelected(true); // Update button state
        }
    }
    
    // Main method
    public static void main(String[] args) {
        // Load data from files
        Species.initializeSpeciesData();
        EmployeesFileManager.loadEmployeesFromFile("src/text_files/employees.txt");
        ResidenceFileManager.loadResidencesFromFile("src/text_files/residences.txt");
        CageFileManager.loadCagesFromFile("src/text_files/cages.txt");
        CageFileManager.initializeCageHandler();
        AnimalFileManager.loadAnimalsFromFile("src/text_files/animals.txt");
        CleaningFileManager.loadCleaningsFromFile("src/text_files/cleanings.txt");
        ManagerFileManager.loadManagersFromFile("src/text_files/managers.txt");
        System.out.println("data loaded");

        // Cleaning Assignment Thread
        Thread cleaningAssignmentThread = new Thread(new CleaningAssignmentThread());
        cleaningAssignmentThread.start();

        // Automatic Writing Thread
        Thread automaticWriting = new Thread(new FileWriteThread());
        automaticWriting.start();

        // Start the GUI
        SwingUtilities.invokeLater(() -> new ZuzaZooApp());
    }

    // Show panel methods
    public void showCleaningsPanel() {
        cardLayout.show(cardPanel, "CleaningsPanel");
        ((CleaningsListPanel)getComponentByName(cardPanel, "CleaningsPanel")).onShow();
    }

    public void showManagerPanel() {
        cardLayout.show(cardPanel, MANAGER_INTERFACE);
    }

    public void showEmployeesListPanel() {
        cardLayout.show(cardPanel, "EmployeesListPanel");
        ((EmployeesListPanel)getComponentByName(cardPanel, "EmployeesListPanel")).onShow();
    }

    public void showCagesListPanel() {
        cardLayout.show(cardPanel, "CagesListPanel");
        ((CagesListPanel)getComponentByName(cardPanel, "CagesListPanel")).onShow();
    }

    public void showCageAnimalsPanel(Cage cage) {
        cardLayout.show(cardPanel, "CageAnimalsPanel");
        ((CageAnimalsPanel)getComponentByName(cardPanel, "CageAnimalsPanel")).setCage(cage);
    }

    public void showManagersListPanel() {
        cardLayout.show(cardPanel, "ManagersListPanel");
        ((ManagersListPanel)getComponentByName(cardPanel, "ManagersListPanel")).onShow();
    }

    // Getters and setters
    private Component getComponentByName(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
        }

        if ("CleaningsPanel".equals(name)) {
            return container.getComponent(3);
        }
        return null;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }

    public void setCurrentUser(String id, String role) {
        this.currentUserId = id;
        this.currentUserRole = role;
        System.out.println("Current user set to: " + id + " (Role: " + role + ")");
    }

    public void clearCurrentUser() {
        this.currentUserId = "";
        this.currentUserRole = "";
        System.out.println("Current user cleared");
    }
} 