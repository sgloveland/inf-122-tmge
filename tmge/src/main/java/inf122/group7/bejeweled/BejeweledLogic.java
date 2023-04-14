package inf122.group7.bejeweled;

import java.awt.Point;
import java.util.List;

import inf122.group7.model.GameBoard;
import inf122.group7.model.GameLogic;
import inf122.group7.model.Player;
import inf122.group7.model.RandomTileFactory;

public class BejeweledLogic extends GameLogic {
    private static final int BOARD_SIZE = 9; // for testing
    private static final int POINTS_PER_GEM_MATCHED = 50;
    private static final int STARTING_MOVES = 20;

    private RandomTileFactory randomJewelFactory;

    public BejeweledLogic() {
        super("Bejeweled", STARTING_MOVES);
        setMatchBehavior(new BejeweledMatchingBehavior());
        randomJewelFactory = new RandomJewelFactory();
    }

    private BejeweledMatchingBehavior getBejeweledMatchingBehavior() {
        return (BejeweledMatchingBehavior) getMatchBehavior();
    }

    @Override
    public GameBoard createBoard() {
        GameBoard board = new GameBoard(BOARD_SIZE, BOARD_SIZE);
        board.fillWithRandomTiles(randomJewelFactory);
        BejeweledMatchingBehavior matchingBehavior = getBejeweledMatchingBehavior();

        // *more* hacky way of generating a board
        while (true) {
            List<Point> matches = getMatches(board);
            if (matches.isEmpty()) {
                if (!matchingBehavior.hasPossibleMoves(board)) {
                    board.fillWithEmptyTiles();
                    continue;
                }
                break;
            }
            board.clearTiles(matches);
            board.fillWithRandomTiles(randomJewelFactory);
        }
        return board;
    }

    @Override
    public void onTilePressed(int playerIndex, Point point) {
        Player player = getPlayer(playerIndex);

        if (player.getIsFinished()) {
            player.setStatusStringValue("You can no longer make moves!");
            return;
        }

        List<Point> selectedPoints = player.getSelectedPoints();

        if (selectedPoints.isEmpty()) {
            player.selectTile(point);
            return;
        }

        Point otherPoint = selectedPoints.get(0);
        // unselect the tile
        player.selectTile(otherPoint);
        if (point.equals(otherPoint)) {
            return;
        }

        if (!areAdjacentTiles(point, otherPoint)) {
            player.setStatusStringValue("You must choose adjacent tiles!");
            return;
        }

        // swap the two tiles
        boolean wasSuccessful = swapAndClearTiles(player, point, otherPoint);

        if (!wasSuccessful) {
            player.setStatusStringValue("Invalid move, try again.");
            return;
        }

        decrementMovesLeft(playerIndex);

        player.setIsFinished(!hasMovesLeft(playerIndex));

        if (!player.getIsFinished()) {
            refreshOnNoMovesLeft(player.getGameBoard()); // needs to be tested
            return;
        }

        checkAndResolveEndOfGame();
    }

    private boolean areAdjacentTiles(Point point, Point otherPoint) {
        return (point.x == otherPoint.x && point.y == otherPoint.y + 1) ||
                (point.x == otherPoint.x && point.y == otherPoint.y - 1) ||
                (point.x == otherPoint.x + 1 && point.y == otherPoint.y) ||
                (point.x == otherPoint.x - 1 && point.y == otherPoint.y);
    }

    private void refreshOnNoMovesLeft(GameBoard board) {
        BejeweledMatchingBehavior matchingBehavior = getBejeweledMatchingBehavior();

        if (!matchingBehavior.hasPossibleMoves(board)) {
            board.fillWithEmptyTiles();
            board.dropAndFillWithRandomTiles(randomJewelFactory);
        }
    }

    private boolean swapAndClearTiles(Player player, Point point1, Point point2) {
        // swap the tiles
        player.swapTiles(point1, point2);

        List<Point> matches = getMatches(player.getGameBoard());

        // if no matches
        if (matches.isEmpty()) {
            // swap the tiles back
            player.swapTiles(point1, point2);
            // return swap unsuccessful
            return false;
        }

        int numGemsMatched = matches.size();

        // if matches, clear and dropAndFillTiles() until there are no more matches
        do {
            player.clearTiles(matches);
            player.dropAndFillWithRandomTiles(randomJewelFactory);
            matches = getMatches(player.getGameBoard());
            numGemsMatched += matches.size();
        } while (!matches.isEmpty());

        // assign points based on matches
        long pointsEarned = numGemsMatched * POINTS_PER_GEM_MATCHED;
        player.addPoints(pointsEarned);
        player.setStatusStringValue(numGemsMatched + " gems were matched for a total of " + pointsEarned + " points!");
        return true; // return swap successful
    }
}
