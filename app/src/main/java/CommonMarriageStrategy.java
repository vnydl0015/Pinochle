// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CommonMarriageStrategy implements  MeldStrategy{
    @Override
    public int getScore(List<Card> cards, String trumpSuit) {
        return Meld.COMMON_MARRIAGE.getScore();
    }

    @Override
    public List<String> checkCards(List<Card> list, CardManager cardManager, String trumpSuit) {
        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(trumpSuit)) continue;

            String queen = Rank.QUEEN.getRankCardValue() + suit.getSuitShortHand();
            String king = Rank.KING.getRankCardValue() + suit.getSuitShortHand();

            List<String> cardsToCheck = new ArrayList<>(Arrays.asList(queen, king));

            if (cardManager.checkCardInList(list, cardsToCheck)) {
                return cardsToCheck;
            }
        }
        return null;
    }
    @Override
    public Meld getMeldType() {
        return Meld.COMMON_MARRIAGE;
    }
}