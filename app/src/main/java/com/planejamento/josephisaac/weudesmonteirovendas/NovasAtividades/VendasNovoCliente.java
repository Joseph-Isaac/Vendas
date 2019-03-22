package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class VendasNovoCliente extends AppCompatActivity {

    private ListView listView;
    private CursorAdapter cursorAdapter;
    private Cursor cursor;
    private SQLiteDatabase db;
    private EditText editText;
    private Button btnPesquisar;
    private Button btnNovo;
    private Button continuar;
    private Intent i;

    String id;
    String nome;
    String endereco;
    String telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas_novo_cliente);

        editText = (EditText) findViewById(R.id.idVendasNovoClientePesquisarEdt);
        btnPesquisar = (Button) findViewById(R.id.idVendasNovoClientePesquisarBtn);
        btnNovo = (Button)findViewById(R.id.idVendasNovoClienteNovoBtn);
        continuar = (Button) findViewById(R.id.idVendasNovoClienteContinuar);

        buscarDadosCliente();
        criarListagem();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SQLiteCursor sqLiteCursor = (SQLiteCursor) cursorAdapter.getItem(i);
                id = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.CLIENTE_ID));
                nome = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.CLIENTE_NOME));
                endereco = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.CLIENTE_ENDERECO));
                telefone = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.CLIENTE_TELEFONE));

                Intent intent = new Intent(getApplicationContext(), VendasNovoProduto.class);

                intent.putExtra("idCliente", id);
                Toast.makeText(VendasNovoCliente.this, "idCliente "+id+" "+nome, Toast.LENGTH_LONG).show();

                if(id != null){
                    startActivity(intent);
                    db.close();
                    finish();
                }else {
                    Toast.makeText(VendasNovoCliente.this, "Selecione um Cliente", Toast.LENGTH_SHORT).show();
                }

            }
        });

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i = new Intent(getApplicationContext(), VendasNovoProduto.class);

                i.putExtra("id", id);
                Toast.makeText(VendasNovoCliente.this, "idCliente "+id+" "+nome, Toast.LENGTH_LONG).show();

                if(id != null){
                    startActivity(i);
                    db.close();
                    finish();
                }else {
                    Toast.makeText(VendasNovoCliente.this, "Selecione um Cliente", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(getApplicationContext(), ClientesNovo.class));
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        buscarDadosCliente();
        criarListagem();
    }

    private void funcaoPesquisar(String pesquisarText) {
        if(pesquisarText.length() < 1){
            buscarDadosCliente();
            criarListagem();
        }else{
            pesquisarDadosCliente(pesquisarText);
            criarListagem();
            editText.setText("");
            Toast.makeText(getApplicationContext(), "Clique novamente em Pesquisar para mostrar todos os Clientes", Toast.LENGTH_LONG).show();
        }
    }

    private void pesquisarDadosCliente(String pesquisarText) {
        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * FROM "+ Banco.CLIENTE_TABELA+" WHERE "+Banco.CLIENTE_NOME+" LIKE '%"+pesquisarText+"%' ORDER BY "+Banco.CLIENTE_NOME+" ASC", null);

        }catch (Exception e){
            e.printStackTrace();
            Log.d("BuscaDadosCliente: ", String.valueOf(e));
            Toast.makeText(getApplicationContext(), "Erro ao buscar dados dos clientes", Toast.LENGTH_SHORT).show();
        }
    }

    private void criarListagem() {
        listView = (ListView) findViewById(R.id.idVendasNovoClienteLV);

        String[] from = {Banco.CLIENTE_NOME, Banco.CLIENTE_ENDERECO, Banco.CLIENTE_TELEFONE};
        int[] to = {R.id.idNomeCliente, R.id.idEnderecoCliente, R.id.idTelefoneCliente};

        cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.clientes_lista, cursor, from, to);
        listView.setAdapter(cursorAdapter);
    }

    private void buscarDadosCliente() {
        db = null;
        try{

            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * FROM "+ Banco.CLIENTE_TABELA+" ORDER BY "+Banco.CLIENTE_NOME+" ASC", null);

        }catch (Exception e) {
            e.printStackTrace();
            Log.d("BuscaDadosCliente: ", String.valueOf(e));
            Toast.makeText(getApplicationContext(), "Erro ao buscar dados dos clientes", Toast.LENGTH_SHORT).show();
        }
    }
}
