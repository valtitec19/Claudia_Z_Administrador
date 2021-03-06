package com.clauzon.proyectoclauz.Clases;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.proyectoclauz.R;

public class HolderInventory extends RecyclerView.ViewHolder {


    private TextView nombre_item, precio_compra, precio_venta, categoria,descripcion,estado,cantidad_item,estado2,oferta;
    private ImageView foto_item;

    public HolderInventory(@NonNull View itemView) {
        super(itemView);
        nombre_item = (TextView) itemView.findViewById(R.id.nombre_item);
        nombre_item.setSelected(true);
        precio_compra= (TextView) itemView.findViewById(R.id.precio_compra);
        precio_venta = (TextView) itemView.findViewById(R.id.precio_venta);
        categoria=(TextView)itemView.findViewById(R.id.categoria_item);
        descripcion=(TextView)itemView.findViewById(R.id.descripcion_item);
        descripcion.setSelected(true);
        foto_item= (ImageView) itemView.findViewById(R.id.foto_item);
        estado=(TextView)itemView.findViewById(R.id.estado_del_item_);
        cantidad_item=(TextView)itemView.findViewById(R.id.cantidad_de_items);
        estado2=(TextView)itemView.findViewById(R.id.estado_del_item_2);
        oferta=(TextView)itemView.findViewById(R.id.precio_oferta);

    }

    public TextView getNombre_item() {
        return nombre_item;
    }

    public void setNombre_item(TextView nombre_item) {
        this.nombre_item = nombre_item;
    }

    public TextView getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(TextView precio_compra) {
        this.precio_compra = precio_compra;
    }

    public TextView getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(TextView precio_venta) {
        this.precio_venta = precio_venta;
    }

    public TextView getCategoria() {
        return categoria;
    }

    public void setCategoria(TextView categoria) {
        this.categoria = categoria;
    }

    public TextView getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(TextView descripcion) {
        this.descripcion = descripcion;
    }

    public TextView getEstado() {
        return estado;
    }

    public void setEstado(TextView estado) {
        this.estado = estado;
    }

    public TextView getCantidad_item() {
        return cantidad_item;
    }

    public void setCantidad_item(TextView cantidad_item) {
        this.cantidad_item = cantidad_item;
    }

    public TextView getEstado2() {
        return estado2;
    }

    public void setEstado2(TextView estado2) {
        this.estado2 = estado2;
    }

    public ImageView getFoto_item() {
        return foto_item;
    }

    public void setFoto_item(ImageView foto_item) {
        this.foto_item = foto_item;
    }

    public TextView getOferta() {
        return oferta;
    }

    public void setOferta(TextView oferta) {
        this.oferta = oferta;
    }
}
