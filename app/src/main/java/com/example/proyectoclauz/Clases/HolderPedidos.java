package com.example.proyectoclauz.Clases;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoclauz.R;

public class HolderPedidos extends RecyclerView.ViewHolder {
    private TextView productos_pedido,lugar,fecha,estado, cantidad,cliente,hora,entrega;
    private ImageView foto;
    private CheckBox checkBox;
    public HolderPedidos(@NonNull View itemView) {
        super(itemView);
        productos_pedido=(TextView)itemView.findViewById(R.id.productos_recycler_pedido);
        lugar=(TextView)itemView.findViewById(R.id.lugar_recycler_pedido);
        fecha=(TextView)itemView.findViewById(R.id.fecha_recycler_pedido);
        estado=(TextView)itemView.findViewById(R.id.estado_recycler_pedido);
        cantidad =(TextView)itemView.findViewById(R.id.cantidad_recycler_pedidos);
        foto=(ImageView)itemView.findViewById(R.id.foto_recycler_pedido);
        hora=(TextView)itemView.findViewById(R.id.hora_recycler_pedido);
        checkBox=(CheckBox)itemView.findViewById(R.id.checkbox_recycler_pedidos);
        entrega =(TextView)itemView.findViewById(R.id.tipo_entrega);
    }

    public TextView getProductos_pedido() {
        return productos_pedido;
    }

    public void setProductos_pedido(TextView productos_pedido) {
        this.productos_pedido = productos_pedido;
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


    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public TextView getCantidad() {
        return cantidad;
    }

    public void setCantidad(TextView cantidad) {
        this.cantidad = cantidad;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public TextView getEntrega() {
        return entrega;
    }

    public void setEntrega(TextView entrega) {
        this.entrega = entrega;
    }

    public TextView getCliente() {
        return cliente;
    }

    public void setCliente(TextView cliente) {
        this.cliente = cliente;
    }
}
