package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Build;
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

import java.util.Date;

public class VendasNovoDetalheDaVenda extends AppCompatActivity {

    private SQLiteDatabase bd;
    private Cursor cursor;
    private EditText preco;
    private EditText data;
    private EditText pago;
    private Button finalizar;
    private String idVenda;
    private Date dataAtual;
    private DateFormat dateFormat;
    private String dataSelecionada;
    private String idProduto;
    private String idCliente;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendas_novo_detalhe_da_venda);

        preco = (EditText) findViewById(R.id.idvndvpreco);
        data = (EditText) findViewById(R.id.idvndvdata);
        pago = (EditText) findViewById(R.id.idvndvpago);
        finalizar = (Button)findViewById(R.id.idvndvbtn);

        Intent it = getIntent();

        if(it != null){
            Bundle params = it.getExtras();

            if(params != null){
                idVenda = params.getString("idVenda");
                preco.setText(buscaPreco(idProduto(idVenda)), TextView.BufferType.EDITABLE);
            }
        }

        dataAtual = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dataSelecionada = dateFormat.format(dataAtual);

        data.setText(dataSelecionada, TextView.BufferType.EDITABLE);

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data != null && preco != null){

                    atualizarVenda(idVenda, preco.getText().toString(), data.getText().toString(), pago.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), Vendas.class);

                    intent.putExtra("idVenda", idVenda);
                    startActivity(intent);

                    atualizarProduto(idProduto);
                    bd.close();
                    finish();

                }else{
                    Toast.makeText(VendasNovoDetalheDaVenda.this, "Digite uma data de recebimento ou o preço a pagar.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        //Não voltarás!
    }

    private void atualizarProduto(String idProduto) {
        try{
            bd = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            Cursor cursorAux = bd.rawQuery("SELECT * FROM "+Banco.PRODUTO_TABELA+" WHERE "+Banco.PRODUTO_ID+" = "+idProduto,null);

            cursorAux.moveToFirst();
            String estoque = cursorAux.getString(cursorAux.getColumnIndex(Banco.PRODUTO_ESTOQUE));

            int i = Integer.parseInt(estoque);
            Toast.makeText(VendasNovoDetalheDaVenda.this, "Antes "+i, Toast.LENGTH_LONG).show();
            i = (i-1);
            estoque = Integer.toString(i);
            Toast.makeText(VendasNovoDetalheDaVenda.this, "Depois "+i, Toast.LENGTH_LONG).show();

            String sql = "UPDATE "+Banco.PRODUTO_TABELA+" SET "+Banco.PRODUTO_ESTOQUE+" = '"
                    +estoque+"' WHERE "+Banco.PRODUTO_ID+" = '"+idProduto+"'";

            bd.execSQL(sql);

        }catch (Exception e){
            Log.e("AtualizarProduto", String.valueOf(e));
        }
    }

    private void atualizarVenda(String id, String preco, String data, String pago) {
        try{
            bd = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            String sql = "UPDATE "+Banco.TABELA_VENDAS+" SET "+Banco.VENDAS_PRECO+" = '"+preco+"', "
                    +Banco.VENDAS_DATA+" = '"+data+"', "+Banco.VENDAS_PAGO+" = '"+pago+"' WHERE "
                    +Banco.VENDAS_ID+" = '"+id+"'";

            bd.execSQL(sql);

        }catch (Exception e){
            if(e.getMessage().toString() != null)
                Log.i("Erro Update VNDV", e.getMessage().toString());
        }
    }

    String idProduto(String idVenda){

        try{
            bd = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            cursor = bd.rawQuery("SELECT "+Banco.VENDAS_ID+", "+Banco.VENDAS_CLIENTE_ID+", "
                    +Banco.VENDAS_PRODUTO_ID+" FROM "+Banco.TABELA_VENDAS+" WHERE "+Banco.VENDAS_ID+" = '"+idVenda+"'",null);
            cursor.moveToFirst();

            idProduto = cursor.getString(cursor.getColumnIndex(Banco.VENDAS_PRODUTO_ID));
            idCliente = cursor.getString(cursor.getColumnIndex(Banco.VENDAS_CLIENTE_ID));

        }catch (Exception e){
            if(e.getMessage().toString() != null)
                Log.i("Erro Id Produto", e.getMessage().toString());
        }

        return idProduto;
    }

    String buscaPreco(String idVendaProduto){

        String precoPro = null;

        try{
            bd = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

            cursor = bd.rawQuery("SELECT "+Banco.PRODUTO_VENDA+" FROM "+Banco.PRODUTO_TABELA+" WHERE "+Banco.PRODUTO_ID+" = "+idVendaProduto,null);
            cursor.moveToFirst();

            precoPro = cursor.getString(cursor.getColumnIndex(Banco.PRODUTO_VENDA));

        }catch (Exception e){
            if(e.getMessage().toString() != null)
                Log.i("BuscaDadosVNDV", e.getMessage().toString());
        }

        return precoPro;
    }
}