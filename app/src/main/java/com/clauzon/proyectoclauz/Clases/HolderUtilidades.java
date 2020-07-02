package com.clauzon.proyectoclauz.Clases;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.proyectoclauz.R;

public class HolderUtilidades extends RecyclerView.ViewHolder {
    private TextView producto,compra,venta,cantidad,ganancia,cuenta_repartidor,repartidor,fecha,hora,lugar,cliente,telefono,estatus;
    private LinearLayout linearLayout;

    public HolderUtilidades(@NonNull View itemView) {
        super(itemView);
        producto=(TextView)itemView.findViewById(R.id.producto_recycler_utlidades);
        compra=(TextView)itemView.findViewById(R.id.compra_recycler_utlidades);
        venta=(TextView)itemView.findViewById(R.id.venta_recycler_utlidades);
        cantidad=(TextView)itemView.findViewById(R.id.vendidos_recycler_utlidades);
        ganancia=(TextView)itemView.findViewById(R.id.ganancia_recycler_utlidades);
        cuenta_repartidor=(TextView)itemView.findViewById(R.id.cuenta_repartidor);
        repartidor=(TextView)itemView.findViewById(R.id.repartidor_recycler_utilidades);
        fecha=(TextView)itemView.findViewById(R.id.fecha_recycler_utilidades);
        hora=(TextView)itemView.findViewById(R.id.hora_recycler_utilidades);
        lugar=(TextView)itemView.findViewById(R.id.lugar_recycler_utiidades);
        cliente=(TextView)itemView.findViewById(R.id.cliente_recycler_utilidades);
        telefono=(TextView)itemView.findViewById(R.id.telefono_recycler_utilidades);
        estatus=(TextView)itemView.findViewById(R.id.estatus_recycler_utilidades);
        linearLayout=(LinearLayout)itemView.findViewById(R.id.linear_utilidades);
    }

    public TextView getProducto() {
        return producto;
    }

    public void setProducto(TextView producto) {
        this.producto = producto;
    }

    public TextView getCompra() {
        return compra;
    }

    public void setCompra(TextView compra) {
        this.compra = compra;
    }

    public TextView getVenta() {
        return venta;
    }

    public void setVenta(TextView venta) {
        this.venta = venta;
    }

    public TextView getCantidad() {
        return cantidad;
    }

    public void setCantidad(TextView cantidad) {
        this.cantidad = cantidad;
    }

    public TextView getGanancia() {
        return ganancia;
    }

    public void setGanancia(TextView ganancia) {
        this.ganancia = ganancia;
    }

    public TextView getCuenta_repartidor() {
        return cuenta_repartidor;
    }

    public void setCuenta_repartidor(TextView cuenta_repartidor) {
        this.cuenta_repartidor = cuenta_repartidor;
    }

    public TextView getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(TextView repartidor) {
        this.repartidor = repartidor;
    }

    public TextView getFecha() {
        return fecha;
    }

    public void setFecha(TextView fecha) {
        this.fecha = fecha;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public TextView getLugar() {
        return lugar;
    }

    public void setLugar(TextView lugar) {
        this.lugar = lugar;
    }

    public TextView getCliente() {
        return cliente;
    }

    public void setCliente(TextView cliente) {
        this.cliente = cliente;
    }

    public TextView getTelefono() {
        return telefono;
    }

    public void setTelefono(TextView telefono) {
        this.telefono = telefono;
    }

    public TextView getEstatus() {
        return estatus;
    }

    public void setEstatus(TextView estatus) {
        this.estatus = estatus;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }
}
