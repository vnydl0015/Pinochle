// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.List;

// A concrete class that inherit from CompositeMeldStrategy.
// Serves to be instantiated unlike its abstract parent class CompositeMeldStrategy.
class ScoringComposite extends CompositeMeldStrategy {

    private final GameSystem system;

    ScoringComposite(List<MeldStrategy> strategies, GameSystem system) {
        super(strategies);
        this.system = system;
    }

    @Override
    public Meld getMeldType() {
        return null;
    }

    @Override
    public List<String> checkCards(List<Card> list, CardManager cardManager, String trumpSuit) {
        return null;
    }

    // Loop through all meld strategies to calculate the max possible score
    @Override
    public int getScore(List<Card> cards, String trumpSuit) {
        int totalScore = 0;
        List<String> cardsRemove;
        for (MeldStrategy strategy : strategies) {
            cardsRemove = strategy.checkCards(cards, system.cardManager, trumpSuit);
            if (cardsRemove != null) {
                totalScore += strategy.getScore(cards, trumpSuit);
                cards = system.cardManager.removeCardFromList(cards, cardsRemove);
            }
        }
        return totalScore;
    }
}