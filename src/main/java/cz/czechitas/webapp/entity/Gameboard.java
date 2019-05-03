package cz.czechitas.webapp.entity;

import java.util.*;

public class Gameboard {

    private Long id;
    private GameStatus status;
    private List<Card> cardset;

    public Gameboard() {
    }

    public Gameboard(List<Card> cardset, GameStatus status) {
        this.cardset = cardset;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long newValue) {
        this.id = newValue;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public List<Card> getCardset() {
        return cardset;
    }

    public void setCardset(List<Card> newValue) {
        cardset = newValue;
    }

    public Card findCard(int cardNumber) {
        for (Card card : cardset) {
            if (card.getCardNumber() == cardNumber) return card;
        }
        throw new IllegalArgumentException("Card " + cardNumber + " not found.");
    }
}

