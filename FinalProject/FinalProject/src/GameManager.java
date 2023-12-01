import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {

    private List<User> users;
    private Map<JButton, Color> colorButtons = new HashMap<>();
    private Map<JTextField, User> betAmountFields = new HashMap<>();
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public GameManager(List<User> users, JPanel cardPanel, CardLayout cardLayout) {
        this.users = users;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
    }

    public void playGame() {
        // Check if there are registered users
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No users registered. Please register before playing the game.");
            return;
        }
    
        JPanel playGamePanel = new JPanel(new BorderLayout()); // Use BorderLayout
    
        // Define colors
        Map<String, Color> colorMap = new HashMap<>();
        colorMap.put("red", Color.RED);
        colorMap.put("blue", Color.BLUE);
        colorMap.put("green", Color.GREEN);
        colorMap.put("pink", Color.PINK);
        colorMap.put("white", Color.WHITE);
        colorMap.put("yellow", Color.YELLOW);
    
        // Create and add colored squares to the play game panel
        JPanel colorButtonsPanel = new JPanel(new GridLayout(2, 3));
        for (String color : colorMap.keySet()) {
            JButton colorButton = new JButton("");
            colorButton.setBackground(colorMap.get(color));
            colorButton.setName(color);
            colorButton.addActionListener(new ColorButtonListener(colorButton, this));
            colorButtons.put(colorButton, colorMap.get(color));
            colorButtonsPanel.add(colorButton);
        }
        playGamePanel.add(colorButtonsPanel, BorderLayout.CENTER);
    
        // Create user info panel
        JPanel userInfoPanel = new JPanel(new GridLayout(users.size(), 4));
    
        for (User user : users) {
            JLabel nameLabel = new JLabel("         " + user.getUsername());
            double initialBalance = user.getAccountBalance();
            JLabel balanceLabel = new JLabel("Balance: " + initialBalance + " pesos");
        user.setBalanceLabel(balanceLabel);
            JComboBox<String> colorComboBox = new JComboBox<>(colorMap.keySet().toArray(new String[0]));
            JTextField betAmountField = new JTextField("0.0");
            betAmountFields.put(betAmountField, user);
    
            JButton placeBetButton = new JButton("Place Bet");
            placeBetButton.addActionListener(new BetButtonListener(colorComboBox, betAmountField, user, this));
    
            userInfoPanel.add(nameLabel);
            userInfoPanel.add(balanceLabel);
            userInfoPanel.add(colorComboBox);
            userInfoPanel.add(betAmountField);
            userInfoPanel.add(placeBetButton);
        }
        
        // Create the "Return to Main Menu" button
        JButton returnToMenuButton = new JButton("Return to Main Menu");
        returnToMenuButton.addActionListener(e -> cardLayout.show(cardPanel, "Main Menu"));

        // Create the button for handling dice roll
        JButton diceRollButton = new JButton("Roll Dice");
        diceRollButton.addActionListener(e -> handleDiceRoll());
    
        // Add the button to the SOUTH position of playGamePanel
        playGamePanel.add(diceRollButton, BorderLayout.SOUTH);
    
        // Create a wrapper panel for both play game and user info panels
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(playGamePanel, BorderLayout.CENTER);
        wrapperPanel.add(userInfoPanel, BorderLayout.SOUTH);
    
        // Add the wrapper panel to the card layout
        cardPanel.add(wrapperPanel, "PlayGame");
        wrapperPanel.add(returnToMenuButton, BorderLayout.NORTH);
        cardLayout.show(cardPanel, "PlayGame");
    }

    private void handleDiceRoll() {
        // Simulate rolling the dice
        String[] rolledColors = GameDice.rollDice();
    
        // Display the rolled colors
        String rolledColorsMessage = "Rolled Colors: " + String.join(", ", rolledColors);
        JOptionPane.showMessageDialog(null, rolledColorsMessage, "Dice Roll Result", JOptionPane.INFORMATION_MESSAGE);
    
        // Check each user's bet
        for (User user : users) {
            if (user.getChosenColor() != null && user.getBetAmount() > 0) {
                double betAmount = user.getBetAmount();
    
                // Check if the bet amount exceeds the user's balance
                if (betAmount > user.getAccountBalance()) {
                    String insufficientBalanceMessage = user.getUsername() + " has insufficient balance to place the bet.";
                    JOptionPane.showMessageDialog(null, insufficientBalanceMessage, "Insufficient Balance", JOptionPane.WARNING_MESSAGE);
                    continue; // Skip processing the bet for this user
                }
    
                String chosenColor = user.getChosenColor();
                int occurrences = countOccurrences(rolledColors, chosenColor);
                double winnings = betAmount * occurrences;
    
                if (occurrences > 0) {
                    String winMessage = user.getUsername() + " won " + winnings + " pesos!";
                    JOptionPane.showMessageDialog(null, winMessage, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                    user.updateAccountBalance(winnings);
                } else {
                    String loseMessage = user.getUsername() + " lost the bet.";
                    JOptionPane.showMessageDialog(null, loseMessage, "Better Luck Next Time!", JOptionPane.WARNING_MESSAGE);
                    // Deduct the lost bet amount from the user's account balance
                    user.updateAccountBalance(-betAmount);
                }
    
                // Save user data after updating balance
                String errorMessage = user.saveUserDataToFile(users);
    
                if (errorMessage != null) {
                    JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static int countOccurrences(String[] rolledColors, String target) {
        int count = 0;
        for (String element : rolledColors) {
            if (element.equals(target)) {
                count++;
            }
        }
        return count;
    }
    
    private static class ColorButtonListener implements ActionListener {
        
        private final JButton colorButton;
        public ColorButtonListener(JButton colorButton, GameManager manager) {
            this.colorButton = colorButton;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String color = colorButton.getName();
            System.out.println("Selected color: " + color);
        }
    }

    private static class BetButtonListener implements ActionListener {
        
        private final JComboBox<String> colorComboBox;
        private final JTextField betAmountField;
        private final User user;
    
        public BetButtonListener(JComboBox<String> colorComboBox, JTextField betAmountField, User user, GameManager manager) {
            this.colorComboBox = colorComboBox;
            this.betAmountField = betAmountField;
            this.user = user;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedColor = (String) colorComboBox.getSelectedItem();
            double betAmount = Double.parseDouble(betAmountField.getText());
    
            if (betAmount <= user.getAccountBalance()) {
                System.out.println("Placing bet for user: " + user.getUsername() +
                        ", Color: " + selectedColor +
                        ", Bet Amount: " + betAmount);
                user.setChosenColor(selectedColor);
                user.setBetAmount(betAmount);
    
            } else {
                JOptionPane.showMessageDialog(null, "Insufficient balance to place the bet.");
            }
        }
    }
}