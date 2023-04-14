package inf122.group7.view;

import java.awt.Point;

import inf122.group7.GameDriver;
import inf122.group7.model.GameBoard;
import inf122.group7.model.GameLogic;
import inf122.group7.model.OnTileChangedListener;
import inf122.group7.model.Player;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GameController {
    private static final int TILE_SIZE = 500; // for testing

    private int playerIndex;
    private GameLogic gameLogic;

    @FXML
    private HBox statusBox;

    @FXML
    private Label statusLabel;

    @FXML
    private GridPane boardPane;

    @FXML
    private VBox scoreBox;

    private Button[][] boardButtons;

    private class DefaultOnTileChangedListener implements OnTileChangedListener {
        @Override
        public void run(Point point, String newValue) {
            boardButtons[point.y][point.x].setText(newValue);
        }
    }

    public GameController(int playerIndex, GameLogic gameLogic) {
        this.playerIndex = playerIndex;
        this.gameLogic = gameLogic;

        getPlayer().setOnTileChangedListener(new DefaultOnTileChangedListener());
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public Player getPlayer() {
        return gameLogic.getPlayer(playerIndex);
    }

    public GameBoard getGameBoard() {
        return getPlayer().getGameBoard();
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public HBox getStatusBox() {
        return statusBox;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public GridPane getBoardPane() {
        return boardPane;
    }

    public VBox scoreBox() {
        return scoreBox;
    }

    public void onTilePressed(Point point) {
        gameLogic.onTilePressed(playerIndex, point);
    }

    final void drawGame() {
        drawBoard();
        drawScoreSection();
        drawStatus();
    }

    public void drawScoreSection() {
        for (int i = 0; i < GameDriver.NUM_PLAYERS; i++) {
            final int PLAYER_NUMBER = i;
            Label newPlayerScoreLabel = new Label();
            newPlayerScoreLabel.setWrapText(true);

            LongProperty playerScore = gameLogic.getPlayer(PLAYER_NUMBER).getPointsProperty();

            newPlayerScoreLabel.setText(getPlayerScoreText(PLAYER_NUMBER, PLAYER_NUMBER == playerIndex, 0));
            playerScore.addListener((observable, oldValue, newValue) -> {
                newPlayerScoreLabel
                        .setText(getPlayerScoreText(PLAYER_NUMBER, PLAYER_NUMBER == playerIndex, newValue.intValue()));
            });

            scoreBox.getChildren().add(newPlayerScoreLabel);
            scoreBox.getChildren().add(createSpacer());
        }
    }

    private String getPlayerScoreText(int playerIndex, boolean isUser, int score) {
        return "Player " + (playerIndex + 1) + ": " + gameLogic.getPlayer(playerIndex).getUsername() + "\nScore" +
                (isUser ? " (You)" : "") + ":\n" + score;
    }

    public void drawBoard() {
        boardButtons = new Button[getGameBoard().getHeight()][getGameBoard().getWidth()];
        for (int row = 0; row < getGameBoard().getHeight(); row++) {
            for (int col = 0; col < getGameBoard().getWidth(); col++) {
                Button nextTile = getTileRepresentation(row, col);
                GridPane.setVgrow(nextTile, Priority.ALWAYS);
                GridPane.setHgrow(nextTile, Priority.ALWAYS);

                boardPane.add(nextTile, row, col);
                boardButtons[row][col] = nextTile;
            }
        }
    }

    public Button getTileRepresentation(int row, int col) {
        Button tileButton = new Button(getGameBoard().getTile(new Point(col, row)).getTextValue());
        tileButton.setPrefSize(TILE_SIZE, TILE_SIZE);
        tileButton.setOnAction((ActionEvent actionEvent) -> {
            onTilePressed(new Point(col, row));
        });
        return tileButton;
    }

    public void drawStatus() {
        Player thisPlayer = gameLogic.getPlayer(playerIndex);
        StringProperty playerStatusString = thisPlayer.getStatusStringProperty();

        if (gameLogic.hasMoveLimit()) {
            IntegerProperty movesLeft = gameLogic.getMovesLeftProperty(getPlayerIndex());

            playerStatusString.addListener((observable, oldValue, newValue) -> {
                int moves = movesLeft.get();
                statusLabel.setText(newValue + ((moves > 0) ? "\nMoves Left: " + moves : ""));
            });

            movesLeft.addListener((observable, oldValue, newValue) -> {
                statusLabel.setText(playerStatusString.get() + "\nMoves Left: " + newValue);
            });
        } else {
            playerStatusString.addListener((observable, oldValue, newValue) -> {
                statusLabel.setText(newValue);
            });
        }

        thisPlayer.setStatusStringValue("You are playing " + gameLogic.getGameName());
    }

    @FXML
    private void initialize() {
        drawGame();
    }

    // Adapted from
    // https://stackoverflow.com/questions/40883858/how-to-evenly-distribute-elements-of-a-javafx-vbox
    private Node createSpacer() {
        final Region spacer = new Region();
        // Make it always grow or shrink according to the available space
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
}
