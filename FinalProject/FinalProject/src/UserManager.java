import java.util.List;

import javax.swing.JOptionPane;

public class UserManager {
    private ColorGameGUI gui;

    public UserManager(ColorGameGUI gui) {
        this.gui = gui;
    }

    public void registerUser(List<User> users) {
        String username = JOptionPane.showInputDialog("Enter your username:");
        if (username == null || username.trim().isEmpty()) {
            // User clicked the X button or canceled the input
            return;
        }

        if (getUserByUsername(users, username) != null) {
            JOptionPane.showMessageDialog(gui, "Username is already taken. Please choose another username.");
            return;
        }

        double initialBalance;
        try {
            String balanceInput = JOptionPane.showInputDialog("Enter the initial balance for your account:");
            if (balanceInput == null) {
                return;
            }
            initialBalance = Double.parseDouble(balanceInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(gui, "Invalid input for initial balance. Please enter a numeric value.");
            return;
        }

        User newUser = new User(username, initialBalance);
        users.add(newUser);
        JOptionPane.showMessageDialog(gui, "User registered successfully! \nInitial account balance: " + initialBalance + " pesos.");
    }

    public static User getUserByUsername(List<User> users, String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public static void setBetAmount(List<User> users, String username, double betAmount) {
        User user = getUserByUsername(users, username);
        if (user != null) {
            user.setBetAmount(betAmount);
        } else {
            System.out.println("User not found. Please check the username and try again.");
        }
    }

    
}