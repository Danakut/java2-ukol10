package cz.czechitas.webapp.entity;

import java.util.*;

public class GameBoard {

    private Long id;
    private StavHry status;
    private List<Card> cardset;

    public GameBoard() {
    }

    public GameBoard(List<Card> cardset, StavHry status) {
        this.cardset = cardset;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long newValue) {
        this.id = newValue;
    }

    public StavHry getStav() {
        return status;
    }

    public void setStav(StavHry status) {
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

