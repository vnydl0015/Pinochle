// Team Number: Wed-16:00-Team-03

import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Managing bids made by both human and computer players
public class BidManager {

    private static final String RANDOM_BID = "random";

    private static final String COMPUTER_BID = "computer";

    private static final String HUMAN_BID = "human";

    private final GameSystem system;

    final BidProcess computerBid;

    final BidProcess humanBid;

    private final BidUIController bidUIController;

    static final int BID_SELECTION_VALUE = 10;

    static final int MAX_SINGLE_BID = 20;

    public BidManager(GameSystem system, BidUIController bidUIController) {
        this.system = system;
        this.computerBid = new ComputerBid(system);
        this.humanBid = new HumanBid(system);
        this.bidUIController = bidUIController;
    }

    // Ask for bids from both human and computer players
    private void askForBidForPlayerIndex(int playerIndex) {
        int bidValue;
        // for computer player bidding
        if (playerIndex == ComputerBid.COMPUTER_PLAYER_INDEX) {
            bidValue = computerBid.getBid();
            bidUIController.updateBidText(playerIndex, system.currentBid + bidValue);
            Pinochle.delay(Pinochle.thinkingTime);
            // if no bid is made, return
            if (bidValue == 0) {
                computerBid.setHasPassed(true);
                humanBid.setHasBid(false);
                return;
            }
            // update bid value
            system.currentBid += bidValue;
            bidUIController.updateBidText(playerIndex, 0);
            humanBid.setHasBid(false); 
        } else {
            // for human player bidding
            bidUIController.displayBidButtons(true);
            bidUIController.updateBidText(playerIndex, 0);
            bidValue = humanBid.getBid();
            humanBid.setHasBid(true);
        }
    }

    // Extract auto bid from properties file
    void extractAutoBid(String playerBidString, int player) {
        if (playerBidString == null || playerBidString.isEmpty()) { return; }
        List<String> bidStrings = Arrays.asList(playerBidString.split(","));
        if (player == HumanBid.HUMAN_PLAYER_INDEX) {
            system.bidManager.humanBid.addAutoBid(bidStrings);
        } else {
            system.bidManager.computerBid.addAutoBid(bidStrings);
        }
    }

    // Keep track of all bids by both human and computer players
    void bidding() {
        // extract auto bids
        String bidOrder = system.pinochle.properties.getProperty("players.bid_first", "random");
        extractAutoBid(system.pinochle.properties.getProperty("players.1.bids", ""),
                HumanBid.HUMAN_PLAYER_INDEX);
        extractAutoBid(system.pinochle.properties.getProperty("players.0.bids", ""),
                ComputerBid.COMPUTER_PLAYER_INDEX);

        // Determine the first bidder
        boolean isContinueBidding = true;
        ((BidUIController) system.bidUIController).updateBidText(-1, 0);
        Random rand = new Random(1);
        int playerIndex = 0;
        switch (bidOrder) {
            case RANDOM_BID:
                playerIndex = rand.nextInt(GameSystem.nbPlayers);
                if (playerIndex == ComputerBid.COMPUTER_PLAYER_INDEX) {
                    computerBid.setBidFirst(true);
                } else {
                    humanBid.setBidFirst(true);
                }
                break;
            case COMPUTER_BID:
                computerBid.setBidFirst(true);
                break;
            case HUMAN_BID:
                playerIndex = HumanBid.HUMAN_PLAYER_INDEX;
                humanBid.setBidFirst(true);
                break;
        }

        // continuously bidding between players
        do {
            for (int i = 0; i < GameSystem.nbPlayers; i++) {
                askForBidForPlayerIndex(playerIndex);
                playerIndex = (playerIndex + 1) % GameSystem.nbPlayers;
                isContinueBidding = humanBid.hasPassed() && computerBid.hasPassed();
                if (!isContinueBidding) {
                    system.bidWinPlayerIndex = playerIndex;
                    break;
                }
            }
        } while (isContinueBidding);
    }

    // return either 10 or 20
    int getBidNumber(boolean condition) { return (condition) ? MAX_SINGLE_BID : BID_SELECTION_VALUE; }
}
