package br.edu.fateczl.colecoes.model;

import androidx.annotation.NonNull;

public class Cliente {
    private int idCliente;
    private String cnpjCliente;
    private String nomeCliente;
    private String enderecoCliente;
    private String telefoneCliente;

    public Cliente(int idCliente, String cnpjCliente, String nomeCliente,
                   String enderecoCliente, String telefoneCliente) {
        this.idCliente = idCliente;
        this.cnpjCliente = cnpjCliente;
        this.nomeCliente = nomeCliente;
        this.enderecoCliente = enderecoCliente;
        this.telefoneCliente = telefoneCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getCnpjCliente() {
        return cnpjCliente;
    }

    public void setCnpjCliente(String cnpjCliente) {
        this.cnpjCliente = cnpjCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(String enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(idCliente) + " - " + nomeCliente;
    }
}
