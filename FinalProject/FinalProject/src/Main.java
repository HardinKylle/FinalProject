import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class Main {

    private static final String USER_DATA_FILE = "userData.txt";

    public static void main(String[] args) {
        
        List<User> users = readUserDataFromFile();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ColorGameGUI gui = new ColorGameGUI(users);
                gui.setVisible(true);
            }});
    }

    private static List<User> readUserDataFromFile() {
        List<User> users = new ArrayList<>();
    
        // Check if the file exists, and create it if it doesn't
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String username = userData[0];
                double balance = Double.parseDouble(userData[1]);
                User user = new User(username, balance);
                users.add(user);
            }
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return users;
    }

}