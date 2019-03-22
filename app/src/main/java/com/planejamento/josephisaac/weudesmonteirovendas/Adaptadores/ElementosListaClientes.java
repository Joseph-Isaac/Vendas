package com.planejamento.josephisaac.weudesmonteirovendas.Adaptadores;

public class ElementosListaClientes {
    private String nome;
    private String endereco;
    private long telefone = Long.parseLong(null);

    public ElementosListaClientes(String endereco, String nome, long telefone) {
        this.endereco = endereco;
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getTelefone() {
        return telefone;
    }

    public void setTelefone(long telefone) {
        this.telefone = telefone;
    }
}