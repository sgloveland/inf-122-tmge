package inf122.group7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import inf122.group7.model.GameLogic;
import inf122.group7.view.GameController;
import inf122.group7.view.GameControllerFactory;

/**
 * JavaFX App
 */
public class GameDriver extends Application {
    private static final String MAIN_FXML = "menu/menu.fxml";
    private static final String GAME_FXML = "view/game.fxml";
    public static final int NUM_PLAYERS = 2;

    private static Stage mainStage;
    private static Stage[] gameStages;

    private static GameControllerFactory gameControllerFactory;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        gameControllerFactory = new GameControllerFactory();

        Scene scene = createScene(loadFXML(MAIN_FXML));
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void openGame(GameLogic gameLogic, String[] userNames) throws Exception {
        mainStage.close();
        gameLogic.initialize(userNames);

        gameStages = new Stage[NUM_PLAYERS];
        for (int i = 0; i < NUM_PLAYERS; i++) {
            GameController newController = gameControllerFactory.createGameLogicController(i, gameLogic);
            gameStages[i] = createGameStage(newController);
            if (i > 0) {
                Stage previousStage = gameStages[i - 1];
                // open the next window side-by-side with the first window
                gameStages[i].setX(previousStage.getX() + previousStage.getWidth());
                gameStages[i].setY(previousStage.getY());
            }
            gameStages[i].show();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    private static Stage createGameStage(GameController gameController) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(GAME_FXML);
        fxmlLoader.setController(gameController);
        Parent root = fxmlLoader.load();
        Scene scene = createScene(root);
        Stage stage = new Stage();
        stage.setTitle(
                gameController.getGameLogic().getGameName()
                        + ": Player #" + (gameController.getPlayerIndex() + 1));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest((WindowEvent windowEvent) -> {
            closeAllGameStages();
        });
        return stage;
    }

    private static void closeAllGameStages() {
        for (Stage stage : gameStages) {
            if (stage.isShowing()) {
                stage.close();
            }
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(fxml);
        return fxmlLoader.load();
    }

    private static FXMLLoader getFXMLLoader(String fxml) throws IOException {
        return new FXMLLoader(GameDriver.class.getResource(fxml));
    }

    private static Scene createScene(Parent root) {
        return new Scene(root, 640, 480);
    }
}