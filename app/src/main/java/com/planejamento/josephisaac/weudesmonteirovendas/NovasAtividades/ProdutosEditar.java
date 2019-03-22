package com.planejamento.josephisaac.weudesmonteirovendas.NovasAtividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
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
import java.io.IOException;

public class ProdutosEditar extends AppCompatActivity {

    private Button button;
    //private ImageView imageView;
    private EditText nome;
    private EditText custo;
    private EditText venda;
    private EditText estoque;
    private String imagePath;
    //private ImageView imagem;
    //private ImageView imageView;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produtos_editar);

        button = (Button) findViewById(R.id.idBotaoProdutosEditar);

        nome = (EditText) findViewById(R.id.idProdutoEditarNome);
        custo = (EditText) findViewById(R.id.idProdutoEditarPrecoCusto);
        venda = (EditText) findViewById(R.id.idProdutoEditarPrecoVenda);
        estoque = (EditText) findViewById(R.id.idProdutoEstoqueEditar);
//        imagem = (ImageView) findViewById(R.id.idEditarImagemProduto);

        Bundle extra = getIntent().getExtras();
        final String id = extra.getString("id");
        final String nomePassado = extra.getString("nome");
        final String custoPassado = extra.getString("preco");
        final String vendaPassada = extra.getString("venda");
        final String estoquePassado = extra.getString("estoque");
        //final String fotoPassada = extra.getString("foto");

        recuperaDados(nomePassado, custoPassado, vendaPassada, estoquePassado);

        //capturarImagem();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeString = nome.getText().toString();
                String custoString = custo.getText().toString();
                String vendaString = venda.getText().toString();
                String fotoString = imagePath;
                String estoqueString = estoque.getText().toString();

                salvarProduto(nomeString, custoString, vendaString, fotoString, estoqueString, id);
            }
        });
    }

    private void salvarProduto(String nomeString, String custoString, String vendaString, String fotoString, String estoqueString, String id) {
        SQLiteDatabase db = null;
        try {
            if (nomeString.length() < 1 || custoString.length() < 1) {
                Toast.makeText(ProdutosEditar.this, "Não pode existir um produto sem nome ou preço de custo e sem estoque", Toast.LENGTH_SHORT).show();
            } else {

                db = openOrCreateDatabase(Banco.NOME_BANCO, MODE_PRIVATE, null);

                if (vendaString.length() < 1) {
                    float custo = Float.parseFloat(custoString);
                    float venda = 2 * custo;

                    vendaString = String.valueOf(venda);
                }

                db.execSQL("UPDATE "+Banco.PRODUTO_TABELA+" SET "+Banco.PRODUTO_NOME+" = '"+nomeString+"' WHERE "+Banco.PRODUTO_ID+" = "+id);
                db.execSQL("UPDATE "+Banco.PRODUTO_TABELA+" SET "+Banco.PRODUTO_CUSTO+" = '"+custoString+"' WHERE "+Banco.PRODUTO_ID+" = "+id);
                db.execSQL("UPDATE "+Banco.PRODUTO_TABELA+" SET "+Banco.PRODUTO_VENDA+" = '"+vendaString+"' WHERE "+Banco.PRODUTO_ID+" = "+id);
                db.execSQL("UPDATE "+Banco.PRODUTO_TABELA+" SET "+Banco.PRODUTO_FOTO+" = '"+fotoString+"' WHERE "+Banco.PRODUTO_ID+" = "+id);
                db.execSQL("UPDATE "+Banco.PRODUTO_TABELA+" SET "+Banco.PRODUTO_ESTOQUE+" = '"+estoqueString+"' WHERE "+Banco.PRODUTO_ID+" = "+id);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ProdutosEditar.this, "Erro ao cadastrar Produto", Toast.LENGTH_SHORT).show();

        } finally {
            db.close();
            finish();
        }

    }

    private void recuperaDados(String nomePassado, String custoPassado, String vendaPassada, String estoquePassado) {

        this.nome.setText(nomePassado);
        this.custo.setText(custoPassado);
        this.venda.setText(vendaPassada);
        this.estoque.setText(estoquePassado);

        /*Bitmap bitmap = null;

        try{
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(fotoPassada));
        }catch (Exception e){
            e.printStackTrace();
        }

        //imagem.setImageBitmap(bitmap);*/
    }

    /*private void capturarImagem() {
        //imageView = (ImageView) findViewById(R.id.idEditarImagemProduto);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(ProdutosEditar.this);

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
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
            imagem.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagePath = localImagemSelecionada.toString();
            imageView.setImageBitmap(imagem);
        }
    }*/
}
