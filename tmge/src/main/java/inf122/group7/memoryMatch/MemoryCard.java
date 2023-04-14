package inf122.group7.memoryMatch;

import inf122.group7.model.Tile;

public class MemoryCard extends Tile {
    private Rank rank;
    private Suit suit;
    private boolean facingUp;

    public MemoryCard(Rank rank, Suit suit) {
        super(Rank.rankToString(rank) + "_" + Suit.suitToString(suit), "", true, false);
        this.rank = rank;
        this.suit = suit;
        this.facingUp = false;
    }

    @Override
    public String getImage() {
        throw new UnsupportedOperationException("Unimplemented method 'getImage'");
    }
    
    public void flipCard() {
        if (this.facingUp) {
            this.facingUp = false;
            this.setName("");
        } else {
            this.facingUp = true;
            this.setName(Rank.rankToString(rank) + "-" + Suit.suitToString(suit));
        }
    }

    public Rank getRank() {
        return this.rank;
    }

    public Suit getSuit() {
        return this.suit;
    }
}
