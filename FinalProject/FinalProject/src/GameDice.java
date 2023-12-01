import java.util.Random;

public class GameDice {
    private static final String[] COLORS = {"red", "green", "blue", "pink", "white", "yellow"};

    public static String[] rollDice() {
        Random random = new Random();
        String[] rolledColors = new String[3];
        for (int i = 0; i < 3; i++) {
            rolledColors[i] = COLORS[random.nextInt(COLORS.length)];
        }
        return rolledColors;
    }
}