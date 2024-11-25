package br.edu.fateczl.colecoes.controller;

import java.sql.SQLException;
import java.util.List;

public interface IControlador<T> {

    void registrarEntrada(T t) throws SQLException;

    void atualizarEntrada(T t) throws SQLException;

    void removerEntrada(T t) throws SQLException;

    T buscarEntrada(T t) throws SQLException;

    List<T> listarEntradas() throws SQLException;
}
