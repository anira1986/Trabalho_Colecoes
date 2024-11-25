package br.edu.fateczl.colecoes.model;

import androidx.annotation.NonNull;

public class ProdutoGibi extends Produto {
    private String generoGibi;
    private boolean edicaoLimitada;

    public ProdutoGibi(int idProduto, String nomeProduto, double precoProduto, String generoGibi, boolean edicaoLimitada) {
        super(idProduto, nomeProduto, precoProduto);
        this.generoGibi = generoGibi;
        this.edicaoLimitada = edicaoLimitada;
    }

    public String getGeneroGibi() {
        return generoGibi;
    }

    public void setGeneroGibi(String generoGibi) {
        this.generoGibi = generoGibi;
    }

    public boolean isEdicaoLimitada() {
        return edicaoLimitada;
    }

    public void setEdicaoLimitada(boolean edicaoLimitada) {
        this.edicaoLimitada = edicaoLimitada;
    }

    @NonNull
    @Override
    public String toString() {
        return getNomeProduto() + " - R$ " + String.format("%.2f", getPrecoProduto()) +
                (edicaoLimitada ? " (Edição Limitada)" : "");
    }
}
