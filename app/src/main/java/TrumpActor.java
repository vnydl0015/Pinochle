// Team Number: Wed-16:00-Team-03

import ch.aplu.jgamegrid.Location;

// TrumpActor<T> extends ActorWithLocation<T> to support all trump actors
class TrumpActor<T> extends ActorWithLocation<T> {
    TrumpActor(T trumpActor, Location location) {
        super(trumpActor, location);
    }
}
