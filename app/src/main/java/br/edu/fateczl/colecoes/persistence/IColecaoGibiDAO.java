package br.edu.fateczl.colecoes.persistence;

import java.sql.SQLException;

public interface IColecaoGibiDAO {
    ColecaoGibiDAO open() throws SQLException;

    void close();
}
