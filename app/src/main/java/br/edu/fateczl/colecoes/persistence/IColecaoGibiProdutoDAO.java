package br.edu.fateczl.colecoes.persistence;

import java.sql.SQLException;

public interface IColecaoGibiProdutoDAO {
    ColecaoGibiProdutoDAO open() throws SQLException;

    void close();
}
