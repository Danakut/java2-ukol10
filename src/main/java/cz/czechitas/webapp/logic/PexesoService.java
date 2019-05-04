package cz.czechitas.webapp.logic;

import java.io.IOException;
import java.util.*;
import java.util.logging.*;

import cz.czechitas.webapp.entity.*;
import cz.czechitas.webapp.persistence.*;

public class PexesoService {

    private static final Logger appLogger = Logger.getGlobal();
    private Handler appHandler;
    private Level reportLevel = Level.INFO;


    private final int CARD_PAIR_SUM = 32;

    private PexesoRepository gameProvider;

    public PexesoService(PexesoRepository gameProvider) {
        this.gameProvider = gameProvider;

        try {
            appHandler = new FileHandler("newlog.txt");
        } catch (IOException e) {
            e.printStackTrace();
            appHandler = new ConsoleHandler();
        }

        appHandler.setLevel(reportLevel);
        SimpleFormatter logFormatter = new SimpleFormatter();
        appHandler.setFormatter(logFormatter);

        appLogger.addHandler(appHandler );
        appLogger.setLevel(reportLevel);
        appLogger.setUseParentHandlers(false);
    }

    public List<Gameboard> findAllBoards() {
        return gameProvider.findAll();
    }

    public Gameboard createBoard() {
        Gameboard board = new Gameboard(createCardset(), GameStatus.PLAYER1_SELECT_1ST_CARD);
        gameProvider.save(board);
        appLogger.info("Board created.");
        return board;
    }

    public Gameboard findBoard(Long id) {
        return gameProvider.findOne(id);
    }

    public void deleteBoard(Long id) {
        gameProvider.delete(id);
    }

    public void makeMove(Long boardId, int clickedCardNumber) {
        Gameboard board = gameProvider.findOne(boardId);
        appLogger.info(board.toString());
        Card chosenCard = board.getCardset().get(clickedCardNumber);

        if (board.getStatus() == GameStatus.PLAYER1_SELECT_1ST_CARD) {
            if (chosenCard.getStatus() == CardStatus.BACK) {
                chosenCard.setStatus(CardStatus.FACE);
                appLogger.info("First card clicked: " + clickedCardNumber);
                board.setStatus(GameStatus.PLAYER1_SELECT_2ND_CARD);
            }

        } else if (board.getStatus() == GameStatus.PLAYER1_SELECT_2ND_CARD)  {
            if (chosenCard.getStatus() == CardStatus.BACK) {
                chosenCard.setStatus(CardStatus.FACE);
                appLogger.info("Second card clicked: " + clickedCardNumber);
                board.setStatus(GameStatus.PLAYER1_EVALUATE);
            }

        } else if (board.getStatus() == GameStatus.PLAYER1_EVALUATE) {
            ArrayList<Card> turnedCards = new ArrayList<>(2);
            int cardsTaken = 0;
            for (Card card : board.getCardset()) {
                if (card.getStatus() == CardStatus.FACE) {
                    turnedCards.add(card);
                    appLogger.info("Card " + card.getCardNumber() + " added to turned cards array.");
                }
                if (card.getStatus() == CardStatus.TAKEN) {
                    cardsTaken +=1;
                }
            }

            Card card1 = turnedCards.get(0);
            Card card2 = turnedCards.get(1);
            appLogger.info("Evaluation of cards: " + card1.getCardNumber() + " and " + card2.getCardNumber() + ".");
            if (card1.getCardNumber() == card2.getCardNumber()) {
                card1.setStatus(CardStatus.TAKEN);
                card2.setStatus(CardStatus.TAKEN);
                cardsTaken +=2;
                appLogger.info("The player found a card pair.");
            } else {
                card1.setStatus(CardStatus.BACK);
                card2.setStatus(CardStatus.BACK);
                appLogger.info("The player didn't find a card pair.");
            }

            if (cardsTaken/2 == CARD_PAIR_SUM) {
                board.setStatus(GameStatus.GAME_FINISHED);
                appLogger.info("The game has finished.");
            } else {
                board.setStatus(GameStatus.PLAYER1_SELECT_1ST_CARD);
                appLogger.info("New turn commencing.");
            }
        }

        Long savedGameid = gameProvider.save(board).getId();
        appLogger.info("Game " + savedGameid.toString() + " saved to db.");
    }

    private List<Card> createCardset() {
        List<Card> cardset = new ArrayList<>();

        for (int cardNumber = 0; cardNumber < CARD_PAIR_SUM; cardNumber++) {
            cardset.add(new Card(cardNumber, CardStatus.BACK));
            cardset.add(new Card(cardNumber, CardStatus.BACK));
        }
        appLogger.info("Cardset created.");
        Collections.shuffle(cardset);
        appLogger.info("Cardset shuffled.");
        return cardset;
    }
}


//todo zastavit turnedArray po 2 kartách (pokud je chybně nastaven status v db, pole nabere i více než dvě karty)
//synchronizovat tak, aby další tah bylo možné provést až po zápisu dat z předchozího tahu do db. Tj. gameprovider.save musí skončit před gameprovider.findOne
//todo prepsat evaluaci na stream/lambdy