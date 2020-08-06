package com.clauzon.proyectoclauz.Clases;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.proyectoclauz.R;

public class HolderCategorias extends RecyclerView.ViewHolder {
    private TextView categoria;
    private ImageView borrar;

    public HolderCategorias(@NonNull View itemView) {
        super(itemView);
        categoria=(TextView)itemView.findViewById(R.id.txt_categoria_recycler);
        borrar=(ImageView) itemView.findViewById(R.id.imagen_categoria_recycler);
    }


    public TextView getCategoria() {
        return categoria;
    }

    public void setCategoria(TextView categoria) {
        this.categoria = categoria;
    }

    public ImageView getBorrar() {
        return borrar;
    }

    public void setBorrar(ImageView borrar) {
        this.borrar = borrar;
    }
}
