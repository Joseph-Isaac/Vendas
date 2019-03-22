package com.planejamento.josephisaac.weudesmonteirovendas;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.planejamento.josephisaac.weudesmonteirovendas.Adaptadores.AdaptadorGridView;
import com.planejamento.josephisaac.weudesmonteirovendas.BancoDados.Banco;
import com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades.Clientes;
import com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades.Produtos;
import com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades.Vendas;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        criaTabelaProdutos();
        CriarTabelaClientes();
        criaTabelaVendas();

        int[] lista = new int[]{R.drawable.clientes_button, R.drawable.produtos_button, R.drawable.vendas_button};

        GridView gridView = (GridView) findViewById(R.id.idGridView);
        gridView.setAdapter(new AdaptadorGridView(getApplicationContext(), lista));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(MainActivity.this, Clientes.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, Produtos.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, Vendas.class));
                        break;
                    default:
                        finish();
                }
            }
        });

    }

    private void criaTabelaProdutos() {
        SQLiteDatabase db = null;

        try {
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            String sqlProdutos = "CREATE TABLE IF NOT EXISTS " + Banco.PRODUTO_TABELA + "("
                    + Banco.PRODUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Banco.PRODUTO_NOME + " TEXT, "
                    + Banco.PRODUTO_CUSTO + " REAL, "
                    + Banco.PRODUTO_VENDA + " REAL, "
                    + Banco.PRODUTO_FOTO + " TEXT, "
                    + Banco.PRODUTO_ESTOQUE + " INTEGER "
                    + ")";

            db.execSQL(sqlProdutos);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erro ao criar tabela dos produtos", Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
        }
    }

    private void CriarTabelaClientes() {
        SQLiteDatabase db = null;

        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            String sqlClientes = "CREATE TABLE IF NOT EXISTS " +Banco.CLIENTE_TABELA+"("
                    +Banco.CLIENTE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +Banco.CLIENTE_NOME+" TEXT, "
                    +Banco.CLIENTE_ENDERECO+" TEXT, "
                    +Banco.CLIENTE_TELEFONE+" INTEGER"
                    +")";

            db.execSQL(sqlClientes);

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Erro ao criar tabela Clientes", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    private void criaTabelaVendas() {
        SQLiteDatabase db = null;
        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            String sqlVendas = "CREATE TABLE IF NOT EXISTS "+Banco.TABELA_VENDAS+"("
                    +Banco.VENDAS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +Banco.VENDAS_CLIENTE_ID+" INTEGER, "
                    +Banco.VENDAS_PRODUTO_ID+" INTEGER, "
                    +Banco.VENDAS_DATA+" TEXT, "
                    +Banco.VENDAS_PRECO+" REAL, "
                    +Banco.VENDAS_PAGO+" REAL "
                    //+"FOREIGN KEY("+Banco.VENDAS_CLIENTE_ID+") REFERENCES "+Banco.CLIENTE_TABELA+"("+Banco.CLIENTE_ID+"), "
                    //+"FOREIGN KEY("+Banco.VENDAS_PRODUTO_ID+") REFERENCES "+Banco.PRODUTO_TABELA+"("+Banco.PRODUTO_ID+")"
                    +")";

            db.execSQL(sqlVendas);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }
}
