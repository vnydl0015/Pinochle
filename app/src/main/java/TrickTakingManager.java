// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.List;

// responsible for handling trick taking-related tasks
public class TrickTakingManager implements TrickStrategy {

    private final GameSystem system;

    private TrickStrategy trickStrategy;

    public TrickTakingManager(GameSystem system) {
        this.system = system;
        this.trickStrategy = new RandomTrickingStrategy();
    }

    // setter
    void setTrickStrategy(TrickStrategy trickStrategy) {
        this.trickStrategy = trickStrategy;
    }

    // for trick strategy
    @Override
    public void computerTricking(GameSystem system, int nextPlayer) { trickStrategy.computerTricking(system, nextPlayer); }

    // check if human player plays valid trick during manual mode
    boolean checkValidTrick(Card playingCard, List<Card> playerCards, List<Card> existingCards) {
        if (existingCards.isEmpty()) {
            return true;
        }

        Suit playingSuit = (Suit) playingCard.getSuit();
        Rank playingRank = (Rank) playingCard.getRank();
        Card existingCard = existingCards.get(0);
        Suit existingSuit = (Suit) existingCard.getSuit();
        Rank existingRank = (Rank) existingCard.getRank();

        // Same Suit, Higher Rank, then valid
        if (playingSuit.getSuitShortHand().equals(existingSuit.getSuitShortHand()) &&
                playingRank.getRankCardValue() > existingRank.getRankCardValue()) {
            return true;
        }

        // If the chosen is not the same suit, higher rank and there is one, then not valid
        Card higherCard = CardManager.getHigherCardFromList(existingCard, playerCards);
        if (higherCard != null) {
            return false;
        }

        boolean isExistingTrump = existingSuit.getSuitShortHand().equals(GameSystem.trumpSuit);
        boolean isPlayingTrump = playingSuit.getSuitShortHand().equals(GameSystem.trumpSuit);
        // If the current is trump, then there is already no trump card with higher rank.
        // Otherwise, the above if you should return false.
        if (isExistingTrump) {
            return true;
        }

        // If the current is not trump card, then playing trump card is valid
        if (isPlayingTrump) {
            return true;
        }

        // If the current is not trump card, and we have a trump card,
        // but not having a same suit, higher rank card, then we have to play trump card
        Card trumpCard = system.cardManager.getTrumpCard(playerCards);
        if (trumpCard != null) {
            return false;
        }

        // If we don't have a trump card, any card is valid
        return true;
    }

    // find the winner
    int checkWinner(int playerIndex) {
        assert (GameSystem.playingArea.getCardList().size() == 2);
        int previousPlayerIndex = Math.abs(playerIndex - 1) % 2;
        Card card1 = GameSystem.playingArea.getCardList().get(0);
        Card card2 = GameSystem.playingArea.getCardList().get(1);

        boolean isHigherRankSameSuit = CardManager.isSameSuit(card1, card2) && CardManager.isHigherRank(card2, card1);
        if (isHigherRankSameSuit) {
            return playerIndex;
        }

        Suit card1Suit = (Suit) card1.getSuit();
        if (card1Suit.getSuitShortHand().equals(GameSystem.trumpSuit)) {
            return previousPlayerIndex;
        }

        Suit card2Suit = (Suit) card2.getSuit();
        if (card2Suit.getSuitShortHand().equals(GameSystem.trumpSuit)) {
            return playerIndex;
        }

        return previousPlayerIndex;
    }
}
