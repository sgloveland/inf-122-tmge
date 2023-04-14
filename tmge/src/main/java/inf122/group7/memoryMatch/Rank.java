package inf122.group7.memoryMatch;

import java.util.List;
import java.util.Random;
import java.util.Arrays;

public enum Rank {
    ACE, 
    TWO, 
    THREE, 
    FOUR, 
    FIVE,
    SIX, 
    SEVEN, 
    EIGHT,
    NINE, 
    TEN,
    JACK, 
    QUEEN, 
    KING;

    private static final List<Rank> RANKS = Arrays.asList(values());
    private static final Random RNG = new Random();

    public static Rank randomRank() {
        return RANKS.get(RNG.nextInt(RANKS.size()));
    }

    public static String rankToString(Rank rank) {
        switch(rank) {
            case ACE:   return "A";
            case EIGHT: return "8";
            case FIVE:  return "5";
            case FOUR:  return "4";
            case JACK:  return "J";
            case KING:  return "K";
            case NINE:  return "9";
            case QUEEN: return "Q";
            case SEVEN: return "7";
            case SIX:   return "6";
            case TEN:   return "10";
            case THREE: return "3";
            case TWO:   return "2";
            default:    return "N";
        }
    }
}
