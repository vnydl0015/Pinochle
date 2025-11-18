// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameSystem {

    static final int nbPlayers = 2;
    Hand[] hands;
    Hand[] trickWinningHands;
    static String trumpSuit = null;
    int currentBid = 0;
    static final int nbStartCards = 12;
    Card selected;
    int[] autoIndexHands = new int[nbPlayers];
    int bidWinPlayerIndex = 0;
    Hand pack;
    public final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    static Hand playingArea;
    static boolean afterBidding = false;
    static boolean afterDiscarding = false;
    final List<List<String>> playerAutoMovements = new ArrayList<>();
    static Map<Suit, List<Card>> hiddenCardsCounter = new HashMap<>();
    List<Card> playedCards = new ArrayList<>();

    public PlayStrategy playStrategy;
    public CardManager cardManager;
    public TrickTakingManager tricktakeManager;
    public Pinochle pinochle;
    public UIController bidUIController;
    public UIController trumpController;
    public ScoreManager scoreManager;
    public BidManager bidManager;
    public Logger logger;
    public GameDisplay gameDisplay;
    public GameState state;
    public Dealer dealer;
    public Card card1 = null;
    public Card card2 = null;

    GameSystem(Pinochle pinochle) {
        this.pinochle = pinochle;
        this.cardManager = new CardManager();
        this.tricktakeManager = new TrickTakingManager(this);
        this.bidUIController = new BidUIController(this);
        this.trumpController = new TrumpUIController(this);
        this.scoreManager = new ScoreManager(this);
        this.bidManager = new BidManager(this, (BidUIController) bidUIController);
        this.logger = new Logger();
        this.gameDisplay = new GameDisplay(this);
        this.dealer = new Dealer();
        this.state = new InitialisingState(this);
        this.playStrategy = new ManualPlayStrategy(this);
    }

    /*** Setters *****************************************************************************************************/

    void setScores() { scoreManager.initScore(); }

    void setPlayStrategy() { playStrategy = new AutoPlayStrategy(this); }

    void addMeldStrategies() { ((CompositeMeldStrategy) scoreManager.scoringComposite).addStrategies(); }

    void setState(GameState state) {
        this.state = state;
    }

    void setHands(Hand[] hands) {
        this.hands = hands;
    }

    void setTrickWinningHands(Hand[] hands) {
        this.trickWinningHands = hands;
    }

    public void setPlayingArea(Hand playingArea) { GameSystem.playingArea = playingArea; }

    void setDeck() {
        for (int i = 0; i < nbPlayers; i++) {
            hands[i] = new Hand(deck);
            trickWinningHands[i] = new Hand(deck);
        }
        setPlayingArea(new Hand(deck));
    }

    /*** Complex logic *******************************************************************************************/
    // sort suit based on priority
    void sortHand() {
        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, false);
        }
    }

    // add card listener class
    void addCardListener() {
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                if (!Pinochle.cutthroatMode || (afterBidding && afterDiscarding)) {
                    if (!tricktakeManager.checkValidTrick(card, hands[HumanBid.HUMAN_PLAYER_INDEX].getCardList(),
                            playingArea.getCardList())) {
                        pinochle.setStatus("Card is not valid. Player needs to choose higher card of the same suit or trump suit");
                        return;
                    }
                    hands[HumanBid.HUMAN_PLAYER_INDEX].setTouchEnabled(false);
                }
                else if (!afterBidding) {
                    if (!card.equals(card1) && !card.equals(card2)) {
                        pinochle.setStatus("Please select 1 of the 2 cards in the middle");
                        return;
                    }
                    playingArea.setTouchEnabled(false);
                } else {
                    if (!hands[HumanBid.HUMAN_PLAYER_INDEX].contains(card)) {
                        pinochle.setStatus("Select 12 cards to discard");
                        return;
                    }
                    hands[HumanBid.HUMAN_PLAYER_INDEX].setTouchEnabled(false);
                }
                selected = card;
            }
        };
        hands[HumanBid.HUMAN_PLAYER_INDEX].addCardListener(cardListener);
        playingArea.addCardListener(cardListener);
    }

    // perform trickTaking
    void trickTakingBetweenPlayers() {
        logger.addTrumpInfoToLog(scoreManager.scores);
        int nextPlayer = bidWinPlayerIndex;
        selected = null;
        int numberOfCards = hands[ComputerBid.COMPUTER_PLAYER_INDEX].getNumberOfCards();
        logger.addPlayerCardsToLog(hands);
        for (int i = 0; i < numberOfCards; i++) {
            logger.addRoundInfoToLog(i);
            for (int j = 0; j < nbPlayers; j++) {
                // playing
                playStrategy.trickTaking(nextPlayer);
                // remove selected cards from hidden list
                removeSelectedCardFromHiddenCounter();

                logger.addCardPlayedToLog(nextPlayer, selected);
                playingArea.insert(selected, true);
                // drawing
                gameDisplay.drawPlayingLocation(2, playingArea);
                // calculating score
                if (playingArea.getCardList().size() == 2) {
                    Pinochle.delay(Pinochle.delayTime);
                    int trickWinPlayerIndex = tricktakeManager.checkWinner(nextPlayer);
                    gameDisplay.transferCardsToWinner(trickWinPlayerIndex);
                    nextPlayer = trickWinPlayerIndex;
                } else {
                    nextPlayer = (nextPlayer + 1) % nbPlayers;
                }
            }
        }
    }

    // set up player's to be played cards from a pre-defined file
    void setupPlayerAutoMovements(String player0AutoMovement, String player1AutoMovement) {
        String[] playerMovements = new String[]{"", ""};
        if (player0AutoMovement != null) {
            playerMovements[0] = player0AutoMovement;
        }
        if (player1AutoMovement != null) {
            playerMovements[1] = player1AutoMovement;
        }
        for (String movementString : playerMovements) {
            List<String> movements = Arrays.asList(movementString.split(","));
            playerAutoMovements.add(movements);
        }
    }

    // update trick taking score
    List<Integer> updateScore() {
        for (int i = 0; i < GameSystem.nbPlayers; i++) scoreManager.updateScore(i);
        int maxScore = 0;
        for (int i = 0; i < GameSystem.nbPlayers; i++) if (scoreManager.scores[i] > maxScore) maxScore = scoreManager.scores[i];
        List<Integer> winners = new ArrayList<>();
        for (int i = 0; i < GameSystem.nbPlayers; i++) if (scoreManager.scores[i] == maxScore) winners.add(i);
        return winners;
    }

    // find the winner
    String getWinner(List<Integer> winners) {
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        return winText;
    }

    /*********************** The following supports trick taking steps ***********************************************/
    void initHiddenCardCounter() { hiddenCardsCounter = cardManager.groupBySuit(pack.getCardList()); }

    // update hidden cards counter
    void updateHiddenCardsCounter() {
        for (Card card: playedCards) {
            Suit suit = cardManager.getFullSuitName(((Suit) card.getSuit()).getSuitShortHand());
            hiddenCardsCounter.get(suit).remove(card);
        }
    }

    // remove played cards
    void removeSelectedCardFromHiddenCounter() {
        Suit suit = cardManager.getFullSuitName(((Suit) selected.getSuit()).getSuitShortHand());
        hiddenCardsCounter.get(suit).remove(selected);
    }
}
