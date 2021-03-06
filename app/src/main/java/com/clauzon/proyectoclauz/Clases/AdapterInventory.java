package com.clauzon.proyectoclauz.Clases;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.proyectoclauz.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterInventory extends RecyclerView.Adapter<HolderInventory> implements View.OnClickListener{

    private View.OnClickListener listener;
    private List<Producto> lista = new ArrayList();
    private Context c;

    public AdapterInventory() {
    }

    @NonNull
    @Override
    public HolderInventory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(c).inflate(R.layout.relleno_recycler,parent,false);
        view.setOnClickListener(this);
        return new HolderInventory(view);
    }

    public AdapterInventory(Context c){
        this.c = c;
    }

    public void add_producto(Producto producto) {
        lista.add(producto);
        notifyItemInserted(lista.size());
    }



    @Override
    public void onBindViewHolder(@NonNull HolderInventory holder, int position) {
        holder.getNombre_item().setText(lista.get(position).getNombre_producto());
        holder.getDescripcion().setText(lista.get(position).getDescripcion());
        //holder.getDescripcion().setMovementMethod(new ScrollingMovementMethod());
        holder.getPrecio_compra().setText("Compra: " + String.valueOf(lista.get(position).getCompra_producto()));
        holder.getPrecio_venta().setText("Venta: " + String.valueOf(lista.get(position).getVenta_producto()));
        if (lista.get(position).isEstado() == true) {
            holder.getEstado().setText("Estado: Activo");
        } else if (lista.get(position).isEstado() == false) {
            holder.getEstado().setText("Estado: Inactivo");
        }
        Glide.with(c).load(lista.get(position).getFoto_producto()).centerCrop().override(450, 450).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto_item());
        holder.getCantidad_item().setText("#" + String.valueOf(lista.get(position).getCantidad_producto()));
        holder.getCategoria().setText(lista.get(position).getCategoria());
        holder.getEstado2().setText(lista.get(position).getEstado_producto());
        holder.getOferta().setText("Oferta: "+String.valueOf(lista.get(position).getOferta()));

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public String nombre_p(int i) {
        String nombre = lista.get(i).getNombre_producto();
        return nombre;
    }

    public Producto getPos(int i){

        return lista.get(i);
    }

    public List<Producto> get_lista(){

        return lista;
    }

    public Producto cual_producto(String cual) {
        Producto p_t = new Producto();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNombre_producto().equals(cual)) {
                p_t = lista.get(i);
            }
        }
        return p_t;
    }

    public void filterList(List<Producto> filteredList) {
        lista=filteredList;
        notifyDataSetChanged();
    }

    public List<Producto> getLista(){
        return lista;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

}

//*************************************************************************//


