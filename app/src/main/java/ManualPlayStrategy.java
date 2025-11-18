// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.Random;

// ManualPlayStrategy is part of PlayStrategy pattern, which supports all "manual" processes of the game.
// Only 1 of either AutoPlayStrategy or ManualPlayStrategy can be created in a game play.
public class ManualPlayStrategy implements PlayStrategy {

    private final GameSystem system;

    static public final int seed = 30008;

    static final Random random = new Random(seed);

    public ManualPlayStrategy(GameSystem system) {
        this.system = system;
    }

    /*********************** The following methods support CutThroat gameplay **********************************/
    // flip over 2 cards from top of pile
    @Override
    public void pullTwoAdditionalCardsDuringCutThroat() {
        system.card1 = CardManager.topCard(system.pack.getCardList());
        system.card2 = CardManager.topCard(system.pack.getCardList());
        system.gameDisplay.drawPlayingLocation(2, GameSystem.playingArea);
        system.selected = null;
    }

    // Deal remaining 12 cards to both players
    @Override
    public void extraCardDealing() {
        pickingMiddleAreaCard();
        GameSystem.afterBidding = true;
        Pinochle.delay(Pinochle.delayTime);
        system.dealer.dealExtraDuringCutThroat(system, system.bidWinPlayerIndex);
        system.gameDisplay.drawLayout(0, system.hands);
        Pinochle.delay(Pinochle.delayTime);
    }

    // Pick one of the 2 cards in the middle
    private void pickingMiddleAreaCard() {
        system.pinochle.setStatus("Player " + system.bidWinPlayerIndex + " is playing. Please select 1 card in the middle");
        if (HumanBid.HUMAN_PLAYER_INDEX == system.bidWinPlayerIndex) {
            GameSystem.playingArea.setTouchEnabled(true);
            while (null == system.selected) Pinochle.delay(Pinochle.delayTime);
        } else {
            system.selected = random.nextBoolean() ? system.card1 : system.card2;
        }
        Pinochle.delay(Pinochle.delayTime);
        GameSystem.playingArea.remove(system.selected, true);
        system.hands[system.bidWinPlayerIndex].insert(system.selected, true);
        Pinochle.delay(Pinochle.delayTime);
        Card remainingCard = (system.selected.equals(system.card1)) ? system.card2 : system.card1;
        GameSystem.playingArea.remove(remainingCard, true);
        system.hands[(system.bidWinPlayerIndex + 1) % GameSystem.nbPlayers].insert(remainingCard, true);
    }

    // discarding cards based on clicking
    @Override
    public void cutThroatCardDiscarding() {
        // by computer
        system.pinochle.setStatus("Player 0 is discarding 12 cards");
        system.dealer.discardComputerCardsDuringCutThroat(system);
        GameSystem.afterDiscarding = true;
        // by human
        while (system.hands[HumanBid.HUMAN_PLAYER_INDEX].getCardList().size() > 12) {
            system.pinochle.setStatus("Player 1 is playing. Please discard 12 cards");
            system.hands[HumanBid.HUMAN_PLAYER_INDEX].setTouchEnabled(true);
            do Pinochle.delay(Pinochle.delayTime);
            while (null == system.selected);
            system.hands[HumanBid.HUMAN_PLAYER_INDEX].remove(system.selected, true);
            system.pack.insert(system.selected, false);
        }
    }

    /*********************** The following methods support Trick Taking gameplay **********************************/
    @Override
    public void trickTaking(int nextPlayer) {
        if (HumanBid.HUMAN_PLAYER_INDEX == nextPlayer) {
            system.hands[HumanBid.HUMAN_PLAYER_INDEX].setTouchEnabled(true);
            system.pinochle.setStatus("Player " + nextPlayer + " is playing. Please double click on a card to discard");
            system.selected = null;
            while (null == system.selected) Pinochle.delay(Pinochle.delayTime);
            system.hands[HumanBid.HUMAN_PLAYER_INDEX].remove(system.selected, true);
        } else {
            system.pinochle.setStatusText("Player " + nextPlayer + " thinking...");
            system.tricktakeManager.computerTricking(system, nextPlayer);
            system.hands[ComputerBid.COMPUTER_PLAYER_INDEX].remove(system.selected, true);
            Pinochle.delay(Pinochle.delayTime);
        }
    }
}
