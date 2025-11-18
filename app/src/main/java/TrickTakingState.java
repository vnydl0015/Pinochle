// Team Number: Wed-16:00-Team-03

// Part of State pattern to signify the current trick taking state.
public class TrickTakingState implements GameState {

    private final GameSystem system;

    public TrickTakingState(GameSystem system) {
        this.system = system;
    }

    @Override
    public void enterState() { system.trickTakingBetweenPlayers(); }

    @Override
    public void transitState(GameState state) {
        system.setState(new ScoringState(system));
    }
}
