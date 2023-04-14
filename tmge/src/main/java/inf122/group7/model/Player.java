package inf122.group7.model;

import java.awt.Point;
import java.util.List;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Player {
    private String username;
    private GameBoard board;
    private boolean isFinished;
    private boolean isPaused;

    private StringProperty statusStringProperty;
    private LongProperty pointsProperty;

    public Player(String username) {
        this.username = username;
        pointsProperty = new SimpleLongProperty(0);
        statusStringProperty = new SimpleStringProperty("");
        this.isFinished = false;
    }

    public GameBoard getGameBoard() {
        return board;
    }

    public void setGameBoard(GameBoard board) {
        this.board = board;
    }

    public void setOnTileChangedListener(OnTileChangedListener onTileChangedListener) {
        this.board.setOnTileChangedListener(onTileChangedListener);
    }

    public String getUsername() {
        return this.username;
    }

    public long getPoints() {
        return getPointsProperty().get();
    }

    public LongProperty getPointsProperty() {
        return pointsProperty;
    }

    public void setPoints(long points) {
        pointsProperty.set(points);
    }

    public String getStatusStringValue() {
        return statusStringProperty.get();
    }

    public StringProperty getStatusStringProperty() {
        return statusStringProperty;
    }

    public void setStatusStringValue(String statusString) {
        statusStringProperty.set(statusString);
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean getIsFinished() {
        return this.isFinished;
    }

    public void addPoints(long points) {
        setPoints(points + getPoints());
    }

    public void deductPoints(long points) {
        // if (points > getPoints()) {
        // setPoints(0);
        // } else {
        // setPoints(getPoints() - points);
        // }
        setPoints(getPoints() - points);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public Tile getTile(Point point) {
        return this.board.getTile(point);
    }

    public void setTile(Point point, Tile tile) {
        this.board.setTile(point, tile);
    }

    public void selectTile(Point point) {
        this.board.selectTile(point);
    }

    public void clearTile(Point point) {
        this.board.clearTile(point);
    }

    public boolean isEmptyTile(Point point) {
        return this.board.isEmptyTile(point);
    }

    public void clearTiles(List<Point> points) {
        this.board.clearTiles(points);
    }

    public void disableTile(Point point) {
        this.board.disableTile(point);
    }

    public void swapTiles(Point point1, Point point2) {
        this.board.swapTiles(point1, point2);
    }

    public List<Point> getSelectedPoints() {
        return this.board.getSelectedTiles();
    }

    public void dropAndFillWithRandomTiles(RandomTileFactory randomTileFactory) {
        this.board.dropAndFillWithRandomTiles(randomTileFactory);
    }
}
