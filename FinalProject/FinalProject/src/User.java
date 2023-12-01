import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JLabel;

public class User {
    private String username;
    private String chosenColor;
    private double accountBalance;
    private double betAmount;
    private JLabel balanceLabel;

    public User(String username, double accountBalance) {
        this.username = username;
        this.accountBalance = accountBalance;
    }

    public String getUsername() {
        return username;
    }

    public String getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(String color) {
        this.chosenColor = color;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void updateAccountBalance(double amount) {
        this.accountBalance += amount;
        
        if (balanceLabel != null) {
            balanceLabel.setText("Balance: " + this.accountBalance + " pesos");
        }
    }
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;

        if (balanceLabel != null) {
            balanceLabel.setText("Balance: " + this.accountBalance + " pesos");
        }
    }
    public void setBalanceLabel(JLabel balanceLabel) {
        this.balanceLabel = balanceLabel;
    }
    public JLabel getBalanceLabel() {
        return balanceLabel;
    }
    public double getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }

    public String saveUserDataToFile(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("userData.txt"))) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getAccountBalance());
                writer.newLine();
            }
            return null;
        } catch (IOException e) {
            return "Error saving user data: " + e.getMessage();
        }
    }
}