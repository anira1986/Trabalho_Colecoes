package br.edu.fateczl.colecoes.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDAO extends SQLiteOpenHelper {
    private static final String DATABASE = "COLECOES_GIBI.DB";
    private static final int DATABASE_VER = 1;
    private static final String CREATE_TABLE_CLIENTE =
            "CREATE TABLE cliente( " +
                    "clienteId INTEGER NOT NULL, " +
                    "clienteCNPJ CHAR(14) NOT NULL, " +
                    "clienteNome VARCHAR(100) NOT NULL, " +
                    "clienteEndereco VARCHAR(100) NOT NULL, " +
                    "clienteTelefone VARCHAR(13) NOT NULL, " +
                    "PRIMARY KEY (clienteId));";
    private static final String CREATE_TABLE_PRODUTO =
            "CREATE TABLE produto( " +
                    "produtoId INTEGER NOT NULL, " +
                    "produtoNome VARCHAR(100) NOT NULL, " +
                    "produtoPreco DECIMAL(10,2) NOT NULL, " +
                    "PRIMARY KEY (produtoId));";
    private static final String CREATE_TABLE_COLECAO_GIBI =
            "CREATE TABLE colecaoGibi( " +
                    "colecaoGibiId INTEGER, " +
                    "colecaoGibiEditora VARCHAR(100) NOT NULL, " +
                    "FOREIGN KEY (colecaoGibiId) REFERENCES produto(produtoId) ON DELETE CASCADE);";
    private static final String CREATE_TABLE_PEDIDO =
            "CREATE TABLE pedido( " +
                    "pedidoId INTEGER NOT NULL, " +
                    "pedidoClienteId INTEGER, " +
                    "pedidoData CHAR(8) NOT NULL, " +
                    "pedidoDataEntrega CHAR(8) NOT NULL, " +
                    "PRIMARY KEY (pedidoId), " +
                    "FOREIGN KEY (pedidoClienteId) REFERENCES cliente(clienteId) ON DELETE CASCADE);";

    private static final String CREATE_TABLE_ITEM_PEDIDO =
            "CREATE TABLE itemPedido( " +
                    "itemPedidoId INTEGER, " +
                    "itemProdutoId INTEGER, " +
                    "itemQuantidade INTEGER NOT NULL, " +
                    "PRIMARY KEY (itemPedidoId, itemProdutoId), " +
                    "FOREIGN KEY (itemPedidoId) REFERENCES pedido(pedidoId) ON DELETE CASCADE, " +
                    "FOREIGN KEY (itemProdutoId) REFERENCES produto(produtoId) ON DELETE CASCADE);";

    public GenericDAO(Context context) {
        super(context, DATABASE, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CLIENTE);
        db.execSQL(CREATE_TABLE_PRODUTO);
        db.execSQL(CREATE_TABLE_COLECAO_GIBI);
        db.execSQL(CREATE_TABLE_PEDIDO);
        db.execSQL(CREATE_TABLE_ITEM_PEDIDO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS itemPedido");
            db.execSQL("DROP TABLE IF EXISTS pedido");
            db.execSQL("DROP TABLE IF EXISTS colecaoGibi");
            db.execSQL("DROP TABLE IF EXISTS produto");
            db.execSQL("DROP TABLE IF EXISTS cliente");

            onCreate(db);
        }
    }
}
