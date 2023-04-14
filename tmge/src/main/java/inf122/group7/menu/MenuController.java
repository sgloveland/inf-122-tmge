package inf122.group7.menu;

import inf122.group7.GameDriver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MenuController {
    private MenuLogic logic;
    private int gameIndex;

    @FXML
    private Label currentGameLabel;

    @FXML
    private Label menuStatusLabel;

    @FXML
    private TextField userNameField1;

    @FXML
    private TextField userNameField2;

    public MenuController() throws Exception {
        logic = new MenuLogic();
    }

    @FXML
    private void initialize() {
        updateCurrentGameLabel();
    }

    @FXML
    private void previousGame() {
        setGameIndex((gameIndex - 1 + logic.getNumGames()) % logic.getNumGames());
    }

    @FXML
    private void nextGame() {
        setGameIndex((gameIndex + 1) % logic.getNumGames());
    }

    @FXML
    private void chooseGame() throws Exception {
        if (userNameField1.getText().isEmpty() || userNameField2.getText().isEmpty()) {
            menuStatusLabel.setText("Enter a user name for both players!");
            return;
        }
        // open two windows based on the current game
        GameDriver.openGame(logic.getGameLogic(gameIndex),
                new String[] { userNameField1.getText(), userNameField2.getText() });
    }

    private void setGameIndex(int value) {
        gameIndex = value;
        updateCurrentGameLabel();
    }

    private void updateCurrentGameLabel() {
        currentGameLabel.setText("Selected: " + logic.getGameName(gameIndex));
    }
}
