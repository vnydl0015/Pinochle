// Team Number: Wed-16:00-Team-03

import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;

import java.util.ArrayList;
import java.util.List;

// Contains all bidding steps by human player and the strategy it implements for bidding
public class HumanBid implements BidProcess {

    static final int HUMAN_PLAYER_INDEX = 1;

    private boolean hasHumanPassed = false;

    private final List<Integer> humanAutoBids = new ArrayList<>();

    private int humanAutoBidIndex = 0;

    private final GameSystem system;

    private int humanBid = 0;

    private boolean hasHumanBid = false;

    private boolean bidFirst = false;

    public HumanBid(GameSystem system) {
        this.system = system;
    }

    /*********************** The following codes are for bidding processes *************************/
    // get human bid
    @Override
    public int getBid() {
        if (Pinochle.isAuto && humanAutoBids != null && humanAutoBidIndex < humanAutoBids.size()) {
            humanBid = humanAutoBids.get(humanAutoBidIndex);
            system.currentBid = system.currentBid + humanBid;
            humanAutoBidIndex++;
            if (humanBid == 0) { hasHumanPassed = true; }
            ((BidUIController) system.bidUIController).updateBidText(HUMAN_PLAYER_INDEX, system.currentBid);
        } else {
            while (!hasHumanBid && !hasHumanPassed) {
                Pinochle.delay(Pinochle.delayTime);
            }
        }
        return 0;
    }

    // set whether human has bid
    @Override
    public void setBidFirst(boolean condition) { this.bidFirst = condition; }

    // check whether human has not bid
    @Override
    public boolean hasPassed() { return !hasHumanPassed; }

    // listens to human player bidding in manual mode
    void updateHumanBidListener(GGButton bidSelectionActor, GGButton bidConfirmActor, GGButton bidPassActor) {
        system.bidManager.computerBid.setHasPassed(false);
        bidSelectionActor.addButtonListener(new GGButtonListener() {
            @Override
            public void buttonPressed(GGButton ggButton) {
                hasHumanBid = false;
                if (humanBid >= BidManager.MAX_SINGLE_BID) {
                    bidSelectionActor.setActEnabled(false);
                    system.pinochle.setStatus("Maximum amount of a single bid reached");
                } else {
                    humanBid += BidManager.BID_SELECTION_VALUE;
                }
                ((BidUIController) system.bidUIController).updateBidText(HumanBid.HUMAN_PLAYER_INDEX,
                        humanBid + system.currentBid);
            }
            @Override
            public void buttonReleased(GGButton ggButton) {}
            @Override
            public void buttonClicked(GGButton ggButton) {}
        });

        bidConfirmActor.addButtonListener(new GGButtonListener() {
            @Override
            public void buttonPressed(GGButton ggButton) {
                system.currentBid = system.currentBid + humanBid;
                hasHumanBid = true;
                humanBid = 0;
                ((BidUIController) system.bidUIController).updateBidText(HUMAN_PLAYER_INDEX, system.currentBid);
                system.pinochle.setStatus("");
            }
            @Override
            public void buttonReleased(GGButton ggButton) {}
            @Override
            public void buttonClicked(GGButton ggButton) {}
        });

        bidPassActor.addButtonListener(new GGButtonListener() {
            @Override
            public void buttonPressed(GGButton ggButton) {
                ((BidUIController) system.bidUIController).updateBidText(HUMAN_PLAYER_INDEX, 0);
                humanBid = 0;
                hasHumanPassed = true;
                system.pinochle.setStatus("");
            }
            @Override
            public void buttonReleased(GGButton ggButton) {}
            @Override
            public void buttonClicked(GGButton ggButton) {}
        });
    }

    // set whether human's bidding period is over
    @Override
    public void setHasPassed(boolean condition) {
        hasHumanPassed = condition;
    }

    // set whether human has bid
    @Override
    public void setHasBid(boolean condition) {
        hasHumanBid = condition;
    }

    // bid based on pre-defined file
    @Override
    public void addAutoBid(List<String> bidStrings) {
        humanAutoBids.addAll(bidStrings.stream().map(Integer::parseInt).toList());
    }
}
