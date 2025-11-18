// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.Arrays;
import java.util.List;

// An interface that supports Meld's Strategy pattern
public interface MeldStrategy {

    int getScore(List<Card> cards, String trumpSuit);

    List<String> checkCards(List<Card> list, CardManager cardManager, String trumpSuit);

    Meld getMeldType();

    default List<String> getTenToAceCards(String trumpSuit) {
        return Arrays.asList(
                Rank.ACE.getRankCardValue() + trumpSuit,
                Rank.JACK.getRankCardValue() + trumpSuit,
                Rank.QUEEN.getRankCardValue() + trumpSuit,
                Rank.KING.getRankCardValue() + trumpSuit,
                Rank.TEN.getRankCardValue() + trumpSuit);
    }
}