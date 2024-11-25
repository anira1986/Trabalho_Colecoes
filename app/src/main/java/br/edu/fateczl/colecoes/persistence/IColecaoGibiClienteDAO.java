package br.edu.fateczl.colecoes.persistence;

import java.sql.SQLException;

public interface IColecaoGibiClienteDAO {
    CustomerDAO open() throws SQLException;

    void close();
}
