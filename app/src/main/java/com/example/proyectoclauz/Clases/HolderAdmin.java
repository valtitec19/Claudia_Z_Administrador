package com.example.proyectoclauz.Clases;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoclauz.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HolderAdmin extends RecyclerView.ViewHolder {
    private CircleImageView foto;
    private TextView repartidor,telefoto, correo_repartidor;
    private ImageView estado;

    public HolderAdmin(@NonNull View itemView) {
        super(itemView);
        repartidor=(TextView)itemView.findViewById(R.id.nombre_repatidor);
        telefoto=(TextView)itemView.findViewById(R.id.telefono_repartidor);
        correo_repartidor =(TextView)itemView.findViewById(R.id.correo_repartidor);
        foto=(CircleImageView)itemView.findViewById(R.id.foto_repartidor);
        estado=(ImageView)itemView.findViewById(R.id.estado_repartidor);
    }

    public CircleImageView getFoto() {
        return foto;
    }

    public void setFoto(CircleImageView foto) {
        this.foto = foto;
    }

    public TextView getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(TextView repartidor) {
        this.repartidor = repartidor;
    }

    public TextView getTelefoto() {
        return telefoto;
    }

    public void setTelefoto(TextView telefoto) {
        this.telefoto = telefoto;
    }

    public TextView getCorreo_repartidor() {
        return correo_repartidor;
    }

    public void setCorreo_repartidor(TextView correo_repartidor) {
        this.correo_repartidor = correo_repartidor;
    }

    public ImageView getEstado() {
        return estado;
    }

    public void setEstado(ImageView estado) {
        this.estado = estado;
    }
}
