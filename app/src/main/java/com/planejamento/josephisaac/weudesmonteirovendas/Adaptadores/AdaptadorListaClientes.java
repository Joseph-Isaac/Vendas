package com.planejamento.josephisaac.weudesmonteirovendas.Adaptadores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.planejamento.josephisaac.weudesmonteirovendas.R;

import java.util.List;


public class AdaptadorListaClientes extends ArrayAdapter<ElementosListaClientes>{

    private final Context context;
    private final List<ElementosListaClientes> elementos;

    public AdaptadorListaClientes(Context context, List<ElementosListaClientes> elementos) {
        super(context, R.layout.clientes_lista, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.clientes_lista, parent, false);

        TextView nome = (TextView) rowView.findViewById(R.id.idNomeCliente);
        TextView endereco = (TextView) rowView.findViewById(R.id.idEnderecoCliente);
        TextView telefone = (TextView) rowView.findViewById(R.id.idTelefoneCliente);

        nome.setText(elementos.get(position).getNome());
        endereco.setText(elementos.get(position).getEndereco());
        telefone.setText((int) elementos.get(position).getTelefone());

        return rowView;
    }
}