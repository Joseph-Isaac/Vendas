package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.planejamento.josephisaac.weudesmonteirovendas.BancoDados.Banco;
import com.planejamento.josephisaac.weudesmonteirovendas.R;

import java.io.IOException;
import java.io.StringReader;

public class Produtos extends Activity {

    private Button button;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView listView;
    private SimpleCursorAdapter cursorAdapter;
    private AlertDialog.Builder mBuilder;
    private View mView;
    private AlertDialog.Builder alertaRemover;
    private Button pesquisa;
    private EditText pesquisaEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produtos);

        button = (Button) findViewById(R.id.idBotaoProdutos);
        pesquisa = (Button) findViewById(R.id.idProdutosPesquisaBotton);
        pesquisaEdt = (EditText) findViewById(R.id.idProdutosPesquisaText);

        criarListagem();
        buscarDadosProdutos();

        pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pesquisarText = pesquisaEdt.getText().toString();

                funcaoPesquisar(pesquisarText);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Produtos.this, ProdutosNovo.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SQLiteCursor sqLiteCursor = (SQLiteCursor) cursorAdapter.getItem(i);
                final String id = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_ID));
                final String nome = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_NOME));
                final String venda = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_VENDA));
                final String preco = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_CUSTO));
                //final String foto = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_FOTO));
                final String estoque = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.PRODUTO_ESTOQUE));

                /*Bitmap mPhoto = null;

                try{
                    mPhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(foto));
                }catch (IOException e){
                    e.printStackTrace();
                }*/

                mBuilder = new AlertDialog.Builder(Produtos.this);
                mBuilder.setMessage(nome+"\nPreço de Venda R$: "+venda+"\nQuantidade: "+estoque);

                mBuilder.setNegativeButton("DELETAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        alertaRemover = new AlertDialog.Builder(Produtos.this);

                        alertaRemover.setMessage("Tem certeza que deseja remover esse produto?");

                        alertaRemover.setNegativeButton("NÃO",
                                new Dialog.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //alertaRemover.setCancelable(true);
                                        Toast.makeText(Produtos.this, "Exclusão cancelada", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        alertaRemover.setPositiveButton("SIM",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        removerProduto(id);
                                    }
                                });

                        alertaRemover.create();
                        alertaRemover.show();
                    }
                });

                mBuilder.setPositiveButton("EDITAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Produtos.this, ProdutosEditar.class);

                                intent.putExtra("id", id);
                                intent.putExtra("nome", nome);
                                intent.putExtra("venda", venda);
                                intent.putExtra("preco", preco);
                                intent.putExtra("estoque", estoque);

                                startActivity(intent);
                            }
                        });

                mBuilder.create();
                mBuilder.show();

                /*
                mView = getLayoutInflater().inflate(R.layout.produtos_dialog, null);

                TextView mNomeProduto = (TextView) mView.findViewById(R.id.idNomeProdutoEditar);
                TextView mPrecoVenda = (TextView) mView.findViewById(R.id.idPrecoVendaEditar);
                TextView mEstoque = (TextView) mView.findViewById(R.id.idQtdEstoqueEditar);
                //ImageView mImagem = (ImageView) mView.findViewById(R.id.idImagemProdutoEditar);

                mNomeProduto.setText(nome);
                mPrecoVenda.setText("PREÇO R$: "+venda);
                mEstoque.setText("QUANTIDADE: "+estoque);
                //mImagem.setImageBitmap(mPhoto);

                Button mEditar = (Button) mView.findViewById(R.id.idBotaoProdutosDialogEditar);
                Button mApagar = (Button) mView.findViewById(R.id.idBotaoProdutosDialogApagar);
                Button mCompartilhar = (Button) mView.findViewById(R.id.idBotaoProdutosDialogCompartilhar);

                mEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Produtos.this, ProdutosEditar.class);

                        intent.putExtra("id", id);
                        intent.putExtra("nome", nome);
                        intent.putExtra("venda", venda);
                        intent.putExtra("preco", preco);
                        intent.putExtra("estoque", estoque);
                        //intent.putExtra("foto", foto);

                        startActivity(intent);
                    }
                });

                mApagar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertaRemover = new AlertDialog.Builder(Produtos.this);

                        alertaRemover.setMessage("Tem certeza que deseja remover esse produto?");

                        alertaRemover.setNegativeButton("NÃO",
                                new Dialog.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //alertaRemover.setCancelable(true);
                                        Toast.makeText(Produtos.this, "Exclusão cancelada", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        alertaRemover.setPositiveButton("SIM",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        removerProduto(id);
                                    }
                                });

                        alertaRemover.create();
                        alertaRemover.show();
                    }
                });

                mCompartilhar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                mBuilder.setView(mView);
                mBuilder.create();
                mBuilder.show();

                */
            }
        });
    }

    private void funcaoPesquisar(String pesquisarText) {
        if(pesquisarText.length() < 1){
            buscarDadosProdutos();
            criarListagem();
        }else{
            pesquisarDadosProdutos(pesquisarText);
            criarListagem();
            pesquisaEdt.setText("");
            Toast.makeText(Produtos.this, "Clique novamente em \"Pesquisar\" para mostrar todos os Produtos", Toast.LENGTH_SHORT).show();
        }
    }

    private void pesquisarDadosProdutos(String pesquisarText) {
        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * FROM "+ Banco.PRODUTO_TABELA+" WHERE "+Banco.PRODUTO_NOME+" LIKE '%"+pesquisarText+"%' ORDER BY "+Banco.PRODUTO_NOME+" ASC", null);

        }catch (Exception e){
            e.printStackTrace();
            Log.d("BuscaDadosProdutos: ", String.valueOf(e));
            Toast.makeText(getApplicationContext(), "Erro ao buscar dados dos produtos", Toast.LENGTH_LONG).show();
        }
    }

    private void removerProduto(String id) {

        try {
            SQLiteDatabase bd = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            bd.execSQL("DELETE FROM " + Banco.PRODUTO_TABELA + " WHERE " + Banco.PRODUTO_ID + " = " + id);

            buscarDadosProdutos();
            criarListagem();

            Toast.makeText(getApplicationContext(), "Produto Removido", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Produtos.this, "Erro ao deletar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        buscarDadosProdutos();
        criarListagem();
    }

    private void criarListagem() {
        listView = (ListView) findViewById(R.id.idListaProdutos);

        String[] from = {Banco.PRODUTO_NOME, Banco.PRODUTO_VENDA, Banco.PRODUTO_FOTO, Banco.PRODUTO_ESTOQUE};
        int[] to = {R.id.idNomeProduto, R.id.idPrecoVendaLista, R.id.idImagemProduto, R.id.idEstoqueListaProdutos};

        cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.produtos_lista, cursor, from, to);
        listView.setAdapter(cursorAdapter);

    }

    private void buscarDadosProdutos() {
        try{

            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * FROM "+ Banco.PRODUTO_TABELA+"  ORDER BY "+Banco.PRODUTO_NOME+" ASC", null);

        }catch (Exception e){
            e.printStackTrace();
            Log.i("BuscaDadosProduto: ", String.valueOf(e));
            Toast.makeText(getApplicationContext(), "Erro ao buscar dados dos Produtos", Toast.LENGTH_SHORT).show();
        }
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
            Toast.makeText(Produtos.this, "Erro ao criar tabela dos produtos", Toast.LENGTH_SHORT).show();
        }
    }
}