package inf122.group7.bejeweled;

import inf122.group7.model.Tile;

public class Jewel extends Tile {
    private JewelType jewelType;

    public Jewel(JewelType jewelType) {
        super("JEWEL_" + jewelType.toString(), jewelType.toString().substring(0, 1), true, false);
        this.jewelType = jewelType;
    }

    public JewelType getJewelType() {
        return jewelType;
    }

    public void setJewelType(JewelType jewelType) {
        this.jewelType = jewelType;
    }

    @Override
    public String getImage() {
        throw new UnsupportedOperationException("Unimplemented method 'getImage'");
    }
}
