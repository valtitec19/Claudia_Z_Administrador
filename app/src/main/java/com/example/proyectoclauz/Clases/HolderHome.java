package com.example.proyectoclauz.Clases;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoclauz.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderHome extends RecyclerView.ViewHolder {
    private CircleImageView foto;
    private TextView nombre_recycler_home, lugar_recycler_home, estado_recycler_home,cantidad_recycler_home,cliente_recycler_home;

    public HolderHome(@NonNull View itemView) {
        super(itemView);
        foto=(CircleImageView)itemView.findViewById(R.id.foto_item_home);
        nombre_recycler_home=(TextView)itemView.findViewById(R.id.nombre_recycler_home);
        lugar_recycler_home =(TextView)itemView.findViewById(R.id.lugar_envio_home);
        estado_recycler_home =(TextView)itemView.findViewById(R.id.estado_recycler_home);
        cantidad_recycler_home=(TextView)itemView.findViewById(R.id.cantidad_de_items_home);
        cliente_recycler_home=(TextView)itemView.findViewById(R.id.cliente);
    }

    public CircleImageView getFoto() {
        return foto;
    }

    public void setFoto(CircleImageView foto) {
        this.foto = foto;
    }

    public TextView getNombre_recycler_home() {
        return nombre_recycler_home;
    }

    public void setNombre_recycler_home(TextView nombre_recycler_home) {
        this.nombre_recycler_home = nombre_recycler_home;
    }

    public TextView getLugar_recycler_home() {
        return lugar_recycler_home;
    }

    public void setLugar_recycler_home(TextView lugar_recycler_home) {
        this.lugar_recycler_home = lugar_recycler_home;
    }

    public TextView getEstado_recycler_home() {
        return estado_recycler_home;
    }

    public void setEstado_recycler_home(TextView estado_recycler_home) {
        this.estado_recycler_home = estado_recycler_home;
    }

    public TextView getCantidad_recycler_home() {
        return cantidad_recycler_home;
    }

    public void setCantidad_recycler_home(TextView cantidad_recycler_home) {
        this.cantidad_recycler_home = cantidad_recycler_home;
    }

    public TextView getCliente_recycler_home() {
        return cliente_recycler_home;
    }

    public void setCliente_recycler_home(TextView cliente_recycler_home) {
        this.cliente_recycler_home = cliente_recycler_home;
    }
}
