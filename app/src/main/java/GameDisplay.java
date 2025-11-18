// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.jgamegrid.Location;

// Responsible for displaying GUI
public class GameDisplay {

    static final int handWidth = 400;

    private final GameSystem system;

    public GameDisplay(GameSystem system) {
        this.system = system;
    }

    static final Location playingLocation = new Location(350, 350);

    static final Location textLocation = new Location(350, 450);

    static final Location[] handLocations = {
            new Location(350, 625),
            new Location(350, 75),
    };
    static final Location[] trickHandLocations = {
            new Location(75, 350),
            new Location(625, 350)
    };

    // drawing out the middle area of the screen
    void drawPlayingLocation(int width, Hand playingArea) {
        int trickWidth = 40;
        playingArea.setView(system.pinochle,
                new RowLayout(playingLocation, (playingArea.getNumberOfCards() + width) * trickWidth));
        playingArea.draw();
    }

    // drawing out all areas (except middle) of the screen
    void drawLayout(int angle, Hand[] hands) {
        RowLayout[] layouts = new RowLayout[GameSystem.nbPlayers];
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            Location[] locations = (angle == 0) ? handLocations: trickHandLocations;
            layouts[i] = new RowLayout(locations[i], handWidth);
            layouts[i].setRotationAngle((angle + 180) * i);
            hands[i].setView(system.pinochle, layouts[i]);
            if (angle == 0) hands[i].setTargetArea(new TargetArea(playingLocation));
            hands[i].draw();
        }
    }

    // display cards transferring to winner
    void transferCardsToWinner(int trickWinPlayerIndex) {
        for (Card card : GameSystem.playingArea.getCardList()) {
            system.trickWinningHands[trickWinPlayerIndex].insert(card, true);
        }
        GameSystem.playingArea.removeAll(true);
        RowLayout[] trickHandLayouts = new RowLayout[GameSystem.nbPlayers];
        Pinochle.delay(Pinochle.delayTime);
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            trickHandLayouts[i] = new RowLayout(GameDisplay.trickHandLocations[i], GameDisplay.handWidth);
            trickHandLayouts[i].setRotationAngle(90);
            system.trickWinningHands[i].setView(system.pinochle, trickHandLayouts[i]);
            system.trickWinningHands[i].draw();
        }
        Pinochle.delay(Pinochle.delayTime);
    }

}
