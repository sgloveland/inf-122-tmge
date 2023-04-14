package inf122.group7.model;

import java.awt.Point;
import java.util.List;

public interface IMatchBehavior {
    public List<Point> getMatches(GameBoard board);
}