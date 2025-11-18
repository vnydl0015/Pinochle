// Team Number: Wed-16:00-Team-03

import java.util.ArrayList;
import java.util.List;

// Contains all bidding steps by computer player and the strategy it implements for bidding
public class ComputerBid implements BidProcess, ComputerBidStrategy  {

    int computerAutoBidIndex = 0;

    final List<Integer> computerAutoBids = new ArrayList<>();

    private boolean hasComputerPassed = false;

    final GameSystem system;

    static final int COMPUTER_PLAYER_INDEX = 0;

    private boolean hasComputerBid = false;

    boolean bidFirst = false;

    private ComputerBidStrategy computerBidStrategy;

    public ComputerBid(GameSystem system) {
        this.system = system;
        this.computerBidStrategy = new ComputerRandomBidStrategy();
    }

    /*********************** The following codes are for bidding processes *************************/
    // get computer bid generally
    @Override
    public int getBid() { return getComputerBidBasedOnStrategy(this); }

    // check whether computer has not bid
    @Override
    public boolean hasPassed() { return !hasComputerPassed; }

    // set whether computer's bidding period is over
    @Override
    public void setHasPassed(boolean condition) { hasComputerPassed = condition; }

    // set whether computer has bid
    @Override
    public void setHasBid(boolean condition) { hasComputerBid = condition;}

    // bid based on pre-defined file
    @Override
    public void addAutoBid(List<String> bidStrings) {
        computerAutoBids.addAll(bidStrings.stream().map(Integer::parseInt).toList());
    }

    @Override
    public void setBidFirst(boolean condition) {
        bidFirst = condition;
    }

    /*********************** The following codes are for bidding strategy *************************/
    // get computer bid from its strategy
    @Override
    public int getComputerBidBasedOnStrategy(ComputerBid computerBid) {
        if (Pinochle.smartBidMode) { computerBidStrategy = new ComputerSmartBidStrategy(); }
        return computerBidStrategy.getComputerBidBasedOnStrategy(this);
    }
}

