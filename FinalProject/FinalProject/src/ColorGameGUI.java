 import javax.swing.*;
import java.awt.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ColorGameGUI extends JFrame{

    private List<User> users;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private UserManager userManager;
    Image Register;
    Image Play;
    Image Save;
    Image viewProf;
    Image Color; 

    public ColorGameGUI(List<User> users) {
        this.users = users;
        userManager = new UserManager(this);
        setTitle("Color Game");
        setSize(900, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel mainMenuPanel = createMainMenuPanel();
        cardPanel.add(mainMenuPanel, "Main Menu");

        getContentPane().add(cardPanel);

        cardLayout.show(cardPanel, "Main Menu");
        }

        private JPanel createMainMenuPanel() {
            JPanel mainMenuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Load the background image
            Image backgroundImage = Toolkit.getDefaultToolkit().getImage("images/bgg.png");
            // Draw the image as the background
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
            };

            mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10,10,10,10); 
        
            // Create buttons
            JButton registerButton = createImageButton("images/register.png");
            JButton playGameButton = createImageButton("images/play.png");
            JButton viewProfileButton = createImageButton("images/view.png");
            JButton saveAndExitButton = createImageButton("images/save.png");
        
            // Set preferred size for buttons
            Dimension buttonSize = new Dimension(300, 100); 
            registerButton.setPreferredSize(buttonSize);
            playGameButton.setPreferredSize(buttonSize);
            viewProfileButton.setPreferredSize(buttonSize);
            saveAndExitButton.setPreferredSize(buttonSize);
        
            gbc.gridy = 0;
            mainMenuPanel.add(registerButton, gbc);
            gbc.gridy = 1;
            mainMenuPanel.add(playGameButton, gbc);
            gbc.gridy = 2;
            mainMenuPanel.add(viewProfileButton, gbc);
            gbc.gridy = 3;
            mainMenuPanel.add(saveAndExitButton, gbc);
        
            // Add action listeners for the buttons
            registerButton.addActionListener(e -> userManager.registerUser(users));
            playGameButton.addActionListener(e -> {
                GameManager gameManager = new GameManager(users, cardPanel, cardLayout);
                gameManager.playGame();
            });
            viewProfileButton.addActionListener(e -> viewProfile());
            saveAndExitButton.addActionListener(e -> saveAndExit());
        
            return mainMenuPanel;
        }

        private JButton createImageButton(String imagePath) {
            JButton imageButton = new JButton();
            ImageIcon icon = new ImageIcon(imagePath);
        
            Image scaledImage = icon.getImage().getScaledInstance(300, 100, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImage);
            imageButton.setIcon(icon);
        
            imageButton.setBorderPainted(false);
            imageButton.setContentAreaFilled(false);
            imageButton.setFocusPainted(false);
            imageButton.setOpaque(false);
        
            imageButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    imageButton.setBorderPainted(true);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    imageButton.setBorderPainted(false);
                }
            });
        
            return imageButton;
        }
        private void viewProfile() {
    String username = JOptionPane.showInputDialog("Enter your username:");

    // Check if the user clicked the X button or canceled the input
    if (username == null || username.trim().isEmpty()) {
        return;
    }

    User user = UserManager.getUserByUsername(users, username);

    if (user != null) {
        Object[] options = {"Update Balance", "Delete User", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Profile for " + user.getUsername() + ":\n" +
                        "Account Balance: " + user.getAccountBalance() + " pesos",
                "View Profile",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[2]);

        switch (choice) {
            case 0: // Update Balance
                updateBalance(user);
                break;
            case 1: // Delete User
                deleteUser(user);
                break;
            // case 2: // Cancel - Do nothing in this case
        }
    } else {
        JOptionPane.showMessageDialog(this, "User not found. Please check the username and try again.");
    }
}

private void updateBalance(User user) {
    try {
        String newBalanceInput = JOptionPane.showInputDialog("Enter the new balance for the user:");
        if (newBalanceInput == null) {
            return;
        }
        double newBalance = Double.parseDouble(newBalanceInput);
        user.setAccountBalance(newBalance);
        if (user.getBalanceLabel() != null) {
            user.getBalanceLabel().setText("Balance: " + user.getAccountBalance() + " pesos");
        }
        saveUserDataToFile(users);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid input for the new balance. Please enter a numeric value.");
    }
}

private void deleteUser(User user) {
    int confirmDelete = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete the user " + user.getUsername() + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

    if (confirmDelete == JOptionPane.YES_OPTION) {
        users.remove(user);
        JOptionPane.showMessageDialog(this, "User deleted successfully!");
    }
}
        
    private void saveAndExit() {
        saveUserDataToFile(users);
        JOptionPane.showMessageDialog(this, "Exiting the game. Goodbye!");
        System.exit(0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


     private void saveUserDataToFile(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("userData.txt"))) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getAccountBalance());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "User data saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } 
}