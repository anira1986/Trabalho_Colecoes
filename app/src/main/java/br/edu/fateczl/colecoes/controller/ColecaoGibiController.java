package br.edu.fateczl.colecoes.controller;

import br.edu.fateczl.colecoes.model.ColecaoGibi;
import br.edu.fateczl.colecoes.persistence.ColecaoGibiDAO;

import java.sql.SQLException;
import java.util.List;

public class ColecaoGibiController implements IController<ColecaoGibi> {
    private final ColecaoGibiDAO colecaoGibiDAO;

    public ColecaoGibiController(ColecaoGibiDAO colecaoGibiDAO) {
        this.colecaoGibiDAO = colecaoGibiDAO;
    }

    @Override
    public void registerEntry(ColecaoGibi colecaoGibi) throws SQLException {
        if (colecaoGibiDAO.open() == null) {
            colecaoGibiDAO.open();
        }

        // Verifica se o ID já está em uso.
        if (colecaoGibiDAO.checkIfEntryExist(colecaoGibi)) {
            throw new SQLException("Uma coleção com este ID já existe");
        }

        colecaoGibiDAO.registerEntry(colecaoGibi);

        colecaoGibiDAO.close();
    }

    @Override
    public void updateEntry(ColecaoGibi colecaoGibi) throws SQLException {
        if (colecaoGibiDAO.open() == null) {
            colecaoGibiDAO.open();
        }

        // Verifica se o ID existe.
        if (!colecaoGibiDAO.checkIfEntryExist(colecaoGibi)) {
            throw new SQLException("Uma coleção com este ID não foi encontrada");
        }

        colecaoGibiDAO.updateEntry(colecaoGibi);

        colecaoGibiDAO.close();
    }

    @Override
    public void removeEntry(ColecaoGibi colecaoGibi) throws SQLException {
        if (colecaoGibiDAO.open() == null) {
            colecaoGibiDAO.open();
        }

        // Verifica se o ID existe.
        if (!colecaoGibiDAO.checkIfEntryExist(colecaoGibi)) {
            throw new SQLException("Uma coleção com este ID não foi encontrada");
        }

        colecaoGibiDAO.removeEntry(colecaoGibi);

        colecaoGibiDAO.close();
    }

    @Override
    public ColecaoGibi searchEntry(ColecaoGibi colecaoGibi) throws SQLException {
        if (colecaoGibiDAO.open() == null) {
            colecaoGibiDAO.open();
        }

        return colecaoGibiDAO.searchEntry(colecaoGibi);
    }

    @Override
    public List<ColecaoGibi> listEntry() throws SQLException {
        if (colecaoGibiDAO.open() == null) {
            colecaoGibiDAO.open();
        }

        return colecaoGibiDAO.listEntry();
    }
}
