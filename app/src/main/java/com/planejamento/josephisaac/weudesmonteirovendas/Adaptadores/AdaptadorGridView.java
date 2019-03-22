package com.planejamento.josephisaac.weudesmonteirovendas.Adaptadores;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AdaptadorGridView extends BaseAdapter {
    private Context context;
    private int[] lista;

    public AdaptadorGridView(Context context, int[] lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.length;
    }

    @Override
    public Object getItem(int i) {
        return lista[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(lista[i]);
        imageView.setAdjustViewBounds(true);

        return imageView;
    }
}