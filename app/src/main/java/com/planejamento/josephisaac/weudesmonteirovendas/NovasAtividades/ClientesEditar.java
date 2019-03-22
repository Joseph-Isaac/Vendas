package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.planejamento.josephisaac.weudesmonteirovendas.Adaptadores.ElementosListaClientes;
import com.planejamento.josephisaac.weudesmonteirovendas.BancoDados.Banco;
import com.planejamento.josephisaac.weudesmonteirovendas.R;

public class ClientesEditar extends AppCompatActivity {

    private EditText nome;
    private EditText endereco;
    private EditText telefone;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientes_editar);

        button = (Button) findViewById(R.id.idBotaoClientesEditar);

        nome = (EditText) findViewById(R.id.idEditTextNomeEditar);
        endereco = (EditText) findViewById(R.id.idEditTextEnderecoEditar);
        telefone = (EditText) findViewById(R.id.idEditTextTelefoneEditar);

        Bundle extra = getIntent().getExtras();
        final String id = extra.getString("id");
        final String nomePassado = extra.getString("nome");
        final String enderecoPassado = extra.getString("endereco");
        final String telefonePassado = extra.getString("telefone");

        recuperaDados(nomePassado, enderecoPassado, telefonePassado);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    String nomeString = nome.getText().toString();
                    String enderecoString = endereco.getText().toString();
                    String telefoneString = telefone.getText().toString();

                    editarCliente(nomeString, enderecoString, telefoneString, id);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro ao editar cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void recuperaDados(String nome, String endereco, String telefone) {

        this.nome.setText(nome);
        this.endereco.setText(endereco);
        this.telefone.setText(telefone);

    }

    private void editarCliente(String nome, String endereco, String telefone, String id) {
        SQLiteDatabase db = null;
        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            db.execSQL("UPDATE "+Banco.CLIENTE_TABELA+" SET "+Banco.CLIENTE_NOME+" = '"+nome+"' WHERE "+Banco.CLIENTE_ID+" = '"+id+"'");
            db.execSQL("UPDATE "+Banco.CLIENTE_TABELA+" SET "+Banco.CLIENTE_ENDERECO+" = '"+endereco+"' WHERE "+Banco.CLIENTE_ID+" = '"+id+"'");
            db.execSQL("UPDATE "+Banco.CLIENTE_TABELA+" SET "+Banco.CLIENTE_TELEFONE+" = '"+telefone+"' WHERE "+Banco.CLIENTE_ID+" = '"+id+"'");

            Toast.makeText(ClientesEditar.this, "Dados editados com sucesso", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erro ao editar os dados do Cliente", Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
            finish();
        }
    }
}
