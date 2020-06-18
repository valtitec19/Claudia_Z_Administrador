package com.example.proyectoclauz.Clases;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoclauz.R;

import org.w3c.dom.Text;

public class HolderEnvio extends RecyclerView.ViewHolder{
    private TextView producto,lugar,fecha,estado;
    private ImageView foto;
    public HolderEnvio(@NonNull View itemView) {
        super(itemView);
        producto=(TextView)itemView.findViewById(R.id.productos_recycler_envio);
        lugar=(TextView)itemView.findViewById(R.id.lugar_recycler_envio);
        fecha=(TextView)itemView.findViewById(R.id.fecha_recycler_envio);
        estado=(TextView)itemView.findViewById(R.id.estado_recycler_endio);
        foto=(ImageView)itemView.findViewById(R.id.foto_recycler_envio);
    }

    public TextView getProducto() {
        return producto;
    }

    public void setProducto(TextView producto) {
        this.producto = producto;
    }

    public TextView getLugar() {
        return lugar;
    }

    public void setLugar(TextView lugar) {
        this.lugar = lugar;
    }

    public TextView getFecha() {
        return fecha;
    }

    public void setFecha(TextView fecha) {
        this.fecha = fecha;
    }

    public TextView getEstado() {
        return estado;
    }

    public void setEstado(TextView estado) {
        this.estado = estado;
    }

    public ImageView getFoto() {
        return foto;
    }

    public void setFoto(ImageView foto) {
        this.foto = foto;
    }
}
