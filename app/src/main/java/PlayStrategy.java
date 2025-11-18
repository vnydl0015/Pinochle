// Team Number: Wed-16:00-Team-03

// PlayStrategy dictates whether it is an auto game or manual game
public interface PlayStrategy {
    // for cutthroat
    void pullTwoAdditionalCardsDuringCutThroat();
    void extraCardDealing();
    void cutThroatCardDiscarding();
    // for trick taking
    void trickTaking(int nextPlayer);
}
