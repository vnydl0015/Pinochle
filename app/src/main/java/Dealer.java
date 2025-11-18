// Team Number: Wed-16:00-Team-03

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

// Dealing out cards in different ways
public class Dealer {

    /******************** The following codes are for dealing out cards to players ***********************/
    // dealing out 24 cards to each player
    void dealingOut(GameSystem system) {
        system.pack = system.deck.toHand(false);
        system.initHiddenCardCounter();
        system.dealer.dealInitBeforeGameStart(system);
        system.dealer.dealRemainingCardsBeforeGameSTart(system);
    }

    // deal based on pre-fined property files
    private void dealInitBeforeGameStart(GameSystem system) {
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCardsValue = system.pinochle.properties.getProperty(initialCardsKey);
            if (initialCardsValue == null) { continue; }
            String[] initialCards = initialCardsValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) { continue; }
                Card card = system.cardManager.getCardFromList(system.pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(false);
                    system.hands[i].insert(card, false);
                }
            }
        }
    }

    // deal remaining cards randomly before gameplay
    private void dealRemainingCardsBeforeGameSTart(GameSystem system) {
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            int cardsToDealt = GameSystem.nbStartCards - system.hands[i].getNumberOfCards();
            for (int j = 0; j < cardsToDealt; j++) {
                if (system.pack.isEmpty()) return;
                Card dealt = CardManager.randomCard(system.pack.getCardList());
                dealt.removeFromHand(false);
                system.hands[i].insert(dealt, false);
            }
        }
        system.playedCards.addAll(system.hands[ComputerBid.COMPUTER_PLAYER_INDEX].getCardList());
        system.updateHiddenCardsCounter();
    }

    // deal remaining cards (up to 22 total) randomly during cutthroat
    void dealExtraDuringCutThroat(GameSystem system, int winnerIndex) {
        int cardsToDealt = 22;
        int loserIndex = (winnerIndex + 1) % GameSystem.nbPlayers;
        for (int j = 0; j < cardsToDealt; j++) {
            if (system.pack.isEmpty()) return;
            Card dealt = CardManager.randomCard(system.pack.getCardList());
            dealt.removeFromHand(false);
            if (j % GameSystem.nbPlayers == 0) {
                system.hands[winnerIndex].insert(dealt, false);
            } else {
                system.hands[loserIndex].insert(dealt, false);
            }
        }
    }

    // deal remaining cards based on properties file during cutthroat
    void dealExtraDuringCutThroatAuto(GameSystem system) {
        for (int i = 0; i < GameSystem.nbPlayers; i++) {
            String extraCardsValue = system.pinochle.properties.getProperty("players." + i + ".extra_cards");
            if (extraCardsValue == null) { continue; }
            String[] extraCards = extraCardsValue.split(",");
            for (String extraCard: extraCards) {
                if (extraCard.length() <= 1) { continue; }
                Card card = system.cardManager.getCardFromList(system.pack.getCardList(), extraCard);
                if (card != null) {
                    card.removeFromHand(false);
                    system.hands[i].insert(card, true);
                    Pinochle.delay(Pinochle.delayTime);
                }
            }
        }
    }

    /******************** The following codes are for discarding cards from players ***********************/
    // remove cards from players during cutthroat according to pre-fined property files
    void discardHumanCardsDuringCutThroat(GameSystem system) {
        String finalCardsValue = system.pinochle.properties.getProperty("players.1.final_cards");
        if (finalCardsValue == null) { return; }
        List<Card> finalCards = new ArrayList<>();
        String[] finalCardsString = finalCardsValue.split(",");
        List<Card> copied = new ArrayList<>(system.hands[HumanBid.HUMAN_PLAYER_INDEX].getCardList());
        // use a copied array to discard original hands
        for (String finalCard: finalCardsString) {
            if (finalCard.length() <= 1) { continue; }
            Card card = system.cardManager.getCardFromList(copied, finalCard);
            if (card != null) {
                finalCards.add(card);
                copied.remove(card);
            }
        }
        // remove cards if not in the pre-defined file
        copied = new ArrayList<>(system.hands[HumanBid.HUMAN_PLAYER_INDEX].getCardList());
        for (Card card: copied) {
            if (!finalCards.contains(card)) {
                system.hands[HumanBid.HUMAN_PLAYER_INDEX].remove(card, true);
                system.pack.insert(card, false);
                Pinochle.delay(Pinochle.delayTime);
            }
        }
    }

    // remove computer player's cards during cutthroat based on rules
    void discardComputerCardsDuringCutThroat(GameSystem system) {
        List<Card> compHand = system.hands[ComputerBid.COMPUTER_PLAYER_INDEX].getCardList();
        int compHandLen = compHand.size();

        while (compHandLen > 12) {
            Map<Suit, List<Card>> cardsBySuit = system.cardManager.groupBySuit(compHand);
            // discarding cards until only 12 are left
            List<Card> suitToBeRemoved = system.cardManager.getSuitWithFewestCards(cardsBySuit);
            int lenSuit = suitToBeRemoved.size();
            if (compHandLen - lenSuit >= 12) {
                // remove all cards in this suit
                compHandLen -= lenSuit;
                for (Card card : suitToBeRemoved) {
                    system.selected = card;
                    system.hands[ComputerBid.COMPUTER_PLAYER_INDEX].remove(card, true);
                    system.pack.insert(card, false);
                    Pinochle.delay(Pinochle.delayTime);
                    system.removeSelectedCardFromHiddenCounter();
                }
            } else {
                int nbToDiscard = compHandLen - 12;
                suitToBeRemoved.sort(Comparator.comparingInt(card -> ((Rank) card.getRank()).getRankCardValue()));
                for (int i = 0; i < nbToDiscard; i++) {
                    system.selected = suitToBeRemoved.get(i);
                    system.hands[ComputerBid.COMPUTER_PLAYER_INDEX].remove(suitToBeRemoved.get(i), true);
                    system.pack.insert(suitToBeRemoved.get(i), false);
                    Pinochle.delay(Pinochle.delayTime);
                    system.removeSelectedCardFromHiddenCounter();
                }
                compHandLen -= nbToDiscard;
            }
        }
    }
}
