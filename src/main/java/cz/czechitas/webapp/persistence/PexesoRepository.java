package cz.czechitas.webapp.persistence;

import java.util.*;
import cz.czechitas.webapp.entity.*;

public interface PexesoRepository {

    public List<Gameboard> findAll();
    public Gameboard findOne(Long id);
    public Gameboard save(Gameboard board);
    public void delete(Long id);

}
