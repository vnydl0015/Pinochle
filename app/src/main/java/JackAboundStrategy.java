// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.List;

class JackAboundStrategy implements MeldStrategy {
    @Override
    public int getScore(List<Card> cards, String trumpSuit) {
        return Meld.JACK_ABOUND.getScore();
    }

    @Override
    public List<String> checkCards(List<Card> list, CardManager cardManager, String trumpSuit) {
        List<String> cardsToCheck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            String jack = Rank.JACK.getRankCardValue() + suit.getSuitShortHand();
            // Add two Jacks for each suit
            cardsToCheck.add(jack);
            cardsToCheck.add(jack);
        }

        if (cardManager.checkCardInList(list, cardsToCheck)) {
            return cardsToCheck;
        }
        return null;
    }

    @Override
    public Meld getMeldType() {
        return Meld.JACK_ABOUND;
    }
}