package com.planejamento.josephisaac.weudesmonteirovendas.BancoDados;

public class Banco {

    public static final String NOME_BANCO = "bancoWeudesMonteiroVendas.db";
    public static final int VERSAO = 1;

    public static final String CLIENTE_TABELA = "tabela_clientes";
    public static final String CLIENTE_ID = "_id";
    public static final String CLIENTE_NOME = "nomeCliente";
    public static final String CLIENTE_ENDERECO = "enrederecoCliente";
    public static final String CLIENTE_TELEFONE = "telefoneCliente";

    public static final String PRODUTO_TABELA = "tabela_produto";
    public static final String PRODUTO_ID = "_id";
    public static final String PRODUTO_NOME = "nomeProduto";
    public static final String PRODUTO_CUSTO = "precoCusto";
    public static final String PRODUTO_VENDA = "precoVenda";
    public static final String PRODUTO_FOTO = "foto";
    public static final String PRODUTO_ESTOQUE = "estoque";

    public static final String TABELA_VENDAS = "tabela_vendas";
    public static final String VENDAS_ID = "_id";
    public static final String VENDAS_PRODUTO_ID = "_id_vendas_produto";
    public static final String VENDAS_CLIENTE_ID = "_id_vendas_cliente";
    public static final String VENDAS_PRECO = "precoVenda";
    public static final String VENDAS_DATA = "dataVenda";
    public static final String VENDAS_PAGO = "precoPago";

}