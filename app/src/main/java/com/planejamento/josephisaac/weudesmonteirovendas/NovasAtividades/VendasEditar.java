package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.planejamento.josephisaac.weudesmonteirovendas.BancoDados.Banco;
import com.planejamento.josephisaac.weudesmonteirovendas.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VendasEditar extends AppCompatActivity {

    private EditText pago;
    private EditText data;
    private TextView sCliente;
    private TextView sProduto;
    private TextView sValor;
    private TextView sData;
    private Date dataAtual;
    private DateFormat dateFormat;
    private String dataSelecionada;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas_editar);

        pago = (EditText) findViewById(R.id.valorJaPagoEditar);
        data = (EditText) findViewById(R.id.dataPagamentoAtual);
        sCliente = (TextView) findViewById(R.id.textView44);
        sProduto = (TextView) findViewById(R.id.textView45);
        sValor = (TextView) findViewById(R.id.valorProdutoEditar);
        sData = (TextView) findViewById(R.id.textView40);
        btn = (Button) findViewById(R.id.button2);

        Bundle extra = getIntent().getExtras();

        final String id = extra.getString("id");
        final String idCliente = extra.getString("idCliente");
        final String idProduto = extra.getString("idProduto");
        final String date = extra.getString("data");
        final String jaPago = extra.getString("pago");
        final String preco = extra.getString("preco");

        sCliente.setText(nomeCliente(idCliente));
        sProduto.setText(nomeProduto(idProduto));
        sValor.setText(preco);
        sData.setText(date);
        pago.setText(jaPago);

        dataAtual = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dataSelecionada = dateFormat.format(dataAtual);

        data.setText(dataSelecionada, TextView.BufferType.EDITABLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sPago = pago.getText().toString();
                String dataS = data.getText().toString();


                if((sPago != null || dataS != null) ){
                    editarVenda(id, sPago, dataS);
                } else {
                    Toast.makeText(VendasEditar.this, "Os campos não podem ficar em branco", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void editarVenda(String id, String sPago, String dataS) {

        SQLiteDatabase db;

        try{

            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            db.execSQL("UPDATE "+Banco.TABELA_VENDAS+" SET "+Banco.VENDAS_PAGO+" = '"+sPago+"' WHERE "+Banco.VENDAS_ID+" = '"+id+"'");
            db.execSQL("UPDATE "+Banco.TABELA_VENDAS+" SET "+Banco.VENDAS_DATA+" = '"+dataS+"' WHERE "+Banco.VENDAS_ID+" = '"+id+"'");

            Toast.makeText(VendasEditar.this, "Dados editados com sucesso", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Log.e("ErroEditar",String.valueOf(e));
            Toast.makeText(VendasEditar.this, "Erro ao Editar dados", Toast.LENGTH_SHORT).show();
        }finally {
            finish();
        }
    }

    private String nomeProduto(String id) {
        Cursor c;
        SQLiteDatabase db;
        String nome = null;

        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            c = db.rawQuery("SELECT * FROM "+Banco.PRODUTO_TABELA+" WHERE "+Banco.PRODUTO_ID+"="+id,null);
            c.moveToFirst();

            nome = c.getString(c.getColumnIndex(Banco.PRODUTO_NOME));

        }catch (Exception e){
            Log.e("ErroNomeProduto", String.valueOf(e));
        }

        return nome;
    }

    private String nomeCliente(String id) {
        Cursor c;
        SQLiteDatabase db;
        String nome = null;

        try{
            db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);
            c = db.rawQuery("SELECT * FROM "+Banco.CLIENTE_TABELA+" WHERE "+Banco.CLIENTE_ID+"="+id,null);
            c.moveToFirst();

            nome = c.getString(c.getColumnIndex(Banco.CLIENTE_NOME));

        }catch (Exception e){
            Log.e("ErroNomeCliente", String.valueOf(e));
        }

        return nome;
    }
}
