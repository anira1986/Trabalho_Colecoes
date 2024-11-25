package br.edu.fateczl.colecoes.persistence;

import java.sql.SQLException;

public interface IColecaoGibiPedidoDAO {
    ColecaoGibiPedidoDAO open() throws SQLException;

    void close();
}
