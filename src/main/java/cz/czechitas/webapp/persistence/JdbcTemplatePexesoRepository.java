package cz.czechitas.webapp.persistence;

import java.sql.*;
import java.time.*;
import java.util.*;
import javax.sql.*;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;
import cz.czechitas.webapp.entity.*;

@Component
public class JdbcTemplatePexesoRepository implements PexesoRepository {

    private JdbcTemplate querySender;
    private RowMapper<Gameboard> boardConverter;
    private RowMapper<Card> cardConverter;

    public JdbcTemplatePexesoRepository(DataSource dataSource) {
        querySender = new JdbcTemplate(dataSource);
        boardConverter = BeanPropertyRowMapper.newInstance(Gameboard.class);
        cardConverter = BeanPropertyRowMapper.newInstance(Card.class);
    }

    public List<Gameboard> findAll() {
        return querySender.query("SELECT id, status FROM gameboards", boardConverter);
    }

    public Gameboard findOne(Long id) {
        Gameboard board = querySender.queryForObject("SELECT id, status FROM gameboards WHERE id = ?", boardConverter, id);
        List<Card> cardset = querySender.query("SELECT id, cardNumber, status FROM cards WHERE gameboardId= ?",cardConverter, id);
        board.setCardset(cardset);
        return board;
    }

    public Gameboard save(Gameboard board) {
        if (board.getId() == null) {
            setupNewBoard(board);
        }
        updateBoard(board);
        return board;
    }

    public void delete(Long id) {

    }
    
    private Gameboard setupNewBoard(Gameboard board) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO gameboards (status, lastTurnStamp) VALUES (?, ?)";
        querySender.update((Connection con) -> {
                    PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, board.getStatus().name());              // proč je tu getStatus().name() místo jen getStatus()?
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
        String sql = "INSERT INTO cards (cardNumber, status, gameboardId, cardOrder) VALUES (?, ?, ?, ?)";
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

    private Gameboard updateBoard(Gameboard board) {
        querySender.update("UPDATE gameboards SET status = ?, lastTurnStamp = ? WHERE id = ?",
                board.getStatus().name(),
                Instant.now(),
                board.getId());

        List<Card> cardset = board.getCardset();
        for (int i = 0; i < cardset.size(); i++) {
            Card card = cardset.get(i);
            querySender.update("UPDATE cards SET status = ?, cardOrder = ? WHERE id = ?",
                    card.getStatus().name(),
                    i,
                    card.getId());
        }
        return board;
    }
}
