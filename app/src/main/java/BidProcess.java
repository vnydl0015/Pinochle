// Team Number: Wed-16:00-Team-03

import java.util.List;

// Contains all bidding steps. Implemented by both human and computer players.
public interface BidProcess {
    int getBid();
    boolean hasPassed();
    void setHasPassed(boolean condition);
    void setHasBid(boolean condition);
    void addAutoBid(List<String> bidStrings);
    void setBidFirst(boolean condition);
}
