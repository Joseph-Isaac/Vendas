package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.planejamento.josephisaac.weudesmonteirovendas.BancoDados.Banco;
import com.planejamento.josephisaac.weudesmonteirovendas.R;

public class Vendas extends Activity {

    private Button button;
    private ListView listView;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter cursorAdapter;
    private AlertDialog.Builder caixa;
    private Button pesquisa;
    private EditText pesquisaEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas);

        button = (Button) findViewById(R.id.idBotaoVendas);
        listView = (ListView)findViewById(R.id.idListaVendas);
        pesquisa = (Button) findViewById(R.id.pesquisarVenda);
        pesquisaEdt = (EditText) findViewById(R.id.edPesquisaVenda);

        PesquisarVendas();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vendas.this, VendasNovoCliente.class));
                finish();
            }
        });

        pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pesquisarText = pesquisaEdt.getText().toString();

                funcaoPesquisar(pesquisarText);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SQLiteCursor sqLiteCursor = (SQLiteCursor) cursorAdapter.getItem(i);
                final String id = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.VENDAS_ID));
                final String idCliente = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.VENDAS_CLIENTE_ID));
                final String idProduto = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.VENDAS_PRODUTO_ID));
                final String preco = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.VENDAS_PRECO));
                final String pago = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.VENDAS_PAGO));
                final String data = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.VENDAS_DATA));

                String nome = pesquisarNome(idCliente);
                String produto = pesquisarProduto(idProduto);

                caixa = new AlertDialog.Builder(Vendas.this);

                caixa.setMessage("Cliente: "+nome+"\nProduto: "+produto+"\nValor: R$"+preco+"\tPago: R$:"+pago);

                caixa.setPositiveButton("EDITAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Vendas.this, VendasEditar.class);

                                intent.putExtra("id", id);
                                intent.putExtra("idCliente", idCliente);
                                intent.putExtra("idProduto", idProduto);
                                intent.putExtra("pago", pago);
                                intent.putExtra("preco", preco);
                                intent.putExtra("data", data);

                                startActivity(intent);
                            }
                        });

                caixa.create();
                caixa.show();

            }
        });
    }

    private void funcaoPesquisar(String pesquisarText) {
        if(pesquisarText.length() < 1){
            PesquisarVendas();
        }else{
            pesquisarVendas(pesquisarText);
            criarListagem();
            pesquisaEdt.setText("");
            Toast.makeText(Vendas.this, "Clique novamente em \"Pesquisar\" para mostrar todos os Produtos", Toast.LENGTH_SHORT).show();
        }
    }

    private void pesquisarVendas(String pesquisarText) {
        try{


            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * FROM "+Banco.TABELA_VENDAS+" INNER JOIN "+Banco.CLIENTE_TABELA+
                    " ON "+Banco.VENDAS_CLIENTE_ID+" = "+Banco.CLIENTE_TABELA+"."+Banco.CLIENTE_ID+" INNER JOIN "
                    +Banco.PRODUTO_TABELA+" ON "+Banco.VENDAS_PRODUTO_ID+" = "+Banco.PRODUTO_TABELA+"."
                    +Banco.PRODUTO_ID+" WHERE "+Banco.PRODUTO_NOME+" LIKE '%"+pesquisarText+"%'" +
                    " OR "+Banco.CLIENTE_NOME+" LIKE '%"+pesquisarText+"%' ORDER BY "+Banco.VENDAS_DATA+" ASC", null);

            criarListagem();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("BuscaDadosVenda: ", String.valueOf(e));
        }
    }

    private void removerVenda(String id) {

        try {

            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            db.execSQL("DELETE FROM " + Banco.TABELA_VENDAS + " WHERE " + Banco.VENDAS_ID + " = " + id);
            PesquisarVendas();

            Toast.makeText(getApplicationContext(), "Venda Removida", Toast.LENGTH_SHORT).show();

        }catch (Exception e){

            Log.e("ErroRV", String.valueOf(e));
            Toast.makeText(Vendas.this, "Erro ao deletar", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        PesquisarVendas();
    }


    private String pesquisarProduto(String idProduto) {
        Cursor c;
        String produto = null;

        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            c = db.rawQuery("SELECT * FROM "+ Banco.PRODUTO_TABELA+" WHERE "+Banco.PRODUTO_ID+ " = "+idProduto, null);
            c.moveToFirst();

            produto = c.getString(c.getColumnIndex(Banco.PRODUTO_NOME));

        }catch (Exception e){

            Log.e("ErroNome",String.valueOf(e));
            Toast.makeText(Vendas.this, "Erro Ao Pesquisar Nome do Produto", Toast.LENGTH_SHORT).show();
            Toast.makeText(Vendas.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }

        return produto;
    }

    private String pesquisarNome(String idCliente) {
        Cursor c;
        String nome = null;

        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            c = db.rawQuery("SELECT * FROM "+ Banco.CLIENTE_TABELA+" WHERE "+Banco.CLIENTE_ID+ " = "+idCliente, null);
            c.moveToFirst();

            nome = c.getString(c.getColumnIndex(Banco.CLIENTE_NOME));

        }catch (Exception e){

            Log.e("ErroNome",String.valueOf(e));
            Toast.makeText(Vendas.this, "Erro Ao Pesquisar Nome do Cliente", Toast.LENGTH_SHORT).show();
            Toast.makeText(Vendas.this, String.valueOf(e), Toast.LENGTH_SHORT).show();

        }

        return nome;
    }

    private void PesquisarVendas() {
        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * FROM "+Banco.TABELA_VENDAS+" INNER JOIN "+Banco.CLIENTE_TABELA+
                    " ON "+Banco.VENDAS_CLIENTE_ID+" = "+Banco.CLIENTE_TABELA+"."+Banco.CLIENTE_ID+" INNER JOIN "
                    +Banco.PRODUTO_TABELA+" ON "+Banco.VENDAS_PRODUTO_ID+" = "+Banco.PRODUTO_TABELA+"."
                    +Banco.PRODUTO_ID+" ORDER BY "+Banco.VENDAS_DATA+" DESC", null);

            criarListagem();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("BuscaDadosVenda: ", String.valueOf(e));
        }
    }

    private void criarListagem() {

        String nomeCliente = null;
        String nomeProduto = null;

        try{
            /*db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            cursorAux = db.rawQuery("SELECT * FROM "+Banco.CLIENTE_TABELA,null);
            cursorAux.moveToFirst();
            //cursor.moveToFirst();
            nomeCliente = cursorAux.getString(cursorAux.getColumnIndex(cursor.getString(cursor.getColumnIndex(Banco.VENDAS_CLIENTE_ID))));

            cursorAux = db.rawQuery("SELECT * FROM "+Banco.PRODUTO_TABELA, null);
            cursorAux.moveToFirst();
            //cursor.moveToFirst();
            nomeProduto = cursorAux.getString(cursorAux.getColumnIndex(cursor.getString(cursor.getColumnIndex(Banco.VENDAS_PRODUTO_ID))));
            */
        }catch (Exception e){
            e.getMessage();
            Log.e("ErroListaVendas", e.toString());
        }


        String[] from ={Banco.CLIENTE_NOME, Banco.PRODUTO_NOME, Banco.PRODUTO_VENDA,Banco.VENDAS_DATA, Banco.VENDAS_PAGO};
        int[] to ={R.id.textView15, R.id.textView17, R.id.textView24, R.id.textView19, R.id.textView28};

        cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.vendas_lista, cursor, from, to);
        listView.setAdapter(cursorAdapter);

    }


}
