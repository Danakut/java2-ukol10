package cz.czechitas.webapp.persistence;

import java.util.*;
import org.springframework.stereotype.*;
import cz.czechitas.webapp.entity.*;

//@Component
public class InMemoryPexesoRepository implements PexesoRepository {

    private Random random;
    private Map<Long, GameBoard> gameBoardMap;

    public InMemoryPexesoRepository() {
        random = new Random();
        gameBoardMap = new HashMap<>();
    }

    public List<GameBoard> findAll() {
        Set<Long> idSet = gameBoardMap.keySet();
        List<Long> idList = new ArrayList<>();
        idSet.forEach(id -> idList.add(id));
        List<GameBoard> gameList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long boardId = idList.get(i);
            gameList.add(gameBoardMap.get(boardId));
        }
        return gameList;
    }

    public GameBoard findOne(Long id) {
        GameBoard board = gameBoardMap.get(id);
        if (board == null) {
            throw new GameNotFoundException();
        }
        return board;
    }

    public GameBoard save(GameBoard board) {
        if (board.getId() == null) {
            setupNewBoard(board);
        }
        gameBoardMap.put(board.getId(), board);
        return board;
    }

    public void delete(Long id) {
        gameBoardMap.remove(id);
    }

    private Long vygenerujNahodneId() {
        return (long)Math.abs(random.nextInt());
    }

    private GameBoard setupNewBoard(GameBoard board) {
        board.setId(vygenerujNahodneId());        //doplnit podminku, ktera vylouci, ze nahodne cislo bude id hry, ktera uz je v "databazi" (gameBoardMap)?
        for (Card card : board.getCardset()) {
            card.setId(vygenerujNahodneId());
        }
        return board;
    }
}
