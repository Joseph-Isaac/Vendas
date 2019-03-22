package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.planejamento.josephisaac.weudesmonteirovendas.BancoDados.Banco;
import com.planejamento.josephisaac.weudesmonteirovendas.R;

public class ClientesNovo extends AppCompatActivity {

    private EditText nome;
    private EditText endereco;
    private EditText telefone;

    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientes_novo);

        button = (Button) findViewById(R.id.idBotaoClientesAdicionar);

        nome = (EditText) findViewById(R.id.idEditTextNome);
        endereco = (EditText) findViewById(R.id.idEditTextEndereco);
        telefone = (EditText) findViewById(R.id.idEditTextTelefone);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    String nomeString = nome.getText().toString();
                    String enderecoString = endereco.getText().toString();
                    String telefoneString = telefone.getText().toString();

                    salvarCliente(nomeString, enderecoString, telefoneString);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro ao inserir cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void salvarCliente(String nomeString, String enderecoString, String telefoneString) {
        SQLiteDatabase db = null;
        try{

            if(nomeString.length() < 1 || enderecoString.length() < 1){
                Toast.makeText(ClientesNovo.this, "Não pode existir um cliente sem nome ou endereço", Toast.LENGTH_SHORT).show();
            }else {

                db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
                db.execSQL("INSERT INTO " + Banco.CLIENTE_TABELA + "( " + Banco.CLIENTE_NOME
                        + ", " + Banco.CLIENTE_ENDERECO + ", " + Banco.CLIENTE_TELEFONE + ") VALUES( '"
                        + nomeString + "', '" + enderecoString + "', '" + telefoneString + "')");
                finish();

            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erro ao salvar Cliente", Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
            finish();
        }
    }


}
