package inf122.group7.model;

import java.awt.Point;
import java.util.List;

import inf122.group7.GameDriver;
import javafx.animation.PauseTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

public abstract class GameLogic {
    private String gameName;
    private Player[] players;
    private IMatchBehavior matchBehavior;
    private IntegerProperty[] movesLeft;
    private boolean hasMoveLimit;

    public GameLogic(String gameName, int startingMoves) {
        this(gameName);
        hasMoveLimit = true;
        movesLeft = new IntegerProperty[getNumPlayers()];
        for (int i = 0; i < getNumPlayers(); i++) {
            movesLeft[i] = new SimpleIntegerProperty(startingMoves);
        }
    }

    public GameLogic(String gameName) {
        this.gameName = gameName;
        this.players = new Player[GameDriver.NUM_PLAYERS]; // may change based on how many players to support
    }

    // anything that has to be done before the controllers are created
    public void initialize(String[] userNames) {
        createPlayers(userNames);
    }

    public String getGameName() {
        return gameName;
    }

    public boolean hasMoveLimit() {
        return hasMoveLimit;
    }

    public void createPlayers(String[] userNames) {
        for (int i = 0; i < GameDriver.NUM_PLAYERS; i++) {
            players[i] = new Player(userNames[i]);
            players[i].setGameBoard(createBoard());
        }
    }

    public int getNumPlayers() {
        return players.length;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public boolean isFinished() {
        for (Player p : this.players) {
            if (!p.getIsFinished()) {
                return false;
            }
        }
        return true;
    }

    public Player getWinner() {
        int maxI = 0;
        long maxPoints = players[0].getPoints();
        for (int i = 1; i < players.length; i++) {
            if (players[i].getPoints() > maxPoints) {
                maxI = i;
                maxPoints = players[i].getPoints();
            }
        }
        return players[maxI];
    }

    public void checkAndResolveEndOfGame() {
        boolean isLastToFinish = true;

        for (Player nextPlayer : getPlayers()) {
            isLastToFinish = isLastToFinish && nextPlayer.getIsFinished();
        }

        if (isLastToFinish) {
            resolveEndOfGame();
        }
    }

    public void resolveEndOfGame() {
        Player winner = getWinner();

        for (Player nextPlayer : getPlayers()) {
            nextPlayer.setStatusStringValue(winner.getUsername() + " has won the game!");
        }
    }

    public IMatchBehavior getMatchBehavior() {
        return matchBehavior;
    }

    public void setMatchBehavior(IMatchBehavior matchBehavior) {
        this.matchBehavior = matchBehavior;
    }

    public List<Point> getMatches(GameBoard board) {
        return matchBehavior.getMatches(board);
    }

    public void setStatus(Player player, String status) {
        player.setStatusStringValue(status);
    }

    public IntegerProperty getMovesLeftProperty(int playerIndex) {
        return movesLeft[playerIndex];
    }

    public int getMovesLeft(int playerIndex) {
        return movesLeft[playerIndex].get();
    }

    public boolean hasMovesLeft(int playerIndex) {
        return getMovesLeft(playerIndex) > 0;
    }

    public void decrementMovesLeft(int playerIndex) {
        movesLeft[playerIndex].set(getMovesLeft(playerIndex) - 1);
    }

    public void executeDelayedAction(DelayedAction action, Duration delayBeforeExecution) {
        PauseTransition pauser = new PauseTransition(delayBeforeExecution);
        pauser.setOnFinished(event -> {
            action.execute();
        });
        pauser.play();
    }

    public abstract GameBoard createBoard();

    public abstract void onTilePressed(int playerIndex, Point point);
}
