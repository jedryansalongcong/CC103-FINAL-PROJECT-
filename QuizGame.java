
package Triviadejava;

import javax.swing.*;
import java.awt.*;

public class QuizGame extends JFrame {
    
    // Abstracting our raw data array into distinct Question object arrays
    private final Question[] quizBank = {
        new Question("1. Which data type is used to store text in Java?", new String[]{"char", "String", "int", "text"}, 1),
        new Question("2. What is the correct way to create an object in Java?", new String[]{"new MyClass();", "class MyClass();", "MyClass = new();", "alloc MyClass();"}, 0),
        new Question("3. Which keyword is used to inherit a class in Java?", new String[]{"implements", "imports", "extends", "inherits"}, 2),
        new Question("4. What is the starting index of an Array in Java?", new String[]{"1", "-1", "0", "Depends on array size"}, 2),
        new Question("5. Which of these is NOT a valid access modifier in Java?", new String[]{"public", "protected", "private", "internal"}, 3),
        new Question("6. What does JVM stand for?", new String[]{"Java Virtual Machine", "Java Variable Method", "Java Visual Mechanism", "Java Version Manager"}, 0),
        new Question("7. Which loop is guaranteed to execute at least once?", new String[]{"while loop", "for loop", "do-while loop", "for-each loop"}, 2),
        new Question("8. What is the size of an 'int' variable in Java?", new String[]{"2 bytes", "4 bytes", "8 bytes", "16 bytes"}, 1),
        new Question("9. Which operator is used to compare two values for equality?", new String[]{"=", "==", "equals", "match"}, 1),
        new Question("10. Which keyword is used to stop a loop prematurely?", new String[]{"exit", "stop", "return", "break"}, 3),
        new Question("11. Which method is the entry point for every Java application?", new String[]{"start()", "main()", "run()", "init()"}, 1),
        new Question("12. What does the 'final' keyword do when applied to a variable?", new String[]{"Makes it public", "Makes it constant", "Deletes it", "Allows inheritance"}, 1),
        new Question("13. Which package is automatically imported into every Java program?", new String[]{"java.util", "java.io", "java.lang", "java.net"}, 2),
        new Question("14. What happens when you try to divide an integer by zero in Java?", new String[]{"Returns 0", "Returns Infinity", "Throws ArithmeticException", "Compiles with warning"}, 2),
        new Question("15. Which of these collections does NOT allow duplicate elements?", new String[]{"ArrayList", "LinkedList", "HashSet", "Vector"}, 2)
    };

    private int currentQuestionIndex = 0;
    private int score = 0;
    private String userName;

    private final JLabel lblQuestion;
    private final JButton[] btnOptions = new JButton[4];
    private final JLabel lblScore;

    public QuizGame(String name) {
        this.userName = name;

        setTitle("Quiz Game - Player: " + userName);
        setSize(550, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // TOP Area
        JPanel questionPanel = new JPanel();
        questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        lblQuestion = new JLabel("", SwingConstants.CENTER);
        lblQuestion.setFont(new Font("Arial", Font.BOLD, 15));
        questionPanel.add(lblQuestion);
        add(questionPanel, BorderLayout.NORTH);

        // CENTER Area
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        for (int i = 0; i < 4; i++) {
            btnOptions[i] = new JButton();
            btnOptions[i].setFont(new Font("Arial", Font.PLAIN, 14));
            btnOptions[i].setFocusable(false);
            final int buttonIndex = i;
            btnOptions[i].addActionListener(e -> checkAnswer(buttonIndex));
            optionsPanel.add(btnOptions[i]);
        }
        add(optionsPanel, BorderLayout.CENTER);

        // BOTTOM Area
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        
        lblScore = new JLabel("Score: 0 / " + quizBank.length, SwingConstants.CENTER);
        lblScore.setFont(new Font("Arial", Font.ITALIC, 14));
        
        JButton btnLeaderboard = new JButton("View Leaderboard");
        btnLeaderboard.setFocusable(false);
        btnLeaderboard.addActionListener(e -> displayLeaderboardDialog());
        
        statusPanel.add(lblScore, BorderLayout.CENTER);
        statusPanel.add(btnLeaderboard, BorderLayout.EAST);
        add(statusPanel, BorderLayout.SOUTH);

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < quizBank.length) {
            Question q = quizBank[currentQuestionIndex];
            lblQuestion.setText(q.getQuestionText());
            for (int i = 0; i < 4; i++) {
                btnOptions[i].setText(q.getOptions()[i]);
            }
        } else {
            showFinalResults();
        }
    }

    private void checkAnswer(int selectedIndex) {
        if (currentQuestionIndex >= quizBank.length) return;

        Question currentQuestion = quizBank[currentQuestionIndex];

        if (currentQuestion.isCorrect(selectedIndex)) {
            score++;
            JOptionPane.showMessageDialog(this, "Correct!", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String correctText = currentQuestion.getOptions()[currentQuestion.getCorrectAnswerIndex()];
            JOptionPane.showMessageDialog(this, "Wrong! The correct answer was: " + correctText, "Result", JOptionPane.ERROR_MESSAGE);
        }

        currentQuestionIndex++;
        lblScore.setText("Score: " + score + " / " + quizBank.length);
        loadQuestion();
    }

    private void showFinalResults() {
        // Interfacing file write through abstraction class model method safely
        LeaderboardManager.saveScore(userName, score, quizBank.length);

        JOptionPane.showMessageDialog(this, 
            "Quiz Finished!\n\nPlayer: " + userName + "\nYour Final Score: " + score + " out of " + quizBank.length, 
            "Game Over", JOptionPane.PLAIN_MESSAGE);
        
        displayLeaderboardDialog();
        
        String[] endChoices = {"Play Again", "Switch Player", "Exit"};
        int choice = JOptionPane.showOptionDialog(this, "What would you like to do next?", "Quiz Complete",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, endChoices, endChoices[0]);

        if (choice == 0) {
            resetGame();
        } else if (choice == 1) {
            promptAndSwitchPlayer();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        currentQuestionIndex = 0;
        score = 0;
        lblScore.setText("Score: 0 / " + quizBank.length);
        loadQuestion();
    }

    private void promptAndSwitchPlayer() {
        String newName = JOptionPane.showInputDialog(this, "Enter new player name:", "Switch Player", JOptionPane.QUESTION_MESSAGE);
        this.userName = (newName == null || newName.trim().isEmpty()) ? "Anonymous Player" : newName.trim();
        setTitle("Quiz Game - Player: " + userName);
        resetGame();
    }

    private void displayLeaderboardDialog() {
        // Interfacing file read logic output strings perfectly 
        String data = LeaderboardManager.getLeaderboardRecords();
        
        JTextArea textArea = new JTextArea(data);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 250));
        JOptionPane.showMessageDialog(this, scrollPane, "Leaderboard History", JOptionPane.PLAIN_MESSAGE);
    }
}