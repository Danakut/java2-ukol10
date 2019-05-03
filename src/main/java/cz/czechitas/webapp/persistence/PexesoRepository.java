package cz.czechitas.webapp.persistence;

import java.util.*;
import cz.czechitas.webapp.entity.*;

public interface PexesoRepository {

    public List<GameBoard> findAll();
    public GameBoard findOne(Long id);
    public GameBoard save(GameBoard board);
    public void delete(Long id);

}
