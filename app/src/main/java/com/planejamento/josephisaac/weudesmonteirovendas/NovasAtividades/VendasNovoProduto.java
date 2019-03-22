package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.planejamento.josephisaac.weudesmonteirovendas.BancoDados.Banco;
import com.planejamento.josephisaac.weudesmonteirovendas.R;

public class VendasNovoProduto extends AppCompatActivity {

    private ListView listView;
    private CursorAdapter cursorAdapter;
    private Cursor cursor;
    private Intent intent;
    private SQLiteDatabase db;
    private EditText editText;
    private Button btnPesquisar;
    private Button btnNovo;
    private Button continuar;
    private String id;
    private String idRecebido;
    private String nome;
    private String venda;
    private String preco;
    private String foto;
    private String estoque;
    private String idVenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas_novo_produto);

        editText = (EditText) findViewById(R.id.idVendasNovoProdutoPesquisarEdt);
        btnPesquisar = (Button) findViewById(R.id.idVendasNovoProdutoPesquisarBtn);
        btnNovo = (Button) findViewById(R.id.idVendasNovoProdutoNovoBtn);
        continuar = (Button) findViewById(R.id.idVendasNovoProdutoContinuar);

        criarListagem();
        buscarDadosProduto();

        Intent it = getIntent();

        if(it != null){
            Bundle params = it.getExtras();

            if(params != null){
              idRecebido = params.getString("idCliente");

                //Toast.makeText(getApplicationContext(), idRecebido+" ", Toast.LENGTH_LONG).show();
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SQLiteCursor sqLiteCursor = (SQLiteCursor) cursorAdapter.getItem(i);
                id = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_ID));
                nome = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_NOME));
                venda = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_VENDA));
                preco = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_CUSTO));
                foto = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_FOTO));
                estoque = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_ESTOQUE));

                if(idRecebido != null && id != null){
                    try{
                        db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

                        String sql = "INSERT INTO "+Banco.TABELA_VENDAS+"("+Banco.VENDAS_CLIENTE_ID+
                                ", "+Banco.VENDAS_PRODUTO_ID+") VALUES ("+idRecebido+", "+id+")";

                        db.execSQL(sql);

                        idVenda = passarDados(idRecebido, id);

                        intent = new Intent(getApplicationContext(), VendasNovoDetalheDaVenda.class);

                        intent.putExtra("idCliente", idRecebido);
                        intent.putExtra("idProduto", id);
                        intent.putExtra("idVenda", idVenda);

                        startActivity(intent);
                        db.close();
                        finish();

                    }catch (Exception e){
                        Log.i("idRecebido", e.getMessage());
                    }
                }else{
                    Toast.makeText(VendasNovoProduto.this, "Selecione um Produto", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pesquisarText = editText.getText().toString();

                funcaoPesquisar(pesquisarText);
            }
        });

        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProdutosNovo.class));
            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(idRecebido != null && id != null){
                   try{
                       db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

                       String sql = "INSERT INTO "+Banco.TABELA_VENDAS+"("+Banco.VENDAS_CLIENTE_ID+
                               ", "+Banco.VENDAS_PRODUTO_ID+") VALUES ("+idRecebido+", "+id+")";

                       db.execSQL(sql);

                       idVenda = passarDados(idRecebido, id);

                       intent = new Intent(getApplicationContext(), VendasNovoDetalheDaVenda.class);

                       intent.putExtra("idCliente", idRecebido);
                       intent.putExtra("idProduto", id);
                       intent.putExtra("idVenda", idVenda);

                       startActivity(intent);
                       db.close();
                       finish();

                   }catch (Exception e){
                       Log.i("idRecebido", e.getMessage());
                   }
                }else{
                    Toast.makeText(VendasNovoProduto.this, "Selecione um Produto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        criarListagem();
        buscarDadosProduto();
    }

    private void criarListagem() {
        listView = (ListView) findViewById(R.id.idVendasNovoProdutoLV);

        String[] from = {Banco.PRODUTO_NOME, Banco.PRODUTO_VENDA, Banco.PRODUTO_FOTO, Banco.PRODUTO_ESTOQUE};
        int[] to = {R.id.idNomeProduto, R.id.idPrecoVendaLista, R.id.idImagemProduto, R.id.idEstoqueListaProdutos};

        cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.produtos_lista, cursor, from, to);
        listView.setAdapter(cursorAdapter);
    }

    private void funcaoPesquisar(String pesquisarText) {
        if(pesquisarText.length() < 1){
            buscarDadosProduto();
            criarListagem();
        }else{
            pesquisarDadosProduto(pesquisarText);
            criarListagem();
            editText.setText("");
            Toast.makeText(getApplicationContext(), "Clique novamente em Pesquisar para mostrar todos os Produtos", Toast.LENGTH_SHORT).show();
        }
    }

    private String passarDados(String id_cliente, String id_produto){

        Cursor cursorAux =  null;

        try{

            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT "+Banco.VENDAS_ID+", "+Banco.VENDAS_CLIENTE_ID+", "+Banco.VENDAS_PRODUTO_ID+
                    " FROM "+Banco.TABELA_VENDAS+" WHERE "+Banco.VENDAS_CLIENTE_ID+" = '"+id_cliente+
                    "' AND "+Banco.VENDAS_PRODUTO_ID+" = '"+id_produto+"'",null);

            cursorAux = db.rawQuery("SELECT count(*) FROM "+Banco.TABELA_VENDAS, null);

            cursor.moveToLast();

            Toast.makeText(VendasNovoProduto.this, "ultimo: "+cursor.getString(cursor.getColumnIndex(Banco.VENDAS_ID)), Toast.LENGTH_LONG).show();
            Toast.makeText(VendasNovoProduto.this, "count: "+cursorAux.getString(cursor.getColumnIndex("count")), Toast.LENGTH_LONG).show();



        }catch (Exception e){
            Log.i("passarDados", e.getMessage());
        }

        return cursor.getString(cursor.getColumnIndex(Banco.VENDAS_ID));
    }

    private void pesquisarDadosProduto(String pesquisarText) {
        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * FROM "+ Banco.PRODUTO_TABELA+" WHERE "+Banco.PRODUTO_NOME+" LIKE '%"+pesquisarText+"%' ORDER BY "+Banco.PRODUTO_NOME+" ASC", null);

        }catch (Exception e){
            e.printStackTrace();
            Log.d("BuscaDadosProdutos: ", String.valueOf(e));
            Toast.makeText(getApplicationContext(), "Erro ao buscar dados dos produtos", Toast.LENGTH_LONG).show();
        }
    }

    private void buscarDadosProduto() {
        try{

            db = openOrCreateDatabase(Banco.NOME_BANCO, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * FROM "+ Banco.PRODUTO_TABELA+"  ORDER BY "+Banco.PRODUTO_NOME+" ASC", null);

        }catch (Exception e){
            e.printStackTrace();
            Log.i("BuscaDadosProduto: ", String.valueOf(e));
            Toast.makeText(getApplicationContext(), "Erro ao buscar dados dos Produtos", Toast.LENGTH_SHORT).show();
        }
    }
}