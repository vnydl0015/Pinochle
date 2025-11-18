// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.List;
import java.util.Map;

// Has all computer's smart bidding behaviour
public class ComputerSmartBidStrategy implements ComputerBidStrategy {

    // compute computer's smart bid based on rules
    @Override
    public int getComputerBidBasedOnStrategy(ComputerBid computerBid) {
        List<Card> cards = computerBid.system.hands[ComputerBid.COMPUTER_PLAYER_INDEX].getCardList();

        // get suit with the greatest count
        Map<Suit, Integer> majorSuitMap = computerBid.system.cardManager.getMajorSuitAndCount(cards);
        Suit majorSuit = majorSuitMap.keySet().iterator().next();
        Integer majorSuitCount = majorSuitMap.values().iterator().next();

        // get meld scores
        int meldScore = computerBid.system.scoreManager.scoringComposite.getScore(cards, majorSuit.getSuitShortHand());
        if (computerBid.bidFirst) {
            computerBid.bidFirst = false;
            return meldScore;
        }
        // get max values of either major suit or high rank suit
        int maxValues = computerBid.system.cardManager.getMaxValues(cards, majorSuit);
        int conditionValue = 6;
        int plannedIncrease = computerBid.system.bidManager.getBidNumber(majorSuitCount >= conditionValue);
        return ((plannedIncrease + computerBid.system.currentBid) < (maxValues + meldScore)) ? plannedIncrease : 0;
    }

}
