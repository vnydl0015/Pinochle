// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.*;

// responsible for handling all card-related tasks
public class CardManager {

    static public final int seed = 30008;

    static final Random random = new Random(seed);

    // check if 2 cards are same suit
    static boolean isSameSuit(Card card1, Card card2) {
        Suit card1Suit = (Suit) card1.getSuit();
        Suit card2Suit = (Suit) card2.getSuit();
        return card1Suit.getSuitShortHand().equals(card2Suit.getSuitShortHand());
    }

    // check if a card is of higher rank
    static boolean isHigherRank(Card card1, Card card2) {
        Rank card2Rank = (Rank) card2.getRank();
        Rank card1Rank = (Rank) card1.getRank();
        return card1Rank.getRankCardValue() > card2Rank.getRankCardValue();
    }

    private boolean isLowerRank(Card card1, Card card2) {
        Rank card2Rank = (Rank) card2.getRank();
        Rank card1Rank = (Rank) card1.getRank();
        return card1Rank.getRankCardValue() < card2Rank.getRankCardValue();
    }

    // group cards by suit
    public Map<Suit, List<Card>> groupBySuit(List<Card> cards) {
        Map<Suit, List<Card>> map = new HashMap<>();
        for (Card card: cards) {
            Suit suit = getFullSuitName(((Suit) card.getSuit()).getSuitShortHand());
            map.computeIfAbsent(suit, k -> new ArrayList<>()).add(card);
        }
        return map;
    }

    // get max of the two values' calculations
    int getMaxValues(List<Card> cards, Suit majorSuit) {
        // get this suit's score value
        int majoritySuitValue = getMajorSuitValue(cards, majorSuit);
        // get suit with high rank's score value
        int highSuitValue =  getHighRankSuitValue(cards);
        return Math.max(majoritySuitValue, highSuitValue);
    }


    // get major suit and its count
    public Map<Suit, Integer> getMajorSuitAndCount(List<Card> cards) {
        Map<String, Integer> map = new HashMap<>();
        int maxSuitCount = 0;
        Suit maxSuit = null;
        for (Card card: cards) {
            String suit = ((Suit) card.getSuit()).getSuitShortHand();
            map.putIfAbsent(suit, 0);
            map.put(suit, map.get(suit) + 1);
            int count = map.get(suit);
            if (count > maxSuitCount) {
                maxSuitCount = count;
                maxSuit = getFullSuitName(suit);
            } else if (count == maxSuitCount) {
                maxSuit = random.nextBoolean() ? maxSuit : getFullSuitName(suit);
            }
        }
        assert maxSuit != null;
        return Map.of(maxSuit, maxSuitCount);
    }

    // convert shorthand name to full name
    public Suit getFullSuitName(String suitString) {
        return switch (suitString) {
            case "S" -> Suit.SPADES;
            case "H" -> Suit.HEARTS;
            case "D" -> Suit.DIAMONDS;
            default -> Suit.CLUBS;
        };
    }

    // get value of major suit
    private int getMajorSuitValue(List<Card> cards, Suit majorSuit) {
        int totalValue = 0;
        for (Card card: cards) {
            if (card.getSuit().equals(majorSuit)) {
                totalValue += ((Rank) card.getRank()).getScoreValue();
            }
        }
        return totalValue;
    }

    // get value of cards with high rank (Ace, Ten, King)
    private int getHighRankSuitValue(List<Card> cards) {
        Map<String, Integer> counter = new HashMap<>();
        Map<Suit, List<Card>> groupedSuit = new HashMap<>();

        int maxCount = 0;
        for (Card card: cards) {
            String suit = ((Suit) card.getSuit()).getSuitShortHand();
            // update groupedSuit map
            groupedSuit.computeIfAbsent(getFullSuitName(suit), k -> new ArrayList<>()).add(card);
            // update counter
            counter.putIfAbsent(suit, 0);
            if (card.getRank().equals(Rank.ACE) || card.getRank().equals(Rank.TEN) ||
                    card.getRank().equals(Rank.KING)) {
                counter.put(suit, counter.get(suit) + 1);
            }
            maxCount = Math.max(counter.get(suit), maxCount);
        }

        // calculate maxRankValue
        int maxRankValue = 0;
        for (Map.Entry<String, Integer> entry: counter.entrySet()) {
            int rankValue;
            if (entry.getValue() == maxCount) {
                rankValue = getTotalCardValue(groupedSuit.get(getFullSuitName(entry.getKey())));
                if (rankValue > maxRankValue) maxRankValue = rankValue;
            }
        }
        return maxRankValue;
    }

    // get value of cards in a list (generic)
    private int getTotalCardValue(List<Card> cards) {
        int totalValue = 0;
        for (Card card : cards) {
            totalValue += ((Rank) card.getRank()).getScoreValue();
        }
        return totalValue;
    }

    // return random card from ArrayList
    static Card randomCard(ArrayList<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    // return top card on the pile
    static Card topCard(ArrayList<Card> list) {
        Card topCard = list.get(0);
        topCard.removeFromHand(false);
        GameSystem.playingArea.insert(topCard, true);
        return topCard;
    }

    // get card name (rank + suit)
    private String getCardName(Card card) {
        Suit suit = (Suit) card.getSuit();
        Rank rank = (Rank) card.getRank();
        return rank.getRankCardValue() + suit.getSuitShortHand();
    }


    // check if card in list
    boolean checkCardInList(List<Card> cardList, List<String> cardsToCheck) {
        ArrayList<String> cardsToRemove = new ArrayList<>(cardsToCheck);
        for (Card card : cardList) {
            String cardName = getCardName(card);
            cardsToRemove.remove(cardName);
        }
        return cardsToRemove.isEmpty();
    }

    // remove cards from list
    List<Card> removeCardFromList(List<Card> cardList, List<String> cardsToRemove) {
        List<Card> newCardList = new ArrayList<>();
        List<String> newCardsToRemove = new ArrayList<>(cardsToRemove);
        for (Card card : cardList) {
            String cardName = getCardName(card);
            if (newCardsToRemove.contains(cardName)) {
                newCardsToRemove.remove(cardName);
            } else {
                newCardList.add(card);
            }
        }
        return newCardList;
    }

    // parse rank of card
    private Rank getRankFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        int rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getShortHandValue() == rankValue) {
                return rank;
            }
        }

        return Rank.ACE;
    }

    // parse suit of card
    private Suit getSuitFromString(String cardName) {
        String suitString = cardName.substring(cardName.length() - 1);
        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }

    // return card to play during trick taking
    Card applyAutoMovement(Hand hand, String nextMovement) {
        if (hand.isEmpty()) return null;
        String[] cardStrings = nextMovement.split("-");
        String cardDealtString = cardStrings[0];
        if (nextMovement.isEmpty()) {
            return null;
        }
        Card dealt = getCardFromList(hand.getCardList(), cardDealtString);
        if (dealt == null) {
            System.err.println("cannot draw card: " + cardDealtString + " - hand: " + hand.getCardList());
        }

        return dealt;
    }

    // get higher card
    static Card getHigherCardFromList(Card existingCard, List<Card> cards) {
        return cards.stream().filter(playerCard -> isSameSuit(existingCard, playerCard) &&
                isHigherRank(playerCard, existingCard)).findAny().orElse(null);
    }

    // trump card finder
    Card getTrumpCard(List<Card> cards) {
        return cards.stream().filter(playerCard -> {
            Suit playerCardSuit = (Suit) playerCard.getSuit();
            return playerCardSuit.getSuitShortHand().equals(GameSystem.trumpSuit);
        }).findAny().orElse(null);
    }

    // get a random card to play
    Card getRandomCardForHand(Hand hand) {
        List<Card> existingCards = GameSystem.playingArea.getCardList();
        if (existingCards.isEmpty()) {
            int x = random.nextInt(hand.getCardList().size());
            return hand.getCardList().get(x);
        }

        Pinochle.delay(Pinochle.thinkingTime);
        Card existingCard = existingCards.get(0);
        Card higherCard = getHigherCardFromList(existingCard, hand.getCardList());
        if (higherCard != null) {
            return higherCard;
        }

        Card trumpCard = getTrumpCard(hand.getCardList());
        if (trumpCard != null) {
            return trumpCard;
        }

        int x = random.nextInt(hand.getCardList().size());
        return hand.getCardList().get(x);
    }

    // For smart tricking extension
    Card getSmartCardForHand(Hand hand) {
        List<Card> existingCards = GameSystem.playingArea.getCardList();
        Map<Suit, List<Card>> currentCardsInHand = groupBySuit(hand.getCardList());
        Card returnCard;
        Suit trumpSuit = getFullSuitName(GameSystem.trumpSuit);

        // If computer leads or play first, play with a strong non-trump card
        if (existingCards.isEmpty()) {

            // sort current hand descending-ly
            hand.getCardList().sort(rankDescendingCompare());

            // check hidden counter
            returnCard = checkHiddenCounter(hand.getCardList());
            if (returnCard != null) {
                return returnCard;
            }

            // if only trump suit exists, lead with the weakest trump suit
            if (currentCardsInHand.containsKey(trumpSuit)) {
                returnCard = currentCardsInHand.get(trumpSuit).stream().reduce((card1, card2) ->
                                isLowerRank(card1, card2) ? card1 : card2).orElse(null);
            }

            // if no trump suit left as well, get weakest
            if (returnCard == null) {
                return getWeakestCard(hand.getCardList(), trumpSuit);
            }
            return returnCard;

        } else {
            Card existingCard = existingCards.get(0);

            if (!(existingCard.getSuit().equals(trumpSuit))) {
                // sort ascending, get immediate stronger card of the same suit
                if (currentCardsInHand.containsKey(getFullSuitName(((Suit) existingCard.getSuit()).getSuitShortHand()))) {
                    Card higherCard = getImmediateStrongerCard(currentCardsInHand.get(getFullSuitName(((Suit)
                            existingCard.getSuit()).getSuitShortHand())), existingCard);
                    if (higherCard != null) {
                        return higherCard;
                    }
                }
            } else if (existingCard.getSuit().equals(trumpSuit)) {
                // sort ascending, get immediate stronger card of the trump suit
                if (currentCardsInHand.containsKey(trumpSuit)) {
                    Card trumpCard = getImmediateStrongerCard(currentCardsInHand.get(trumpSuit), existingCard);
                    if (trumpCard != null) {
                        return trumpCard;
                    }
                }
            }

            // else return weakest of all suits, forgetting trump
            return getWeakestCard(hand.getCardList(), trumpSuit);
        }
    }

    // get the weakest card
    Card getWeakestCard(List<Card> cards, Suit trumpSuit) {
        if (cards.isEmpty()) { return null; }

        List<Card> nonTrumpCards = new ArrayList<>();
        List<Card> trumpCards = new ArrayList<>();

        for (Card card : cards) {
            if (card.getSuit().equals(trumpSuit)) {
                trumpCards.add(card);
            } else {
                nonTrumpCards.add(card);
            }
        }
        nonTrumpCards.sort(rankDescendingCompare());
        trumpCards.sort(rankDescendingCompare());

//        Card returnCard;
        if (!nonTrumpCards.isEmpty()) {
            // can recheck the hiddenCounter here
//            returnCard = checkHiddenCounter(nonTrumpCards);
//            if (returnCard != null) {
//                return returnCard;
//            }
            return nonTrumpCards.get(nonTrumpCards.size() - 1);
        } else if (!trumpCards.isEmpty()) {
            // can recheck the hiddenCounter here
//            returnCard = checkHiddenCounter(trumpCards);
//            if (returnCard != null) {
//                return returnCard;
//            }
            return trumpCards.get(trumpCards.size() - 1);
        }

        return null;
    }

    Card checkHiddenCounter(List<Card> cards) {
        for (Card card: cards) {
            Suit currentSuit = getFullSuitName(((Suit) card.getSuit()).getSuitShortHand());

            // if our card is currently strongest, but most cards are already played,
            // then human will definitely throw in a trump card --> lose
            if (GameSystem.hiddenCardsCounter.get(currentSuit).isEmpty()) { continue; }
            GameSystem.hiddenCardsCounter.get(currentSuit).sort(rankDescendingCompare());

            // if our card is stronger than cards that have NOT been played, play it --> win if players play weaker
            // cards of same suit. But if they play trump suit --> lose, but we can infer that they don't
            // have anymore strong cards of that suit
            if (isHigherRank(card, GameSystem.hiddenCardsCounter.get(currentSuit).get(0))) {
                return card;
            }
        }
        return null;
    }

    // get the weakest but stronger card of the same suit
    // Adapted from https://www.geeksforgeeks.org/stream-min-method-in-java-with-examples/
    Card getImmediateStrongerCard(List<Card> cards, Card existingCard) {
        return cards.stream()
                .filter(card -> isSameSuit(existingCard, card) && isHigherRank(card, existingCard))
                .min(Comparator.comparingInt(c -> ((Rank) c.getRank()).getRankCardValue()))
                .orElse(null);
    }

    // get a card from list
    Card getCardFromList(List<Card> cards, String cardName) {
        Rank existingRank = getRankFromString(cardName);
        Suit existingSuit = getSuitFromString(cardName);
        for (Card card : cards) {
            Suit suit = (Suit) card.getSuit();
            Rank rank = (Rank) card.getRank();
            if (suit.getSuitShortHand().equals(existingSuit.getSuitShortHand())
                    && rank.getRankCardValue() == existingRank.getRankCardValue()) {
                return card;
            }
        }
        return null;
    }

    // return suit with the lowest card count
    List<Card> getSuitWithFewestCards(Map<Suit, List<Card>> cardsBySuit) {
        List<Card> fewestCards = null;
        int minSize = Integer.MAX_VALUE;

        for (Map.Entry<Suit, List<Card>> entry : cardsBySuit.entrySet()) {
            Suit suit = entry.getKey();
            if (suit.getSuitShortHand().equals(GameSystem.trumpSuit)) {
                continue;
            }
            List<Card> suitCards = entry.getValue();
            if (suitCards != null && suitCards.size() < minSize) {
                fewestCards = suitCards;
                minSize = suitCards.size();
            }
        }
        return fewestCards != null ? fewestCards : new ArrayList<>();
    }

    // sort descending-ly
    // Adapted from https://www.geeksforgeeks.org/java-comparator-interface/
    private static Comparator<Card> rankDescendingCompare(){
        return (Card card1, Card card2) -> {
            int rank1 = ((Rank) card1.getRank()).getRankCardValue();
            int rank2 = ((Rank) card2.getRank()).getRankCardValue();
            return Integer.compare(rank2, rank1);
        };
    }
}