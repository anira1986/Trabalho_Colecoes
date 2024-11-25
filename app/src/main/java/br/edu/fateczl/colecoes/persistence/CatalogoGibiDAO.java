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

public class CatalogoGibiDAO implements ICatalogoGibiDAO, ICRUDDAO<ColecaoGibi> {
    private final Context context;
    private GenericDAO genericDAO;
    private SQLiteDatabase db;

    public CatalogoGibiDAO(Context context) {
        this.context = context;
    }

    @Override
    public CatalogoGibiDAO open() throws SQLException {
        genericDAO = new GenericDAO(context);
        db = genericDAO.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
        return this;
    }

    @Override
    public void close() {
        genericDAO.close();
    }

    private static ContentValues getContentValues(ColecaoGibi colecaoGibi, boolean isSuper) {
        ContentValues contentValues = new ContentValues();
        if (isSuper) {
            contentValues.put("produtoId", colecaoGibi.getProductId());
            contentValues.put("produtoNome", colecaoGibi.getProductName());
            contentValues.put("produtoPreco", colecaoGibi.getProductPrice());
        } else {
            contentValues.put("catalogoGibiId", colecaoGibi.getProductId());
            contentValues.put("editora", colecaoGibi.getEditora());
        }
        return contentValues;
    }

    @Override
    public void registerEntry(ColecaoGibi colecaoGibi) throws SQLException {
        db.insert("produto", null, getContentValues(colecaoGibi, true));
        db.insert("catalogoGibi", null, getContentValues(colecaoGibi, false));
    }

    @Override
    public void updateEntry(ColecaoGibi colecaoGibi) throws SQLException {
        db.update("produto", getContentValues(colecaoGibi, true),
                "produtoId = " + colecaoGibi.getProductId(), null);
        db.update("catalogoGibi", getContentValues(colecaoGibi, false),
                "catalogoGibiId = " + colecaoGibi.getProductId(), null);
    }

    @Override
    public void removeEntry(ColecaoGibi colecaoGibi) throws SQLException {
        db.delete("produto", "produtoId = " + colecaoGibi.getProductId(), null);
        db.delete("catalogoGibi", "catalogoGibiId = " + colecaoGibi.getProductId(), null);
    }

    @SuppressLint("Range")
    @Override
    public ColecaoGibi searchEntry(ColecaoGibi colecaoGibi) throws SQLException {
        String querySQL = "SELECT " +
                "produto.produtoId, produto.produtoNome, produto.produtoPreco, " +
                "catalogoGibi.editora " +
                "FROM produto, catalogoGibi " +
                "WHERE produto.produtoId = catalogoGibi.catalogoGibiId " +
                "AND catalogoGibi.catalogoGibiId = " + colecaoGibi.getProductId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            colecaoGibi.setProductId(cursor.getInt(cursor.getColumnIndex("produtoId")));
            colecaoGibi.setProductName(cursor.getString(cursor.getColumnIndex("produtoNome")));
            colecaoGibi.setProductPrice(cursor.getDouble(cursor.getColumnIndex("produtoPreco")));
            colecaoGibi.setEditora(cursor.getString(cursor.getColumnIndex("editora")));
        }

        cursor.close();

        return colecaoGibi;
    }

    @SuppressLint("Range")
    @Override
    public List<ColecaoGibi> listEntry() throws SQLException {
        List<ColecaoGibi> catalogos = new ArrayList<>();

        String querySQL = "SELECT " +
                "produto.produtoId, produto.produtoNome, produto.produtoPreco, " +
                "catalogoGibi.editora " +
                "FROM produto, catalogoGibi " +
                "WHERE produto.produtoId = catalogoGibi.catalogoGibiId ";

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            ColecaoGibi catalogo = new ColecaoGibi(
                    cursor.getInt(cursor.getColumnIndex("produtoId")),
                    cursor.getString(cursor.getColumnIndex("produtoNome")),
                    cursor.getDouble(cursor.getColumnIndex("produtoPreco")),
                    cursor.getString(cursor.getColumnIndex("editora"))
            );

            catalogos.add(catalogo);

            cursor.moveToNext();
        }

        cursor.close();

        return catalogos;
    }

    public boolean checkIfEntryExist(ColecaoGibi colecaoGibi) {
        boolean existProduct = false;

        String querySQL =
                "SELECT produto.produtoId FROM produto WHERE produto.produtoId = " + colecaoGibi.getProductId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            existProduct = true;

            cursor.moveToNext();
        }

        cursor.close();

        return existProduct;
    }

    public boolean checkIfItemExist(ColecaoGibi colecaoGibi) {
        boolean existItem = false;

        String querySQL =
                "SELECT itemProdutoId FROM itemPedido WHERE itemProdutoId = " + colecaoGibi.getProductId();

        Cursor cursor = db.rawQuery(querySQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (!cursor.isAfterLast()) {
            existItem = true;

            cursor.moveToNext();
        }

        cursor.close();

        return existItem;
    }
}
