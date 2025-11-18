// Team Number: Wed-16:00-Team-03

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;

// ActorFactory uses a Factory pattern to create both bid and trump actors.
// Both actors can be either TextActor and GGButton.
// Therefore, a generic (with wildcard (?)) is used to support this parametric polymorphism.
// Parametric polymorphism allows object of multiple types to be created from the same code.
// Wildcard is used to reduce restriction on the actor type. https://www.geeksforgeeks.org/wildcards-in-java/
public class ActorFactory {

    Font smallFont = new Font("Arial", Font.BOLD, 18);

    private final GameSystem system;

    public ActorFactory(GameSystem system) {
        this.system = system;
    }

    // Create all trump actors
    TrumpActor<? extends Actor> createTrumpActor(String actor) {
        switch (actor) {
            case "trumpInstructionActor" -> {
                return new TrumpActor<>( new TextActor("Trump Selection", Color.white, system.pinochle.bgColor,
                        smallFont),
                        new Location(550, 80));
            }
            case "clubTrumpActor" -> {
                return new TrumpActor<>(new GGButton("sprites/clubs_item.png", false),
                        new Location(580, 100));
            }
            case "spadeTrumpActor" -> {
                return new TrumpActor<>(new GGButton("sprites/spades_item.png", false),
                        new Location(610, 100));
            }
            case "diamondTrumpActor" -> {
                return new TrumpActor<>(new GGButton("sprites/diamonds_item.png", false),
                        new Location(640, 100));
            }
            case "heartTrumpActor" -> {
                return new TrumpActor<>(new GGButton("sprites/hearts_item.png", false),
                        new Location(670, 100));
            }
        }
        return null;
    }

    // Create all bid actors
    BidActor<?> createBidActor(String actor) {
        switch (actor) {
            case "playerBidActor" -> {
                return new BidActor<>(new TextActor("Bidding: ", Color.white, Color.black, smallFont),
                        new Location(550, 30));
            }
            case "currentBidActor" -> {
                return new BidActor<>(new TextActor("Current Bidding", Color.white, Color.black, smallFont),
                        new Location(550, 50));
            }
            case "newBidActor" -> {
                return new BidActor<>(new TextActor("New Bid: ", Color.white, Color.black, smallFont),
                        new Location(550, 75));
            }
            case "bidSelectionActor" -> {
                return new BidActor<>(new GGButton("sprites/bid_10.gif", false),
                        new Location(600, 100));
            }
            case "bidConfirmActor" -> {
                return new BidActor<>(new GGButton("sprites/done30.gif", false),
                        new Location(660, 100));
            }
            case "bidPassActor" -> {
                return new BidActor<>(new GGButton("sprites/bid_pass.gif", false),
                        new Location(630, 150));
            }
        }
        return null;
    }
}
