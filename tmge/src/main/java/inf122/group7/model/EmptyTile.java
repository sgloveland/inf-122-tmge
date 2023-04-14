package inf122.group7.model;

//singleton
public class EmptyTile extends Tile {
    public EmptyTile() {
        super("empty", "", false, false);
    }

    public boolean matches(Tile otherTile) {
        return false;
    }

    public boolean equals(Object other) {
        return other instanceof EmptyTile;
    }

    public String getImage() {
        return "Empty Tile Path";
    }
}
