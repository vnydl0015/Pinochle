// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class RoyalMarriageStrategy implements MeldStrategy {

    @Override
    public int getScore(List<Card> cards, String trumpSuit) {
        return Meld.ROYAL_MARRIAGE.getScore();
    }

    @Override
    public List<String> checkCards(List<Card> list, CardManager cardManager, String trumpSuit) {
        List<String> cardsToCheck = new ArrayList<>(Arrays.asList(
                Rank.QUEEN.getRankCardValue() + trumpSuit,
                Rank.KING.getRankCardValue() + trumpSuit));
        if (cardManager.checkCardInList(list, cardsToCheck)) {
            return cardsToCheck;
        }
        return null;
    }

    @Override
    public Meld getMeldType() {
        return Meld.ROYAL_MARRIAGE;
    }
}