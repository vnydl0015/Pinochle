// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.util.*;
import java.util.List;

// Pinochle acts as a facade that hides complexity of the game.
// It is closely related to Game System, which knows all the complex logic behind the game.
@SuppressWarnings("serial")
public class Pinochle extends CardGame {

    final Properties properties;

    private final String version = "1.0";

    static int thinkingTime;

    static int delayTime;

    private final GameSystem system;

    static boolean smartTrick;

    static boolean additionalMeldMode;

    static boolean smartBidMode;

    static boolean cutthroatMode;

    static boolean isAuto;

    // initialising
    public Pinochle(Properties properties) {
        super(700, 700, 30);
        this.system = new GameSystem(this);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto", "false"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "200"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "50"));
        additionalMeldMode = Boolean.parseBoolean(properties.getProperty("melds.additional", "true"));
        smartBidMode = Boolean.parseBoolean(properties.getProperty("players.0.smartbids", "true"));
        cutthroatMode = Boolean.parseBoolean(properties.getProperty("mode.cutthroat", "true"));
        smartTrick = Boolean.parseBoolean(properties.getProperty("mode.smarttrick", "true"));
    }

    // run the entire game
    public String runApp() {
        // setting up texts
        setTitle("Pinochle  (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        system.setupPlayerAutoMovements(properties.getProperty("players.0.cardsPlayed"),
                properties.getProperty("players.1.cardsPlayed"));
        // play game
        system.state.enterState(); // start initialisation
        system.state.transitState(new BiddingState(system)); // transit to bidding
        system.state.enterState();
        system.state.transitState(new MeldingState(system)); // transit to meld + cutthroat (if any)
        system.state.enterState();
        system.state.transitState(new TrickTakingState(system)); // transit to trick taking
        system.state.enterState();
        system.state.transitState(new ScoringState(system)); // transit to scoring
        system.state.enterState();
        // get list of winners
        List<Integer> winners = system.updateScore();
        String winText = system.getWinner(winners);
        addActor(new Actor("sprites/gameover.gif"), GameDisplay.textLocation);
        setStatusText(winText);
        refresh();
        return system.logger.addEndOfGameToLog(winners, system.scoreManager.scores, system.trickWinningHands);
    }

    public void setStatus(String string) { setStatusText(string); }
}