package cz.czechitas.webapp.persistence;

import java.sql.*;
import java.time.*;
import java.util.*;
import javax.sql.*;
import org.mariadb.jdbc.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;
import cz.czechitas.webapp.entity.*;

@Component
public class JdbcTemplatePexesoRepository implements PexesoRepository {

    private JdbcTemplate querySender;
    private RowMapper<GameBoard> boardConverter;
    private RowMapper<Card> cardConverter;

    public JdbcTemplatePexesoRepository(DataSource dataSource) {
        querySender = new JdbcTemplate(dataSource);
        boardConverter = BeanPropertyRowMapper.newInstance(GameBoard.class);
        cardConverter = BeanPropertyRowMapper.newInstance(Card.class);
    }

    public List<GameBoard> findAll() {
        return querySender.query("SELECT ID, Stav FROM HerniPlochy", boardConverter);
    }

    public GameBoard findOne(Long id) {
        GameBoard board = querySender.queryForObject("SELECT ID, Stav FROM HerniPlochy WHERE ID = ?", boardConverter, id);
        List<Card> cardset = querySender.query("SELECT ID, CisloKarty AS CardNumber, Stav AS Status FROM Karty WHERE HerniPlochaID = ?",cardConverter, id);
        board.setCardset(cardset);
        return board;
    }

    public GameBoard save(GameBoard board) {
        if (board.getId() == null) {
            setupNewBoard(board);
        }
        updateBoard(board);
        return board;
    }

    public void delete(Long id) {

    }
    
    private GameBoard setupNewBoard(GameBoard board) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO HerniPlochy (Stav, CasPoslednihoTahu) VALUES (?, ?)";
        querySender.update((Connection con) -> {
                    PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, board.getStav().name());              // proč je tu getStatus().name() místo jen getStatus()?
                    statement.setObject(2, Instant.now());
                    return statement;
                },
                keyHolder);
        board.setId(keyHolder.getKey().longValue());
        
        List<Card> cardset = board.getCardset();
        for (int i = 0; i < cardset.size(); i++) {
            Card card = cardset.get(i);
            addCard(card, board.getId(), i);
        }
        return board;
    }

    private void addCard(Card card, Long boardId, int cardArrayIndex) {
        GeneratedKeyHolder cardKeyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO Karty (Cislokarty, Stav, HerniPlochaID, PoradiKarty) VALUES (?, ?, ?, ?)";
        querySender.update((Connection con) -> {
                    PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, card.getCardNumber());
                    statement.setString(2, card.getStatus().name());
                    statement.setLong(3, boardId);
                    statement.setInt(4, cardArrayIndex);
                    return statement;
                },
                cardKeyHolder);
        card.setId(cardKeyHolder.getKey().longValue());
    }

    private GameBoard updateBoard(GameBoard board) {
        querySender.update("UPDATE HerniPlochy SET Stav = ?,CasPoslednihoTahu = ? WHERE ID = ?",
                board.getStav().name(),     // proč je tu getStatus().name() místo jen getStatus()?
                Instant.now(),
                board.getId());

        List<Card> cardset = board.getCardset();
        for (int i = 0; i < cardset.size(); i++) {
            Card card = cardset.get(i);
            querySender.update("UPDATE Karty SET Stav = ?, PoradiKarty = ? WHERE ID = ?",
                    card.getStatus().name(),
                    i,
                    card.getId());
        }
        return board;
    }
}
