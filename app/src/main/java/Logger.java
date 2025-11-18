// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.List;
import java.util.stream.Collectors;

// responsible for logging information
public class Logger {

    private final StringBuilder logResult = new StringBuilder();

    // add card played to log
    void addCardPlayedToLog(int player, Card card) {
        logResult.append("P" + player + "-");
        Rank cardRank = (Rank) card.getRank();
        Suit cardSuit = (Suit) card.getSuit();
        logResult.append(cardRank.getCardLog() + cardSuit.getSuitShortHand());
        logResult.append(",");
    }

    // add bid info to log
    void addBidInfoToLog(int bidWinPlayerIndex, int currentBid) {
        logResult.append("Bid:" + bidWinPlayerIndex + "-" + currentBid + "\n");
    }

    // add trump info to log
    void addTrumpInfoToLog(int[] scores) {
        logResult.append("Trump: " + GameSystem.trumpSuit + "\n");
        logResult.append("Melding Scores: " + scores[0] + "-" + scores[1] + "\n");
    }

    // add round info to log
    void addRoundInfoToLog(int roundNumber) {
        logResult.append("\n");
        logResult.append("Round" + roundNumber + ":");
    }

    // add player cards to log
    void addPlayerCardsToLog(Hand[] hands) {
        logResult.append("Initial Cards:");
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            logResult.append("P" + i + "-");
            logResult.append(convertCardListoString(hands[i]));
        }
    }

    // convert card list to string
    String convertCardListoString(Hand hand) {
        StringBuilder sb = new StringBuilder();
        sb.append(hand.getCardList().stream().map(card -> {
            Rank rank = (Rank) card.getRank();
            Suit suit = (Suit) card.getSuit();
            return rank.getCardLog() + suit.getSuitShortHand();
        }).collect(Collectors.joining(",")));
        sb.append("-");
        return sb.toString();
    }

    // add end of game to log
    String addEndOfGameToLog(List<Integer> winners, int[] scores, Hand[] trickWinningHands) {
        logResult.append("\n");
        logResult.append("Trick Winning: ");
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            logResult.append("P" + i + ":");
            logResult.append(convertCardListoString(trickWinningHands[i]));
        }
        logResult.append("\n");
        logResult.append("Final Score: ");
        for (int score : scores) {
            logResult.append(score + ",");
        }
        logResult.append("\n");
        logResult.append("Winners: " + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
        return logResult.toString();
    }

}
