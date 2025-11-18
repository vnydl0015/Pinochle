// Team Number: Wed-16:00-Team-03

// Part of State pattern to signify the current melding state.
public class MeldingState implements GameState {

    private final GameSystem system;

    public MeldingState(GameSystem system) {
        this.system = system;
    }

    // perform all tasks in this state
    @Override
    public void enterState() {
        if (Pinochle.cutthroatMode) {
            system.playStrategy.pullTwoAdditionalCardsDuringCutThroat();
            system.playStrategy.extraCardDealing();
            system.playStrategy.cutThroatCardDiscarding();
        }
        system.scoreManager.calculateMeldScore();
    }

    // transit to the next state
    @Override
    public void transitState(GameState state) { system.setState(new TrickTakingState(system)); }
}
