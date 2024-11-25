package br.edu.fateczl.colecoes.controller;

import br.edu.fateczl.colecoes.model.PedidoColecao;
import br.edu.fateczl.colecoes.persistence.PedidoColecaoDAO;

import java.sql.SQLException;
import java.util.List;

public class PedidoColecaoController implements IControlador<PedidoColecao> {
    private final PedidoColecaoDAO pedidoColecaoDAO;

    public PedidoColecaoController(PedidoColecaoDAO pedidoColecaoDAO) {
        this.pedidoColecaoDAO = pedidoColecaoDAO;
    }

    @Override
    public void registrarEntrada(PedidoColecao pedidoColecao) throws SQLException {
        if (pedidoColecaoDAO.abrir() == null) {
            pedidoColecaoDAO.abrir();
        }

        if (pedidoColecaoDAO.verificarSeEntradaExiste(pedidoColecao)) {
            throw new SQLException("Um pedido com este ID já existe");
        }

        pedidoColecaoDAO.registrarEntrada(pedidoColecao);
        pedidoColecaoDAO.fechar();
    }

    @Override
    public void atualizarEntrada(PedidoColecao pedidoColecao) throws SQLException {
        if (pedidoColecaoDAO.abrir() == null) {
            pedidoColecaoDAO.abrir();
        }

        if (!pedidoColecaoDAO.verificarSeEntradaExiste(pedidoColecao)) {
            throw new SQLException("Nenhum pedido com este ID foi encontrado");
        }

        pedidoColecaoDAO.atualizarEntrada(pedidoColecao);
        pedidoColecaoDAO.fechar();
    }

    @Override
    public void removerEntrada(PedidoColecao pedidoColecao) throws SQLException {
        if (pedidoColecaoDAO.abrir() == null) {
            pedidoColecaoDAO.abrir();
        }

        if (!pedidoColecaoDAO.verificarSeEntradaExiste(pedidoColecao)) {
            throw new SQLException("Nenhum pedido com este ID foi encontrado");
        }

        pedidoColecaoDAO.removerEntrada(pedidoColecao);
        pedidoColecaoDAO.fechar();
    }

    @Override
    public PedidoColecao buscarEntrada(PedidoColecao pedidoColecao) throws SQLException {
        if (pedidoColecaoDAO.abrir() == null) {
            pedidoColecaoDAO.abrir();
        }

        return pedidoColecaoDAO.buscarEntrada(pedidoColecao);
    }

    @Override
    public List<PedidoColecao> listarEntradas() throws SQLException {
        if (pedidoColecaoDAO.abrir() == null) {
            pedidoColecaoDAO.abrir();
        }

        return pedidoColecaoDAO.listarEntradas();
    }
}
