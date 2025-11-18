// Team Number: Wed-16:00-Team-03

// Keep track of the entire game's state
public interface GameState {
    void enterState();
    void transitState(GameState state);
}
