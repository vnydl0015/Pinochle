// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.*;

// Part of State pattern to signify the current initialising state.
public class InitialisingState implements GameState {

    private final GameSystem system;

    public InitialisingState(GameSystem system) {
        this.system = system;
    }

    // perform all tasks during this state
    @Override
    public void enterState() {
        if (Pinochle.additionalMeldMode) system.addMeldStrategies();
        if (Pinochle.isAuto) {
            system.setPlayStrategy();
        } else {
            system.tricktakeManager.setTrickStrategy(new SmartTrickingStrategy());
        }
        system.setScores();
        system.setHands(new Hand[GameSystem.nbPlayers]);
        system.setTrickWinningHands(new Hand[GameSystem.nbPlayers]);
        system.setDeck();
        system.dealer.dealingOut(system);
        system.gameDisplay.drawPlayingLocation(3, GameSystem.playingArea);
        system.sortHand();
        system.addCardListener();
        system.gameDisplay.drawLayout(0, system.hands);
        system.gameDisplay.drawLayout(90, system.trickWinningHands);
    }

    // transit to next state
    @Override
    public void transitState(GameState state) { system.setState(state); }
}
