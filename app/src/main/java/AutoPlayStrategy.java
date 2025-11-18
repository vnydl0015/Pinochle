// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Hand;

import java.util.List;

// AutoPlayStrategy is part of PlayStrategy pattern, which supports all "auto" processes of the game.
// Only 1 of either AutoPlayStrategy or ManualPlayStrategy can be created in a game play.
public class AutoPlayStrategy implements PlayStrategy {

    private final GameSystem system;

    public AutoPlayStrategy(GameSystem system) {
        this.system = system;
    }

    /*********************** The following methods support CutThroat gameplay **********************************/
    @Override
    public void pullTwoAdditionalCardsDuringCutThroat() {}

    // Deal remaining 12 cards to both players
    @Override
    public void extraCardDealing() {
        system.dealer.dealExtraDuringCutThroatAuto(system);
        Pinochle.delay(Pinochle.delayTime);
    }

    // Discarding cards
    @Override
    public void cutThroatCardDiscarding() {
        // by human
        system.dealer.discardHumanCardsDuringCutThroat(system);
        Pinochle.delay(Pinochle.delayTime);
        // by computer
        system.dealer.discardComputerCardsDuringCutThroat(system);
        Pinochle.delay(Pinochle.delayTime);
    }

    /*********************** The following methods support TrickTaking gameplay **********************************/
    // Perform TrickTaking via: (1) Pre-defined moves in playerAutoMovements (2) Random card draw.
    // autoIndexHands store winners for each round
    @Override
    public void trickTaking(int nextPlayer) {
        int nextPlayerAutoIndex = system.autoIndexHands[nextPlayer];
        List<String> nextPlayerMovement = system.playerAutoMovements.get(nextPlayer);
        String nextMovement = "";

        // (1) Pre-defined moves in playerAutoMovements
        if (nextPlayerMovement.size() > nextPlayerAutoIndex && !nextPlayerMovement.equals("")) {
            nextMovement = nextPlayerMovement.get(nextPlayerAutoIndex);
            nextPlayerAutoIndex++;
            system.autoIndexHands[nextPlayer] = nextPlayerAutoIndex;
            Hand nextHand = system.hands[nextPlayer];

            // Apply movement for player
            system.selected = system.cardManager.applyAutoMovement(nextHand, nextMovement);
            Pinochle.delay(Pinochle.delayTime);
            if (system.selected != null) {
                system.selected.removeFromHand(true);
            } else {
                getRandomCardAuto(nextPlayer);
            }
        } else {
            // (2) Random card draw
            getRandomCardAuto(nextPlayer);
        }
    }

    // Get random card from stockpile
    private void getRandomCardAuto(int nextPlayer) {
        system.selected = system.cardManager.getRandomCardForHand(system.hands[nextPlayer]);
        system.selected.removeFromHand(true);
    }
}
