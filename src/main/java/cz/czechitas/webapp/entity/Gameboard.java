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

    public String cardsetToString() {
        String allCardStrings = "";
        int order = 0;
        for (Card card : this.cardset) {
            allCardStrings += order + ": " + card.toString() + "; ";
            order++;
        }
        return allCardStrings;
    }

    public String toString() {
        return "Game " + id + ": " + status + "\n" + cardsetToString();
    }
}

