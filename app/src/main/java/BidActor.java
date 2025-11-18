// Team Number: Wed-16:00-Team-03

import ch.aplu.jgamegrid.Location;

// BidActor<T> extends ActorWithLocation<T> to support all bidding actors
class BidActor<T> extends ActorWithLocation<T> {
    BidActor(T bidActor, Location location) {
        super(bidActor, location);
    }
}