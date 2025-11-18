// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.List;

class TrumpNineStrategy implements MeldStrategy{

    @Override
    public int getScore(List<Card> cards, String trumpSuit) {
        return Meld.TRUMP_NINE.getScore();
    }

    @Override
    public List<String> checkCards(List<Card> list, CardManager cardManager, String trumpSuit) {
        List<String> cardsToCheck = new ArrayList<>(List.of(
                Rank.NINE.getRankCardValue() + trumpSuit));
        if (cardManager.checkCardInList(list, cardsToCheck)) {
            return cardsToCheck;
        }
        return null;
    }

    @Override
    public Meld getMeldType() {
        return Meld.TRUMP_NINE;
    }
}