package br.edu.fateczl.colecoes.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.edu.fateczl.colecoes.model.ColecaoGibi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColecaoGibiDAO implements IColecaoGibiDAO, ICRUDDAO<ColecaoGibi> {
    private final Context context;
    private GenericDAO genericDAO;
    private SQLiteDatabase db;

    public ColecaoGibiDAO(Context context) {
        this.context = context;
    }

    @Override
    public ColecaoGibiDAO abrir() throws SQLException {
        genericDAO = new GenericDAO(context);
        db = genericDAO.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");

        return this;
    }

    @Override
    public void fechar() {
        genericDAO.close();
    }

    private static ContentValues getContentValues(ColecaoGibi colecaoGibi, boolean isSuper) {
        ContentValues contentValues = new ContentValues();
        if (isSuper) {
            contentValues.put("produtoId", colecaoGibi.getProdutoId());
            contentValues.put("produtoNome", colecaoGibi.getProdutoNome());
            contentValues.put("produtoPreco", colecaoGibi.getProdutoPreco());
        } else {
            contentValues.put("colecaoGibiId", colecaoGibi.getProdutoId());
            contentValues.put("colecaoGibiEditora", colecaoGibi.getEditora());
        }

        return contentValues;
    }

    @Override
    public void registrarEntrada(ColecaoGibi colecaoGibi) throws SQLException {
        db.insert("produto", null, getContentValues(colecaoGibi, true));
        db.insert("colecaoGibi", null, getContentValues(colecaoGibi, false));
    }

    @Override
    public void atualizarEntrada(ColecaoGibi colecaoGibi) throws SQLException {
        db.update("produto", getContentValues(colecaoGibi, true),
                "produtoId = " + colecaoGibi.getProdutoId(), null);
        db.update("colecaoGibi", getContentValues(colecaoGibi, false),
                "colecaoGibiId = " + colecaoGibi.getProdutoId(), null);
    }

    @Override
    public void removerEntrada(ColecaoGibi colecaoGibi) throws SQLException {
        db.delete("produto", "produtoId = " + colecaoGibi.getProdutoId(), null);
        db.delete("colecaoGibi", "colecaoGibiId = " + colecaoGibi.getProdutoId(), null);
    }

    @SuppressLint("Range")
    @Override
    public ColecaoGibi buscarEntrada(ColecaoGibi colecaoGibi) throws SQLException {
        String querySQL = "SELECT " +
                "produto.produtoId, produto.produtoNome, produto.produtoPreco, " +
                "colecaoGibi.colecaoGibiEditora " +
                "FROM produto, colecaoGibi " +
                "WHERE produto.produtoId = colecaoGibi.colecaoGibiId " +
                "AND colecaoGibi.colecaoGibiId = " + colecaoGibi.getProdutoId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            colecaoGibi.setProdutoId(cursor.getInt(cursor.getColumnIndex("produtoId")));
            colecaoGibi.setProdutoNome(cursor.getString(cursor.getColumnIndex("produtoNome")));
            colecaoGibi.setProdutoPreco(cursor.getDouble(cursor.getColumnIndex("produtoPreco")));
            colecaoGibi.setEditora(cursor.getString(cursor.getColumnIndex("colecaoGibiEditora")));
        }

        cursor.close();

        return colecaoGibi;
    }

    @SuppressLint("Range")
    @Override
    public List<ColecaoGibi> listarEntradas() throws SQLException {
        List<ColecaoGibi> colecoes = new ArrayList<>();

        String querySQL = "SELECT " +
                "produto.produtoId, produto.produtoNome, produto.produtoPreco, " +
                "colecaoGibi.colecaoGibiEditora " +
                "FROM produto, colecaoGibi " +
                "WHERE produto.produtoId = colecaoGibi.colecaoGibiId";

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            ColecaoGibi colecaoGibi = new ColecaoGibi(
                    cursor.getInt(cursor.getColumnIndex("produtoId")),
                    cursor.getString(cursor.getColumnIndex("produtoNome")),
                    cursor.getDouble(cursor.getColumnIndex("produtoPreco")),
                    cursor.getString(cursor.getColumnIndex("colecaoGibiEditora"))
            );

            colecoes.add(colecaoGibi);

            cursor.moveToNext();
        }

        cursor.close();

        return colecoes;
    }

    public boolean verificarSeEntradaExiste(ColecaoGibi colecaoGibi) {
        boolean existeProduto = false;

        String querySQL =
                "SELECT produto.produtoId FROM produto WHERE produto.produtoId = " + colecaoGibi.getProdutoId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            existeProduto = true;

            cursor.moveToNext();
        }

        cursor.close();

        return existeProduto;
    }

    public boolean verificarSeItemExiste(ColecaoGibi colecaoGibi) {
        boolean existeItem = false;

        String querySQL =
                "SELECT itemProdutoId FROM item WHERE itemProdutoId = " + colecaoGibi.getProdutoId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            existeItem = true;

            cursor.moveToNext();
        }

        cursor.close();

        return existeItem;
    }
}
