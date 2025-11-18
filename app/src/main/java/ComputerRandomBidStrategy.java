// Team Number: Wed-16:00-Team-03

import java.util.Random;

// Has all computer's random bidding behaviour
public class ComputerRandomBidStrategy implements ComputerBidStrategy {

    // Compute computer's random bid
    @Override
    public int getComputerBidBasedOnStrategy(ComputerBid computerBid) {
        int bidValue;
        // pre-defined random bids
        if (Pinochle.isAuto && computerBid.computerAutoBids != null && computerBid.computerAutoBidIndex < computerBid.computerAutoBids.size()) {
            bidValue = computerBid.computerAutoBids.get(computerBid.computerAutoBidIndex);
            computerBid.computerAutoBidIndex++;
        } else {
            // random bids
            Random random = new Random();
            int randomBidBase = random.nextInt(3);
            bidValue = randomBidBase * 10;
        }
        return bidValue;
    }
}
