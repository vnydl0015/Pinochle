// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.Arrays;
import java.util.List;

class PinochleStrategy implements MeldStrategy{
    @Override
    public int getScore(List<Card> cards, String trumpSuit) {
        return Meld.PINOCHLE.getScore();
    }

    @Override
    public List<String> checkCards(List<Card> list, CardManager cardManager, String trumpSuit) {
        List<String> cardsToCheck = Arrays.asList((Rank.JACK.getRankCardValue() + Suit.DIAMONDS.getSuitShortHand()),
                (Rank.QUEEN.getRankCardValue() + Suit.SPADES.getSuitShortHand()));

        if (cardManager.checkCardInList(list, cardsToCheck)) {
            return cardsToCheck;
        }
        return null;
    }

    @Override
    public Meld getMeldType() {
        return Meld.PINOCHLE;
    }
}