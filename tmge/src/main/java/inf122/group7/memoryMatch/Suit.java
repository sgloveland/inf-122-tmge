package inf122.group7.memoryMatch;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

public enum Suit {
    CLUB, 
    SPADE, 
    DIAMOND, 
    HEART;

    private static final List<Suit> SUITS = Arrays.asList(values());
    private static final Random RNG = new Random();

    public static Suit randomSuit() {
        return SUITS.get(RNG.nextInt(SUITS.size()));
    }

    public static String suitToString(Suit suit) {
        switch(suit) {
            case CLUB:     return "C";
            case SPADE:    return "S";
            case DIAMOND:  return "D";
            case HEART:    return "H";
            default:       return "N";
        }
    }
}
