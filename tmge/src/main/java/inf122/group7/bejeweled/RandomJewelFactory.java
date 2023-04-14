package inf122.group7.bejeweled;

import java.util.Random;

import inf122.group7.model.RandomTileFactory;
import inf122.group7.model.Tile;

public class RandomJewelFactory implements RandomTileFactory {
    private Random random;

    public RandomJewelFactory() {
        random = new Random();
    }

    @Override
    public Tile createRandomTile() {
        return new Jewel(JewelType.values()[random.nextInt(JewelType.values().length)]);
    }

}
