// Team Number: Wed-16:00-Team-03

// Stores all meld types and their score. They are ordered based on their score.
public enum Meld {
    DOUBLE_RUN(1900),
    JACK_ABOUND(400),
    DOUBLE_PINOCHLE(300),
    ACE_ROYAL_MARRIAGE(230),
    ACE_RUN_EXTRA_KING(190),
    ACE_RUN_EXTRA_QUEEN(190),
    TEN_TO_ACE_RUN(150),
    ACE_AROUND(100),
    ROYAL_MARRIAGE(40),
    PINOCHLE(40),
    COMMON_MARRIAGE(20),
    TRUMP_NINE(10);

    private final int score;
    Meld(int score) { this.score = score; }
    public int getScore() { return score; }
}