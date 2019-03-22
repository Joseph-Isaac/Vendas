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

public class Clientes extends Activity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter cursorAdapter;
    private ListView listView;
    private Button button;
    private AlertDialog.Builder alerta;
    private AlertDialog.Builder alertaRemover;
    private EditText editText;
    private Button pesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientes);

        //CriarTabelaClientes();


        button = (Button) findViewById(R.id.idBotaoClientes);
        editText = (EditText)findViewById(R.id.idClientesPesquisaText);
        pesquisar = (Button) findViewById(R.id.idClientesPesquisaBotton);

        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pesquisarText = editText.getText().toString();

                funcaoPesquisar(pesquisarText);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Clientes.this, ClientesNovo.class));
            }
        });

        buscarDadosCliente();
        criarListagem();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SQLiteCursor sqLiteCursor = (SQLiteCursor) cursorAdapter.getItem(i);
                final String id = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.CLIENTE_ID));
                final String nome = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.CLIENTE_NOME));
                final String endereco = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.CLIENTE_ENDERECO));
                final String telefone = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(Banco.CLIENTE_TELEFONE));

                alerta = new AlertDialog.Builder(Clientes.this);

                alerta.setMessage("Nome: "+nome+"\nEndereço: "+endereco+"\nTelefone: "+telefone);


                alerta.setNegativeButton("DELETAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                alertaRemover = new AlertDialog.Builder(Clientes.this);

                                alertaRemover.setMessage("Tem certeza que deseja remover esse cliente?");

                                alertaRemover.setNegativeButton("NÃO",
                                        new Dialog.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //alertaRemover.setCancelable(true);
                                                Toast.makeText(Clientes.this, "Exclusão cancelada", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                alertaRemover.setPositiveButton("SIM",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                removerCliente(id);
                                            }
                                        });

                                alertaRemover.create();
                                alertaRemover.show();
                            }
                        });

                alerta.setPositiveButton("EDITAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Clientes.this, ClientesEditar.class);

                                intent.putExtra("id", id);
                                intent.putExtra("nome", nome);
                                intent.putExtra("endereco", endereco);
                                intent.putExtra("telefone", telefone);

                                startActivity(intent);
                            }
                        });

                alerta.create();
                alerta.show();

            }
        });
    }

    private void funcaoPesquisar(String pesquisarText) {
        if(pesquisarText.length() < 1){
            buscarDadosCliente();
            criarListagem();
        }else{
            pesquisarDadosCliente(pesquisarText);
            criarListagem();
            editText.setText("");
            Toast.makeText(Clientes.this, "Clique novamente em Pesquisar para mostrar todos os Clientes", Toast.LENGTH_SHORT).show();
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

    private void removerCliente(String id) {

        try {
            SQLiteDatabase bd = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            bd.execSQL("DELETE FROM " + Banco.CLIENTE_TABELA + " WHERE " + Banco.CLIENTE_ID + " = " + id);

            buscarDadosCliente();
            criarListagem();

            Toast.makeText(getApplicationContext(), "Cliente Removido", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Clientes.this, "Erro ao deletar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        buscarDadosCliente();
        criarListagem();
    }

    private void criarListagem() {
        listView = (ListView) findViewById(R.id.idListaClientes);

        String[] from = {Banco.CLIENTE_NOME, Banco.CLIENTE_ENDERECO, Banco.CLIENTE_TELEFONE};
        int[] to = {R.id.idNomeCliente, R.id.idEnderecoCliente, R.id.idTelefoneCliente};

        cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.clientes_lista, cursor, from, to);
        listView.setAdapter(cursorAdapter);

    }

    private void buscarDadosCliente() {
        try{

            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            cursor = db.rawQuery("SELECT * FROM "+ Banco.CLIENTE_TABELA+" ORDER BY "+Banco.CLIENTE_NOME+" ASC", null);

        }catch (Exception e){
            e.printStackTrace();
            Log.d("BuscaDadosCliente: ", String.valueOf(e));
            Toast.makeText(getApplicationContext(), "Erro ao buscar dados dos clientes", Toast.LENGTH_SHORT).show();
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
        }
    }
}
