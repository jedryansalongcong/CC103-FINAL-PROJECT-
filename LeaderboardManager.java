
package Triviadejava;

import java.io.*;

public class LeaderboardManager {
    private static final String FILE_NAME = "leaderboard.txt";

    // create a new raw score record line into the file
    public static void saveScore(String name, int score, int totalQuestions) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write("Player: " + name + " | Score: " + score + "/" + totalQuestions + "\n");
        } catch (IOException e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
    }

    // Reads all raw records line by line from the file
    public static String getLeaderboardRecords() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            sb.append("No records found on the leaderboard yet. Be the first!");
        }
        return sb.toString();
    }
}