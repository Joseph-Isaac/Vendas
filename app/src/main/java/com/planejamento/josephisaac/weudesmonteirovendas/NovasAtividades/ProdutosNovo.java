package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.planejamento.josephisaac.weudesmonteirovendas.BancoDados.Banco;
import com.planejamento.josephisaac.weudesmonteirovendas.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ProdutosNovo extends AppCompatActivity {

    private Button button;
    private ImageView imageView;
    private EditText nome;
    private EditText custo;
    private EditText venda;
    private EditText estoque;
    //private String imagePath;

    //AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produtos_novo);

        button = (Button) findViewById(R.id.idBotaoProdutosAdicionar);

        nome = (EditText) findViewById(R.id.idProdutoNome);
        custo = (EditText) findViewById(R.id.idProdutoPrecoCusto);
        venda = (EditText) findViewById(R.id.idProdutoPrecoVenda);
        estoque = (EditText) findViewById(R.id.idQtdEstoque);

        //capturarImagem();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeString = nome.getText().toString();
                String custoString = custo.getText().toString();
                String vendaString = venda.getText().toString();
                //String fotoString = imagePath;
                String estoqueString = estoque.getText().toString();

                salvarProduto(nomeString, custoString, vendaString, estoqueString);
            }
        });
    }



    private void salvarProduto(String nomeString, String custoString, String vendaString, String estoqueString) {
        SQLiteDatabase db = null;
        try {
            if (nomeString.length() < 1 || custoString.length() < 1) {
                Toast.makeText(ProdutosNovo.this, "Não pode existir um produto sem nome ou preço de custo e sem estoque", Toast.LENGTH_SHORT).show();
            } else {

                db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

                if (vendaString.length() < 1) {
                    float custo = Float.parseFloat(custoString);
                    float venda = 2 * custo;

                    vendaString = String.valueOf(venda);
                }

                db.execSQL("INSERT INTO " + Banco.PRODUTO_TABELA + "( " + Banco.PRODUTO_NOME
                        + ", " + Banco.PRODUTO_CUSTO + ", " + Banco.PRODUTO_VENDA
                        + ", " +Banco.PRODUTO_ESTOQUE+" ) VALUES( '" + nomeString + "', '"
                        + custoString + "', '" + vendaString + "'" +", '"+estoqueString+"' )");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ProdutosNovo.this, "Erro ao cadastrar Produto", Toast.LENGTH_SHORT).show();

        } finally {
            db.close();
            finish();
        }
    }

    /*private void capturarImagem() {
        //imageView = (ImageView) findViewById(R.id.idAdicionarImagem);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(ProdutosNovo.this);

                alertDialog.setPositiveButton("CÂMERA",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 2);
                            }
                        });
                alertDialog.setNegativeButton("GALERIA",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 1);
                            }
                        });

                alertDialog.create();
                alertDialog.show();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri localImagemSelecionada = data.getData();
            Bitmap photo = null;

            try{
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
            }catch (IOException e){
                e.printStackTrace();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 75, stream);
            imagePath = localImagemSelecionada.toString();
            imageView.setImageBitmap(photo);

        }else if(requestCode == 2 && resultCode == RESULT_OK && data != null){

            Uri localImagemSelecionada = data.getData();
            Bitmap imagem = null;

            try {
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.PNG, 75, stream);
            imagePath = localImagemSelecionada.toString();
            imageView.setImageBitmap(imagem);
        }
    }*/
}
