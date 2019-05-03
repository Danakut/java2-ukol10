package cz.czechitas.webapp.persistence;

import java.util.*;

import cz.czechitas.webapp.entity.*;

//@Component
public class InMemoryPexesoRepository implements PexesoRepository {

    private Random random;
    private Map<Long, Gameboard> gameBoardMap;

    public InMemoryPexesoRepository() {
        random = new Random();
        gameBoardMap = new HashMap<>();
    }

    public List<Gameboard> findAll() {
        Set<Long> idSet = gameBoardMap.keySet();
        List<Long> idList = new ArrayList<>();
        idSet.forEach(id -> idList.add(id));
        List<Gameboard> gameList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long boardId = idList.get(i);
            gameList.add(gameBoardMap.get(boardId));
        }
        return gameList;
    }

    public Gameboard findOne(Long id) {
        Gameboard board = gameBoardMap.get(id);
        if (board == null) {
            throw new GameNotFoundException();
        }
        return board;
    }

    public Gameboard save(Gameboard board) {
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

    private Gameboard setupNewBoard(Gameboard board) {
        board.setId(vygenerujNahodneId());        //doplnit podminku, ktera vylouci, ze nahodne cislo bude id hry, ktera uz je v "databazi" (gameBoardMap)?
        for (Card card : board.getCardset()) {
            card.setId(vygenerujNahodneId());
        }
        return board;
    }
}
