// Team Number: Wed-16:00-Team-03

// Computer trick at random
public class RandomTrickingStrategy implements TrickStrategy {

    @Override
    public void computerTricking(GameSystem system, int nextPlayer) {
        system.selected = system.cardManager.getRandomCardForHand(system.hands[nextPlayer]);
    }
}
