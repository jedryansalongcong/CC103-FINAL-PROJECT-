
package Triviadejava;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String inputName = JOptionPane.showInputDialog(null, "Enter your name to start the quiz:", "Welcome", JOptionPane.QUESTION_MESSAGE);
            
            if (inputName == null || inputName.trim().isEmpty()) {
                inputName = "Anonymous Player";
            } else {
                inputName = inputName.trim();
            }
            
            // Instantiating and initializing GUI context cleanly
            new QuizGame(inputName).setVisible(true);
        });
    }
}
