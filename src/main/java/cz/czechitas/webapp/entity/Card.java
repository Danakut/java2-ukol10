package cz.czechitas.webapp.entity;

import java.util.*;

public class Card {

    private Long id;
    private int cardNumber;
    private CardStatus status;

    //prevodnik jmen souboru (protoze nemaji jednotnou masku *.jpg) - pro pouziti v .html sablone
    private static final List<String> FILENAMES = Arrays.asList(
            "pic01.jpg",
            "pic01.jpg",
            "pic02.jpg",
            "pic02.jpg",
            "pic03.jpg",
            "pic03.jpg",
            "pic04.jpg",
            "pic04.jpg",
            "pic05.jpg",
            "pic05.jpg",
            "pic06.jpg",
            "pic06.jpg",
            "pic07.jpg",
            "pic07.jpg",
            "pic08.jpg",
            "pic08.jpg",
            "pic09.jpg",
            "pic09.jpg",
            "pic10.jpg",
            "pic10.jpg",
            "pic11.jpg",
            "pic11.jpg",
            "pic12.jpg",
            "pic12.jpg",
            "pic13.jpg",
            "pic13.jpg",
            "pic14.jpg",
            "pic14.jpg",
            "pic15.jpg",
            "pic15.jpg",
            "pic16.jpg",
            "pic16.jpg",
            "pic17.jpg",
            "pic17.jpg",
            "pic18.jpg",
            "pic18.jpg",
            "pic19.jpg",
            "pic19.jpg",
            "pic20.gif",
            "pic20.gif",
            "pic21.png",
            "pic21.png",
            "pic22.png",
            "pic22.png",
            "pic23.png",
            "pic23.png",
            "pic24.png",
            "pic24.png",
            "pic25.png",
            "pic25.png",
            "pic26.png",
            "pic26.png",
            "pic27.png",
            "pic27.png",
            "pic28.png",
            "pic28.png",
            "pic29.png",
            "pic29.png",
            "pic30.png",
            "pic30.png",
            "pic31.png",
            "pic31.png",
            "pic32.png",
            "pic32.png"
    );

    public Card() {
    }

    public Card(int cardNumber, CardStatus status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long newValue) {
        this.id = newValue;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int newValue) {
        cardNumber = newValue;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus newValue) {
        this.status = newValue;
    }

    public String getFilepath() {
        return FILENAMES.get(cardNumber);
    }
}

