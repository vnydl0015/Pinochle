// Team Number: Wed-16:00-Team-03

// Part of State pattern to signify the current scoring state.
public class ScoringState implements GameState{

    private final GameSystem system;

    public ScoringState(GameSystem system) {
        this.system = system;
    }

    // perform all tasks for this state
    @Override
    public void enterState() {
        system.scoreManager.updateTrickScore();
    }

    // transit to the next state
    @Override
    public void transitState(GameState state) {}
}
