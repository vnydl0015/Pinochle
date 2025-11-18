// Team Number: Wed-16:00-Team-03

import ch.aplu.jgamegrid.*;

import java.util.*;
import java.util.List;

// TrumpUIController extends from UIController to perform trump buttons-related tasks
public class TrumpUIController extends UIController {

    private List<TrumpActor<?>> allButtonActors = new ArrayList<>();

    private List<TrumpActor<?>> allTextActors = new ArrayList<>();

    private TrumpActor<?> trumpInstructionActor;

    private TrumpActor<?> clubTrumpActor;

    private TrumpActor<?> spadeTrumpActor;

    private TrumpActor<?> diamondTrumpActor;

    private TrumpActor<?> heartTrumpActor;

    final Location trumpLocation = new Location(620, 120);

    private final Map<String, String> trumpImages = new HashMap<>(Map.of(
            Suit.SPADES.getSuitShortHand(), "sprites/bigspade.gif",
            Suit.CLUBS.getSuitShortHand(), "sprites/bigclub.gif",
            Suit.DIAMONDS.getSuitShortHand(), "sprites/bigdiamond.gif",
            Suit.HEARTS.getSuitShortHand(), "sprites/bigheart.gif"));

    public TrumpUIController(GameSystem system) { super(system, new ActorFactory(system)); }

    // creates all buttons and add them to list
    @Override
    public void create() {
        trumpInstructionActor = factory.createTrumpActor("trumpInstructionActor");
        clubTrumpActor = factory.createTrumpActor("clubTrumpActor");
        spadeTrumpActor = factory.createTrumpActor("spadeTrumpActor");
        diamondTrumpActor = factory.createTrumpActor("diamondTrumpActor");
        heartTrumpActor = factory.createTrumpActor("heartTrumpActor");
        allTextActors.add(trumpInstructionActor);
        allButtonActors.add(clubTrumpActor);
        allButtonActors.add(spadeTrumpActor);
        allButtonActors.add(diamondTrumpActor);
        allButtonActors.add(heartTrumpActor);
    }

    // update trump actor
    private void updateTrumpActor() {
        String trumpImage = trumpImages.get(GameSystem.trumpSuit);
        Actor trumpActor = new Actor(trumpImage);
        system.pinochle.addActor(trumpActor, trumpLocation);
    }

    // ask for click
    @Override
    public void askFor() {
        if (Pinochle.isAuto) {
            GameSystem.trumpSuit = system.pinochle.properties.getProperty("players.trump", "C");
            updateTrumpActor();
            return;
        }

        system.pinochle.addActor((Actor) trumpInstructionActor.actor, trumpInstructionActor.location);
        // set Trump Suit
        if (system.bidWinPlayerIndex == ComputerBid.COMPUTER_PLAYER_INDEX) {
            Suit selectedTrumpSuit = Arrays.stream(Suit.values()).findAny().get();
            GameSystem.trumpSuit = selectedTrumpSuit.getSuitShortHand();
        } else {
            add();
            GGButtonListener buttonListener = new GGButtonListener() {
                @Override
                public void buttonPressed(GGButton ggButton) {
                    if (ggButton.equals(clubTrumpActor.actor)) {
                        GameSystem.trumpSuit = Suit.CLUBS.getSuitShortHand();
                    } else if (ggButton.equals(spadeTrumpActor.actor)) {
                        GameSystem.trumpSuit = Suit.SPADES.getSuitShortHand();
                    } else if (ggButton.equals(heartTrumpActor.actor)) {
                        GameSystem.trumpSuit = Suit.HEARTS.getSuitShortHand();
                    } else if (ggButton.equals(diamondTrumpActor.actor)) {
                        GameSystem.trumpSuit = Suit.DIAMONDS.getSuitShortHand();
                    }
                }
                @Override
                public void buttonReleased(GGButton ggButton) {
                }
                @Override
                public void buttonClicked(GGButton ggButton) {
                }
            };
            ((GGButton) clubTrumpActor.actor).addButtonListener(buttonListener);
            ((GGButton) spadeTrumpActor.actor).addButtonListener(buttonListener);
            ((GGButton) heartTrumpActor.actor).addButtonListener(buttonListener);
            ((GGButton) diamondTrumpActor.actor).addButtonListener(buttonListener);

            while (GameSystem.trumpSuit == null) Pinochle.delay(Pinochle.delayTime);
        }
        remove();
        updateTrumpActor();
    }

    // register actors
    @Override
    public void add() {
        for (TrumpActor<?> actor: allButtonActors) { system.pinochle.addActor((GGButton) actor.actor, actor.location); }
    }

    // remove actors
    @Override
    public void remove() {
        for (TrumpActor<?> actor: allButtonActors) { system.pinochle.removeActor((Actor) actor.actor); }
    }
}
