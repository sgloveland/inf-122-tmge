package inf122.group7.memoryMatch;

import java.awt.Point;

import inf122.group7.model.GameBoard;
import inf122.group7.model.GameLogic;
import javafx.util.Duration;
import inf122.group7.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class MemoryMatchLogic extends GameLogic {
    private static final int BOARD_SIZE = 8; // for testing
    private static final int POINTS_PER_PAIR_MATCHED = 50;
    private static final int DEDUCTION_PER_MISMATCH = 5;
    private static final int MATCH_SELECTION_DELAY = 2;
    private static final int WRONG_SELECTION_DELAY = 3;
    private static final String CLEARED_TILE = "----";

    public MemoryMatchLogic() {
        super("MemoryMatch");
    }

    // need to populate the board with random cards
    @Override
    public GameBoard createBoard() {
        GameBoard board = new GameBoard(BOARD_SIZE, BOARD_SIZE);
        final int NUM_UNIQUE_CARDS = (BOARD_SIZE * BOARD_SIZE) / 2;

        List<MemoryCard> cards = new ArrayList<>(BOARD_SIZE * BOARD_SIZE);
        Set<Pair> uniqueCards = new HashSet<>(NUM_UNIQUE_CARDS);

        Rank r;
        Suit s;
        int count = 0;
        while (count < NUM_UNIQUE_CARDS) {
            r = Rank.randomRank();
            s = Suit.randomSuit();
            uniqueCards.add(new Pair(r, s));
            ++count;
        }

        for (Pair p : uniqueCards) {
            cards.add(new MemoryCard(p.r, p.s));
            cards.add(new MemoryCard(p.r, p.s));
        }

        Collections.shuffle(cards);

        count = cards.size() - 1;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board.setTile(new Point(col, row), cards.get(count));
                count--;
            }
        }

        return board;
    }

    @Override
    public void onTilePressed(int playerIndex, Point point) {
        Player player = getPlayer(playerIndex);

        // if player is done, just return
        if (player.getIsFinished()) {
            player.setStatusStringValue("You can no longer make moves!");
            return;
        }

        if (player.isPaused()) {
            return;
        }

        List<Point> selectedPoints = player.getSelectedPoints();

        // if there are no selected points, all we do is have the player select a single
        // card
        if (selectedPoints.isEmpty()) {
            player.selectTile(point);
            flipTile(player, point);
            return;
        }

        // ignore selecting non-selectable tiles
        if (!player.getTile(point).getCanSelect()) {
            return;
        }

        Point selectedPoint = selectedPoints.get(0);

        // can't select the current tile
        if (point.equals(selectedPoint)) {
            return;
        }

        player.selectTile(point);
        flipTile(player, point);

        if (player.getTile(point).matches(player.getTile(selectedPoint))) {
            player.addPoints(POINTS_PER_PAIR_MATCHED);
            // unselect and disable both tiles
            player.selectTile(selectedPoint); 
            player.selectTile(point);
            player.getTile(selectedPoint).setName(CLEARED_TILE);
            player.getTile(point).setName(CLEARED_TILE);
            player.disableTile(selectedPoint);
            player.disableTile(point);
            player.setStatusStringValue("Card matched! Adding " + POINTS_PER_PAIR_MATCHED + " points!");
            player.setIsFinished(!hasPossiblePairs(player));
        } else {
            player.deductPoints(DEDUCTION_PER_MISMATCH);
            player.setStatusStringValue(
                    "Sorry, those cards did not match. Try again!" +
                            " You have " + WRONG_SELECTION_DELAY + " seconds to view the cards.");
            player.setPaused(true);

            executeDelayedAction(() -> {
                // Unselect and flip both tiles after waiting X seconds
                player.setPaused(false);
                player.selectTile(selectedPoint);
                player.selectTile(point);
                flipTile(player, selectedPoint);
                flipTile(player, point);
            }, Duration.seconds(WRONG_SELECTION_DELAY));
        }
    }

    private void flipTile(Player player, Point point) {
        MemoryCard card = (MemoryCard) player.getTile(point);
        card.flipCard();
    }

    private boolean hasPossiblePairs(Player player) {
        GameBoard board = player.getGameBoard();

        for (int i = 0; i < board.getWidth(); ++i) {
            for (int j = 0; j < board.getHeight(); ++j) {
                if (board.getTile(new Point(j, i)).getCanSelect()) {
                    return true;
                }
            }
        }

        return false;
    }
}

class Pair {
    public Rank r;
    public Suit s;

    public Pair(Rank r, Suit s) {
        this.r = r;
        this.s = s;
    }
}
