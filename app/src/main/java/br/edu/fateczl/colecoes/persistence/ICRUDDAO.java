package br.edu.fateczl.colecoes.persistence;

import java.sql.SQLException;
import java.util.List;

public interface ICRUDDAO<T> {
    void registerEntry(T t) throws SQLException;

    void updateEntry(T t) throws SQLException;

    void removeEntry(T t) throws SQLException;

    T searchEntry(T t) throws SQLException;

    List<T> listEntry() throws SQLException;

    boolean checkIfEntryExist(T t) throws SQLException;
}
