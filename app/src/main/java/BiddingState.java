// Team Number: Wed-16:00-Team-03

// Part of State pattern to signify the current bidding state.
public class BiddingState implements GameState {

    private final GameSystem system;

    public BiddingState(GameSystem system) {
        this.system = system;
    }

    // Bidding state involves bidding between players and updating the button listener
    @Override
    public void enterState() {
        system.bidUIController.process();
        system.trumpController.process();
    }

    // Transits to the next state
    @Override
    public void transitState(GameState state) {
        system.setState(new MeldingState(system));
    }
}
