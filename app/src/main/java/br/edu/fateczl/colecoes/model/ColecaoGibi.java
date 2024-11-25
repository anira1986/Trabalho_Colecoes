package br.edu.fateczl.colecoes.model;

public class ColecaoGibi extends Produto {
    private String editora;

    public ColecaoGibi(int idProduto, String nomeProduto, double precoProduto, String editora) {
        super(idProduto, nomeProduto, precoProduto);
        this.editora = editora;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    @Override
    public String toString() {
        return super.toString() + " - Editora: " + editora;
    }
}
