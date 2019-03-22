package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.planejamento.josephisaac.weudesmonteirovendas.BancoDados.Banco;
import com.planejamento.josephisaac.weudesmonteirovendas.R;

public class VendasNovoTotal extends Activity {

    private SimpleCursorAdapter cursorAdapter;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Cursor cursorAux;
    private Cursor cProd;
    private Cursor cursorProd;
    private Cursor cClien;
    private Cursor cursorClien;
    private ListView listView;
    private EditText etDate;
    private TextView nomeCliente;
    private Button btnProduto;
    private String idVendas;
    private String idProduto;
    private String idCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas_novo_total);

        btnProduto = (Button) findViewById(R.id.idVendasNovoTotalAddProduto);
        nomeCliente = (TextView) findViewById(R.id.idVendasNovoTotalNome);

        Intent it = getIntent();

        if(it != null){
            Bundle params = it.getExtras();

            if(params != null){
                idVendas = params.getString("idVenda");
            }
        }

        buscarDadosVendas();

        btnProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), VendasNovoProduto.class);

                i.putExtra("id", idCliente(idVendas));
                startActivity(i);
            }
        });

        nomeCliente.setText(NomeCliente(idCliente(idVendas)));
        Toast.makeText(VendasNovoTotal.this, NomeCliente(idCliente(idVendas)), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent it = getIntent();

        if(it != null){
            Bundle params = it.getExtras();

            if(params != null){
                idVendas = params.getString("idVenda");
                Toast.makeText(VendasNovoTotal.this, "idVenda "+idVendas, Toast.LENGTH_SHORT).show();
            }
        }

        buscarDadosVendas();
    }

    String idProduto(String idVenda){

        String idVendasProduto = null;
        cursorProd = null;

        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            cursorProd = db.rawQuery("SELECT "+Banco.VENDAS_PRODUTO_ID+" FROM "+Banco.TABELA_VENDAS+" WHERE "+Banco.VENDAS_ID+" = "+idVenda,null);
            cursorProd.moveToFirst();

            idVendasProduto = cursorProd.getString(cursorProd.getColumnIndex(Banco.VENDAS_PRODUTO_ID));

        }catch (Exception e){
            if(e.getMessage().toString() != null)
                Log.i("Erro Id Produto", e.getMessage().toString());
        }

        return idVendasProduto;
    }

    String idCliente(String idVenda){

        String idVendasCliente = null;

        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            cursorClien = db.rawQuery("SELECT "+Banco.VENDAS_CLIENTE_ID+" FROM "+Banco.TABELA_VENDAS+" WHERE "+Banco.VENDAS_ID+" = "+idVenda,null);
            cursorClien.moveToFirst();

            idVendasCliente = cursorClien.getString(cursorClien.getColumnIndex(Banco.VENDAS_CLIENTE_ID));

        }catch (Exception e){
            if(e.getMessage().toString() != null)
                Log.i("Erro Id Produto", e.getMessage().toString());
        }

        return idVendasCliente;
    }

    String NomeCliente(String idDoCliente){

        String nomeCliente = null;

        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            cursor = db.rawQuery("SELECT "+Banco.CLIENTE_NOME+" FROM "+Banco.CLIENTE_TABELA+" WHERE "+Banco.CLIENTE_ID+" = "+idDoCliente,null);
            cursor.moveToFirst();

            nomeCliente = cursor.getString(cursor.getColumnIndex(Banco.CLIENTE_NOME));

        }catch (Exception e){
            if(e.getMessage().toString() != null)
                Log.i("ERRO NOME CLIENTE", e.getMessage());
        }

        return nomeCliente;
    }

    private void criarListagem() {
        listView = (ListView) findViewById(R.id.idNovaVendaTotalLV);

        String[] from = {Banco.PRODUTO_FOTO, Banco.PRODUTO_NOME, Banco.VENDAS_PRECO, Banco.VENDAS_PAGO, Banco.VENDAS_DATA};
        int[] to = {R.id.imageView, R.id.textView13, R.id.idAPagar, R.id.idJaPago, R.id.idDataDaVenda};

        cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.vendas_novo_total_lista_produtos, cursor, from, to);

        listView.setAdapter(cursorAdapter);

    }

    private void buscarDadosVendas() {
        db = null;
        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, Context.MODE_PRIVATE, null);

            String name;
            String photo;

            cursorAux = db.rawQuery("SELECT * FROM "+Banco.TABELA_VENDAS+
                    " WHERE "+Banco.VENDAS_ID+" = '"+idVendas+"'", null);

            cursorAux.moveToFirst();

            idProduto = cursorAux.getString(cursorAux.getColumnIndex(Banco.VENDAS_PRODUTO_ID));
            idCliente = cursorAux.getString(cursorAux.getColumnIndex(Banco.VENDAS_CLIENTE_ID));

            cClien = db.rawQuery("SELECT * FROM "+Banco.CLIENTE_TABELA+" WHERE "+Banco.CLIENTE_ID+" = "+idCliente,null);
            cProd = db.rawQuery("SELECT * FROM "+Banco.PRODUTO_TABELA+" WHERE "+Banco.PRODUTO_ID+" = "+idProduto,null);

            cClien.moveToFirst();
            cProd.moveToFirst();

            name = cClien.getString(cClien.getColumnIndex(Banco.CLIENTE_ID));
            photo = cProd.getString(cProd.getColumnIndex(Banco.PRODUTO_ID));

            cursor = db.rawQuery("SELECT * FROM "+Banco.TABELA_VENDAS+", "+Banco.PRODUTO_TABELA+", "+Banco.CLIENTE_TABELA
                    +" WHERE "+Banco.CLIENTE_TABELA+"."+Banco.CLIENTE_ID+" = '"+name+"', "+Banco.PRODUTO_TABELA+"."
                    +Banco.PRODUTO_ID+" = '"+photo+"'", null);

            criarListagem();

        }catch (Exception e){
            e.printStackTrace();
            Log.i("BuscaDadosProduto: ", e.getMessage());
            Toast.makeText(getApplicationContext(), "Erro ao buscar dados dos Produtos", Toast.LENGTH_SHORT).show();
        }
    }
}