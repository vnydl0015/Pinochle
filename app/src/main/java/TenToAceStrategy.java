// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.List;

class TenToAceStrategy implements MeldStrategy{
    @Override
    public int getScore(List<Card> cards, String trumpSuit) {
        return Meld.TEN_TO_ACE_RUN.getScore();
    }

    @Override
    public List<String> checkCards(List<Card> list, CardManager cardManager, String trumpSuit) {
        List<String> cardsToCheck = getTenToAceCards(trumpSuit);
        if (cardManager.checkCardInList(list, cardsToCheck)) {
            return cardsToCheck;
        }
        return null;
    }

    @Override
    public Meld getMeldType() {
        return Meld.TEN_TO_ACE_RUN;
    }
}