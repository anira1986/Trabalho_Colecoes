package br.edu.fateczl.colecoes.model;

public class ItemPedido {
    private int idPedido;
    private Produto produto;
    private int quantidade;

    public ItemPedido(int idPedido, Produto produto, int quantidade) {
        this.idPedido = idPedido;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return produto.getNomeProduto() + " - Quantidade: " + quantidade;
    }
}
