// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// An abstract composite class that implements meld strategy to support total meld calculations.
public abstract class CompositeMeldStrategy implements MeldStrategy {

    protected final List<MeldStrategy> strategies;

    CompositeMeldStrategy(List<MeldStrategy> strategies) {
        this.strategies = strategies;
    }

    void addStrategies() {
        this.strategies.addAll(new ArrayList<>(List.of(
                new DoubleRunStrategy(),
                new JackAboundStrategy(),
                new DoublePinochleStrategy(),
                new AceRoyalMarriageStrategy(),
                new AceAroundStrategy(),
                new PinochleStrategy(),
                new CommonMarriageStrategy()
        )));
        sortMelds(strategies);
    }

    // sort meld in the array based on their order in Meld enum.
    private void sortMelds(List<MeldStrategy> strategies) {
        strategies.sort(Comparator.comparing(
                strategy -> strategy.getMeldType().ordinal()
        ));
    }

    public abstract int getScore(List<Card> cards, String trumpSuit);
}