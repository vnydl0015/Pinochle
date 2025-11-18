// Team Number: Wed-16:00-Team-03

// Computer trick smartly
public class SmartTrickingStrategy implements TrickStrategy {

    @Override
    public void computerTricking(GameSystem system, int nextPlayer) {
        system.selected = system.cardManager.getSmartCardForHand(system.hands[nextPlayer]);
    }
}
