// Team Number: Wed-16:00-Team-03

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;
import java.util.*;
import java.util.List;

// BidUIController extends from UIController to perform bid buttons-related tasks
public class BidUIController extends UIController {

    private List<BidActor<?>> allBidActors = new ArrayList<>();

    private List<BidActor<?>> allButtonActors = new ArrayList<>();

    private List<BidActor<?>> allTextActors = new ArrayList<>();

    // Text Actors
    private BidActor<?> playerBidActor;

    private BidActor<?> currentBidActor;

    private BidActor<?> newBidActor;

    // GGButton Actors
    private BidActor<?> bidSelectionActor;

    private BidActor<?> bidConfirmActor;

    private BidActor<?> bidPassActor;

    private final Font smallFont = new Font("Arial", Font.BOLD, 18);

    private final GameSystem system;

    public BidUIController(GameSystem system) {
        super(system, new ActorFactory(system));
        this.system = system;
    }

    // create all actors and add to list
    @Override
    public void create() {
        // create bid actors
        playerBidActor = factory.createBidActor("playerBidActor");
        currentBidActor = factory.createBidActor("currentBidActor");
        newBidActor = factory.createBidActor("newBidActor");
        bidSelectionActor = factory.createBidActor("bidSelectionActor");
        bidConfirmActor = factory.createBidActor("bidConfirmActor");
        bidPassActor = factory.createBidActor("bidPassActor");
        // add bid actors to list
        allBidActors.add(playerBidActor);
        allBidActors.add(currentBidActor);
        allBidActors.add(newBidActor);
        allBidActors.add(bidSelectionActor);
        allBidActors.add(bidConfirmActor);
        allBidActors.add(bidPassActor);
        allButtonActors.add(bidSelectionActor);
        allButtonActors.add(bidConfirmActor);
        allButtonActors.add(bidPassActor);
        allTextActors.add(playerBidActor);
        allTextActors.add(currentBidActor);
        allTextActors.add(newBidActor);
    }

    // update bid actors
    @SuppressWarnings("unchecked")
    private void updateBidActor(int currentBid) {
        ((BidActor<TextActor>) currentBidActor).setBidActor(new TextActor("Current Bid: " + currentBid,
                Color.WHITE, system.pinochle.bgColor, smallFont));
    }
    @SuppressWarnings("unchecked")
    private void updateBidActor(String actorType, String bidString) {
        if (actorType.equals("newBidActor")) {
            ((BidActor<TextActor>) newBidActor).setBidActor(new TextActor("New Bid: " + bidString,
                    Color.WHITE, system.pinochle.bgColor, smallFont));
        } else if (actorType.equals("playerBidActor")) {
            ((BidActor<TextActor>) playerBidActor).setBidActor(new TextActor(bidString, Color.WHITE,
                    system.pinochle.bgColor, smallFont));
        }
    }

    // update text actors
    void updateBidText(int playerIndex, int newBid) {
        String playerBidString = switch (playerIndex) {
            case -1 -> "Bid";
            case 0 -> "Computer Bid";
            case 1 -> "Human Bid";
            default -> "";
        };
        removeBidText();
        updateBidActor(system.currentBid);
        String newBidString = newBid == 0 ? "" : String.valueOf(newBid);
        updateBidActor("newBidActor", newBidString);
        updateBidActor("playerBidActor", playerBidString);
        add();
    }

    // update bid result
    private void updateBidResult() {
        system.pinochle.removeActor((TextActor) playerBidActor.actor);
        system.pinochle.removeActor((TextActor) currentBidActor.actor);
        updateBidActor(system.currentBid);
        system.pinochle.addActor((TextActor) currentBidActor.actor, currentBidActor.location);
        String playerBidString = system.bidWinPlayerIndex == ComputerBid.COMPUTER_PLAYER_INDEX ? "Computer Win" : "Human Win";
        updateBidActor("playerBidActor", playerBidString);
        system.pinochle.addActor((TextActor) playerBidActor.actor, playerBidActor.location);
    }

    // remove actors
    @Override
    public void remove() {
        for (BidActor<?> actor: allButtonActors) { system.pinochle.removeActor((GGButton) actor.actor); }
        system.pinochle.removeActor((TextActor) newBidActor.actor);
    }
    private void removeBidText() {
        for (BidActor<?> actor: allTextActors) { system.pinochle.removeActor((TextActor) actor.actor); }
    }

    // add actors
    @Override
    public void add() {
        for (BidActor<?> actor: allTextActors) { system.pinochle.addActor((TextActor) actor.actor, actor.location); }
    }

    // display actors
    void displayBidButtons(boolean isShown) {
        for (BidActor<?> actor: allButtonActors) { ((GGButton) actor.actor).setActEnabled(isShown); }
    }

    // 'ask-for' click
    @Override
    public void askFor() {
        initBidsUI();
        displayBidButtons(false);
        system.bidManager.bidding();
        remove();
        updateBidResult();
        system.logger.addBidInfoToLog(system.bidWinPlayerIndex, system.currentBid);
    }

    // initialise actors
    private void initBidsUI() {
        for (BidActor<?> actor: allBidActors) { system.pinochle.addActor((Actor) actor.actor, actor.location); }
        for (BidActor<?> actor: allButtonActors) {
            system.pinochle.setActorOnTop((GGButton) actor.actor);
            ((GGButton)actor.actor).setActEnabled(false);
        }
        ((HumanBid)system.bidManager.humanBid).updateHumanBidListener((GGButton)
                bidSelectionActor.actor, (GGButton) bidConfirmActor.actor, (GGButton) bidPassActor.actor);
    }
}
