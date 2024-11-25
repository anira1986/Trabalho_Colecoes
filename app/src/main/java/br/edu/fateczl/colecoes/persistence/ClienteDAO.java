package br.edu.fateczl.colecoes.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.edu.fateczl.colecoes.model.Cliente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO, ICRUDDAO<Cliente> {
    private final Context context;
    private GenericDAO genericDAO;
    private SQLiteDatabase db;

    public ClienteDAO(Context context) {
        this.context = context;
    }

    @Override
    public ClienteDAO abrir() throws SQLException {
        genericDAO = new GenericDAO(context);
        db = genericDAO.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");

        return this;
    }

    @Override
    public void fechar() {
        genericDAO.close();
    }

    private static ContentValues getContentValues(Cliente cliente) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("clienteId", cliente.getClienteId());
        contentValues.put("clienteCNPJ", cliente.getClienteCNPJ());
        contentValues.put("clienteNome", cliente.getClienteNome());
        contentValues.put("clienteEndereco", cliente.getClienteEndereco());
        contentValues.put("clienteTelefone", cliente.getClienteTelefone());

        return contentValues;
    }

    @Override
    public void registrarEntrada(Cliente cliente) throws SQLException {
        db.insert("cliente", null, getContentValues(cliente));
    }

    @Override
    public void atualizarEntrada(Cliente cliente) throws SQLException {
        db.update("cliente", getContentValues(cliente),
                "clienteId = " + cliente.getClienteId(), null);
    }

    @Override
    public void removerEntrada(Cliente cliente) throws SQLException {
        db.delete("cliente", "clienteId = " + cliente.getClienteId(), null);
    }

    @SuppressLint("Range")
    @Override
    public Cliente buscarEntrada(Cliente cliente) throws SQLException {
        String querySQL = "SELECT " +
                "clienteId, clienteCNPJ, clienteNome, clienteEndereco, clienteTelefone " +
                "FROM cliente WHERE clienteId = " + cliente.getClienteId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            cliente.setClienteId(cursor.getInt(cursor.getColumnIndex("clienteId")));
            cliente.setClienteCNPJ(cursor.getString(cursor.getColumnIndex("clienteCNPJ")));
            cliente.setClienteNome(cursor.getString(cursor.getColumnIndex("clienteNome")));
            cliente.setClienteEndereco(cursor.getString(cursor.getColumnIndex("clienteEndereco")));
            cliente.setClienteTelefone(cursor.getString(cursor.getColumnIndex("clienteTelefone")));
        }

        cursor.close();

        return cliente;
    }

    @SuppressLint("Range")
    @Override
    public List<Cliente> listarEntradas() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();

        String querySQL = "SELECT " +
                "clienteId, clienteCNPJ, clienteNome, clienteEndereco, clienteTelefone " +
                "FROM cliente ";

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            Cliente cliente = new Cliente(
                    cursor.getInt(cursor.getColumnIndex("clienteId")),
                    cursor.getString(cursor.getColumnIndex("clienteCNPJ")),
                    cursor.getString(cursor.getColumnIndex("clienteNome")),
                    cursor.getString(cursor.getColumnIndex("clienteEndereco")),
                    cursor.getString(cursor.getColumnIndex("clienteTelefone"))
            );

            clientes.add(cliente);

            cursor.moveToNext();
        }

        cursor.close();

        return clientes;
    }

    public boolean verificarSeEntradaExiste(Cliente cliente) {
        boolean existeCliente = false;

        String querySQL =
                "SELECT clienteId FROM cliente WHERE clienteId = " + cliente.getClienteId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            existeCliente = true;

            cursor.moveToNext();
        }

        cursor.close();

        return existeCliente;
    }
}
