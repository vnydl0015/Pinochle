// Team Number: Wed-16:00-Team-03

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

// ActorWithLocation<T> have the properties (actor and its location) of all bid/trump actors.
// It is extended by BidActor<T> and TrumpActor<T> to minimise code duplication and support future Temple pattern.
// Reason why they are grouped together into 1 file: these classes are small and closely related
abstract class ActorWithLocation<T> extends Actor {

    T actor;
    Location location;

    ActorWithLocation(T actor, Location location) {
        this.actor = actor;
        this.location = location;
    }

    // setter
    protected void setBidActor(T actor) {
        this.actor = actor;
    }
}


