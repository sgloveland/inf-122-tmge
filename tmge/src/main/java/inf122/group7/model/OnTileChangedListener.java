package inf122.group7.model;

import java.awt.Point;

public interface OnTileChangedListener {
    public abstract void run(Point point, String newValue);
}
