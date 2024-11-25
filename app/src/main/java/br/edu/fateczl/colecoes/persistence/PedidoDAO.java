package br.edu.fateczl.colecoes.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.edu.fateczl.colecoes.model.Cliente;
import br.edu.fateczl.colecoes.model.ColecaoGibi;
import br.edu.fateczl.colecoes.model.ItemPedido;
import br.edu.fateczl.colecoes.model.Pedido;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements IColecaoGibiPedidoDAO, ICRUDDAO<Pedido> {
    private final Context context;
    private GenericDAO genericDAO;
    private SQLiteDatabase db;

    public PedidoDAO(Context context) {
        this.context = context;
    }

    @Override
    public PedidoDAO open() throws SQLException {
        genericDAO = new GenericDAO(context);
        db = genericDAO.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
        return this;
    }

    @Override
    public void close() {
        genericDAO.close();
    }

    private static ContentValues getPedidoContentValues(Pedido pedido) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("pedidoId", pedido.getIdPedido());
        contentValues.put("clienteId", pedido.getCliente().getIdCliente());
        contentValues.put("dataPedido", pedido.getDataPedido().toString());
        contentValues.put("dataEntrega", pedido.getDataEntrega().toString());
        return contentValues;
    }

    private static ContentValues getItemContentValues(ItemPedido item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("pedidoId", item.getIdPedido());
        contentValues.put("gibiId", item.getColecaoGibi().getIdGibi());
        contentValues.put("quantidade", item.getQuantidade());
        return contentValues;
    }

    @Override
    public void registerEntry(Pedido pedido) throws SQLException {
        db.insert("pedido", null, getPedidoContentValues(pedido));
        for (ItemPedido item : pedido.getItensPedido()) {
            db.insert("item_pedido", null, getItemContentValues(item));
        }
    }

    @Override
    public void updateEntry(Pedido pedido) throws SQLException {
        db.update("pedido", getPedidoContentValues(pedido), "pedidoId = ?", new String[]{String.valueOf(pedido.getIdPedido())});
        db.delete("item_pedido", "pedidoId = ?", new String[]{String.valueOf(pedido.getIdPedido())});
        for (ItemPedido item : pedido.getItensPedido()) {
            db.insert("item_pedido", null, getItemContentValues(item));
        }
    }

    @Override
    public void removeEntry(Pedido pedido) throws SQLException {
        db.delete("item_pedido", "pedidoId = ?", new String[]{String.valueOf(pedido.getIdPedido())});
        db.delete("pedido", "pedidoId = ?", new String[]{String.valueOf(pedido.getIdPedido())});
    }

    @SuppressLint("Range")
    @Override
    public Pedido searchEntry(Pedido pedido) throws SQLException {
        String query = "SELECT * FROM pedido WHERE pedidoId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(pedido.getIdPedido())});
        if (cursor != null && cursor.moveToFirst()) {
            Cliente cliente = new Cliente(
                    cursor.getInt(cursor.getColumnIndex("clienteId")),
                    "", "", "", ""
            );
            LocalDate dataPedido = LocalDate.parse(cursor.getString(cursor.getColumnIndex("dataPedido")));
            LocalDate dataEntrega = LocalDate.parse(cursor.getString(cursor.getColumnIndex("dataEntrega")));
            pedido.setCliente(cliente);
            pedido.setDataPedido(dataPedido);
            pedido.setDataEntrega(dataEntrega);
        }
        if (cursor != null) {
            cursor.close();
        }
        return pedido;
    }

    @Override
    public List<Pedido> listEntry() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String query = "SELECT * FROM pedido";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Pedido pedido = new Pedido(
                        cursor.getInt(cursor.getColumnIndex("pedidoId")),
                        new Cliente(cursor.getInt(cursor.getColumnIndex("clienteId")), "", "", "", ""),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndex("dataPedido")))
                );
                pedidos.add(pedido);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return pedidos;
    }

    @Override
    public boolean checkIfEntryExist(Pedido pedido) throws SQLException {
        String query = "SELECT pedidoId FROM pedido WHERE pedidoId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(pedido.getIdPedido())});
        boolean exists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }
}
