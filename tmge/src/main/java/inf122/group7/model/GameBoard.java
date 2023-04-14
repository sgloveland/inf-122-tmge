package inf122.group7.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private Tile[][] gameBoard;

    private int width;
    private int height;

    private OnTileChangedListener onTileChangedListener;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;

        gameBoard = new Tile[height][width];
        fillWithEmptyTiles();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[][] getGameBoard() {
        return gameBoard;
    }

    public void setOnTileChangedListener(OnTileChangedListener onTileChangedListener) {
        this.onTileChangedListener = onTileChangedListener;
    }

    public void setTile(Point point, Tile tile) {
        // maybe change this
        gameBoard[point.y][point.x] = tile;
        if (onTileChangedListener != null) {
            onTileChangedListener.run(point, tile.getName());
        }
        tile.setValueChangeListener((observable, oldValue, newValue) -> {
            if (onTileChangedListener != null) {
                onTileChangedListener.run(point, newValue);
            }
        });
    }

    public Tile getTile(Point point) {
        return gameBoard[point.y][point.x];
    }

    public void disableTile(Point point) {
        Tile tileToDisable = getTile(point);
        tileToDisable.setCanSelect(false);
        tileToDisable.clearValueChangeListener();
    }

    public boolean isEmptyTile(Point point) {
        Tile tile = getTile(point);
        return tile.equals(new EmptyTile());
    }

    public List<Point> getSelectedTiles() {
        List<Point> selectedTiles = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Point point = new Point(col, row);
                if (getTile(point).getIsSelected()) {
                    selectedTiles.add(point);
                }
            }
        }
        return selectedTiles;
    }

    public void swapTiles(Point point1, Point point2) {
        Tile tile1 = getTile(point1);
        Tile tile2 = getTile(point2);
        setTile(point1, tile2);
        setTile(point2, tile1);
    }

    public void selectTile(Point point) {
        // clicking selects, clicking again unselects
        if (!gameBoard[point.y][point.x].getCanSelect()) {
            return;
        }
        gameBoard[point.y][point.x].setIsSelected(!gameBoard[point.y][point.x].getIsSelected());
    }

    public void dropAndFillWithRandomTiles(RandomTileFactory randomTileFactory) {
        dropTiles();
        fillWithRandomTiles(randomTileFactory);
    }

    public void dropTiles() {
        for (int row = height - 1; row > -1; row--) {
            for (int col = 0; col < width; col++) {
                Point point = new Point(col, row);
                if (!isEmptyTile(point)) {
                    dropTile(point);
                }
            }
        }
    }

    public void dropTile(Point point) {
        // see if we can move the tile downwards
        for (int bottomRow = height - 1; bottomRow > point.y; bottomRow--) {
            Point fallPoint = new Point(point.x, bottomRow);
            if (isEmptyTile(fallPoint)) {
                swapTiles(point, fallPoint);
                break;
            }
        }
    }

    public void fillWithRandomTiles(RandomTileFactory randomTileFactory) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Point point = new Point(col, row);
                if (isEmptyTile(point)) {
                    setTile(point, randomTileFactory.createRandomTile());
                }
            }
        }
    }

    public void fillWithEmptyTiles() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Point point = new Point(col, row);
                clearTile(point);
            }
        }
    }

    public void clearTile(Point point) {
        setTile(point, new EmptyTile());
    }

    public void clearTiles(List<Point> points) {
        for (Point point : points) {
            clearTile(point);
        }
    }
}
