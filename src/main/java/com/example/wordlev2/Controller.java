package com.example.wordlev2;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javax.swing.*;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    // BUTTONS
    @FXML
    private Button backspaceKey, restartKey, homeKey, quitButton;

    // KEYBOARD ROWS
    @FXML
    private GridPane firstRowKeyboard, secondRowKeyboard, thirdRowKeyboard;

    // GAME FIELDS
    @FXML
    private GridPane fiveLettersField, fourLettersField, threeLettersField;

    // PANELS
    @FXML
    private AnchorPane gameField, keyboard, menu;


    private ArrayList <Button> keyboardButtons;
    private ArrayList <Label> gameContent;
    private Set <String> seen;
    private int n, m, pointer, border;
    private String[][] mat;
    private String answer;
    private boolean gameOver;
    private final String GREY = "#b3b3b3", YELLOW = "#eded00", GREEN = "#00ff0d";
    private final String labelStyle = "-fx-background-radius: 15px; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 15px; ";

    // On backspace button clicked
    @FXML
    void backspaceClicked(ActionEvent event) {

        // Ignores
        if (pointer <= 0 || gameOver) return;
        if ((pointer - 1) / m <= border) return;

        // Pointer to previous position
        pointer--;

        // Setting value
        gameContent.get(pointer).setText("");
    }

    // On keyboard buttons clicked
    @FXML
    void keyboardButtonsClicked(ActionEvent event) {

        // Ignores
        if (pointer >= n || gameOver) return;

        // Getting button and coordinates
        Button button = (Button) event.getSource();
        int pointerX = pointer / m, pointerY = pointer % m;

        // Setting values
        gameContent.get(pointer).setText(button.getText());
        mat[pointerX][pointerY] =  gameContent.get(pointer).getText();

        // Line is full
        if (pointerY == m - 1 && !gameOver) {

            // Border to ignore backspaces
            border = pointerX;

            // Updating
            update(pointerX);
        }

        // Pointer to next empty position
        pointer++;
    }

    void update(int index) {
        boolean userAnsIsRight = true;
        ArrayList <Integer> colored = new ArrayList<>();

        // Coloring to green
        for (int i = 0; i < m; i++) {
            Label currentLabel = gameContent.get(index * m + i);

            // Current letter matches the answer letter
            if (currentLabel.getText().equals(Character.toString(answer.charAt(i)))) {
                currentLabel.setStyle(labelStyle + ("-fx-background-color:" + GREEN + ";"));
                colored.add(i);
            } else
                userAnsIsRight = false;
        }

        // Coloring to yellow and grey
        for (int i = 0; i < m; i++) {
            Label currentLabel = gameContent.get(index * m + i);

            // Getting pointer value at this letter position
            int indexJ = index * m + i;

            String color = "-fx-background-color: ";

            // Ignore colored letters (green)
            if (colored.contains(i))
                continue;

            // Current letter is in the answer
            if (seen.contains(mat[index][i])) {
                color += YELLOW + ";";
            }
            // Current letter isn't in the answer
            else {
                color += GREY + ";";
                disableKey(mat[index][i]);
            }
            currentLabel.setStyle(labelStyle + color);
        }

        // User guessed the word
        if (userAnsIsRight) {
            win();
            gameOver = true;
            return;
        }

        // User lost
        if (index == 5) {
            lose();
            gameOver = true;
        }
    }

    // Disable key
    void disableKey(String key) {
        for (Button button : keyboardButtons) {
            if (button.getText().equals(key)) {
                button.setDisable(true);
                button.setOpacity(100);
            }
        }
    }

    // Lose alert
    void lose() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Good try!\nThe hidden word was: " + answer, ButtonType.OK);
        alert.showAndWait();
    }

    // Win alert
    void win() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nice!\nYou guessed the word!", ButtonType.OK);
        alert.showAndWait();
    }

    // On quit button clicked
    @FXML
    void quitClicked() {
        System.exit(0);
    }

    // On home button clicked
    @FXML
    void homeClicked() {

        // Show and hide panels
        menu.setVisible(true);
        gameField.setVisible(false);
        keyboard.setVisible(false);
        threeLettersField.setVisible(false);
        fourLettersField.setVisible(false);
        fiveLettersField.setVisible(false);
    }

    // On restart button clicked
    @FXML
    void restartClicked() {
        switch (m) {
            case 3:

                // restart 3 letter mode
                answer = Answer.get3letterWord();
                getGameContent(threeLettersField);
                break;
            case 4:

                // restart 4 letter mode
                answer = Answer.get4letterWord();
                getGameContent(fourLettersField);
                break;
            case 5:

                // restart 5 letter mode
                answer = Answer.get5letterWord();
                getGameContent(fiveLettersField);
                break;
        }
    }

    // Selected game mode
    @FXML
    void gamemodeSelected(ActionEvent event) {
        Button button = (Button) event.getSource();

        // Show and hide panels
        gameField.setVisible(true);
        keyboard.setVisible(true);
        menu.setVisible(false);

        switch (button.getText()) {
            case "3 Letters":

                // Initializing 3 letter mode
                answer = Answer.get3letterWord();
                getGameContent(threeLettersField);
                threeLettersField.setVisible(true);
                break;
            case "4 Letters":

                // Initializing 4 letter mode
                answer = Answer.get4letterWord();
                getGameContent(fourLettersField);
                fourLettersField.setVisible(true);
                break;
            case "5 Letters":

                // Initializing 5 letter mode
                answer = Answer.get5letterWord();
                getGameContent(fiveLettersField);
                fiveLettersField.setVisible(true);
                break;
        }

    }

    // Setting and clearing game content
    void getGameContent(GridPane field) {

        // Setting values
        gameContent = new ArrayList<>();
        seen = new HashSet<>();
        gameOver = false;
        pointer = 0;
        border = -1;

        // Adding answer letters to a tracking set
        for (char i : answer.toCharArray())
            seen.add(Character.toString(i));

        // Clearing, styling and adding game field
        for (Node i : field.getChildren()) {
            Label label = (Label) i;
            label.setStyle(labelStyle);
            label.setText("");
            gameContent.add(label);
        }

        // Clearing keyboard buttons
        for (Button button : keyboardButtons) {
            button.setDisable(false);
        }

        // Row count and Column count
        n = gameContent.size();
        m = n / 6;

        // Clearing the tracking matrix
        mat = new String[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = "";
            }
        }
    }


    @FXML
    void initialize() {

        // Initializing answers
        Answer.init();

        // Picking keyboard buttons
        keyboardButtons = new ArrayList<>();
        for (Node i : firstRowKeyboard.getChildren()) {
            Button button = (Button) i;
            keyboardButtons.add(button);
        }
        for (Node i : secondRowKeyboard.getChildren()) {
            Button button = (Button) i;
            keyboardButtons.add(button);
        }
        for (Node i : thirdRowKeyboard.getChildren()) {
            Button button = (Button) i;
            keyboardButtons.add(button);
        }
    }

}
