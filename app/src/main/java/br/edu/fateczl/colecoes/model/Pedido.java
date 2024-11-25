package br.edu.fateczl.colecoes.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private double total;

    public Pedido(int idPedido, Cliente cliente) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.total = 0.0;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void addItem(ItemPedido item) {
        itens.add(item);
        total += item.getQuantidade() * item.getProduto().getPrecoProduto();
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Pedido ID: " + idPedido + " - Cliente: " + cliente.getNomeCliente() + " - Total: R$ " + total;
    }
}
