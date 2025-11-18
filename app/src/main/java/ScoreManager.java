// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// responsible for handling scores
public class ScoreManager {

    private final Font bigFont = new Font("Arial", Font.BOLD, 36);

    private final TextActor[] scoreActors = {null, null, null, null};

    int[] scores = new int[GameSystem.nbPlayers];

    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 25),
    };

    private final GameSystem system;

    final MeldStrategy scoringComposite;

    public ScoreManager(GameSystem system) {
        this.system = system;
        this.scoringComposite = new ScoringComposite(new ArrayList<>(List.of(
                new AceRunExtraKingStrategy(),
                new AceRunExtraQueenStrategy(),
                new TenToAceStrategy(),
                new RoyalMarriageStrategy(),
                new TrumpNineStrategy()
        )), system);
    }

    // update trick score
    void updateTrickScore() {
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            List<Card> cards = system.trickWinningHands[i].getCardList();
            int score = 0;
            for (Card card : cards) {
                Rank rank = (Rank) card.getRank();
                Suit suit = (Suit) card.getSuit();
                boolean isNineCard = rank.getRankCardValue() == Rank.NINE.getRankCardValue();
                boolean isTrumpCard = suit.getSuitShortHand().equals(GameSystem.trumpSuit);
                if (isNineCard && isTrumpCard) {
                    score += Rank.NINE_TRUMP;
                } else {
                    score += rank.getScoreValue();
                }
            }
            scores[i] += score;
            if (i == system.bidWinPlayerIndex) {
                if (scores[i] < system.currentBid) {
                    scores[i] = 0;
                }
            }
        }
    }

    // initialise score
    void initScore() {
        Arrays.fill(scores, 0);
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            String text = "[" + String.valueOf(scores[i]) + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, system.pinochle.bgColor, bigFont);
            system.pinochle.addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    // update total score
    void updateScore(int player) {
        system.pinochle.removeActor(scoreActors[player]);
        int displayScore = Math.max(scores[player], 0);
        String text = "P" + player + "[" + String.valueOf(displayScore) + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, system.pinochle.bgColor, bigFont);
        system.pinochle.addActor(scoreActors[player], scoreLocations[player]);
    }

    // get meld score
    void calculateMeldScore() {
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            scores[i] = scoringComposite.getScore(system.hands[i].getCardList(), GameSystem.trumpSuit);
            updateScore(i);
            Pinochle.delay(Pinochle.delayTime);
        }
    }
}
