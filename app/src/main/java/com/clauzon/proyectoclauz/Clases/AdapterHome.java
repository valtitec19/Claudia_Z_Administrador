package com.clauzon.proyectoclauz.Clases;

import android.content.Context;
import android.graphics.Color;
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

public class AdapterHome extends RecyclerView.Adapter<HolderHome> implements View.OnClickListener{

    private List<Pedidos> lista = new ArrayList();
    private Context c;
    private View.OnClickListener listener;

    public List<Pedidos> ger_lista(){
        return lista;
    }

    public AdapterHome() {
    }

    public AdapterHome(Context c) {
        this.c = c;
    }


    public void add_producto(Pedidos producto){
        lista.add(producto);
        notifyItemInserted(lista.size());
    }
    @NonNull
    @Override
    public HolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(c).inflate(R.layout.relleno_recycler_home,parent,false);
        view.setOnClickListener(this);
        return new HolderHome(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHome holder, int position) {
        holder.getNombre_recycler_home().setText(lista.get(position).getNombre()+" "+lista.get(position).getColor()+" "+lista.get(position).getTamano()+" "+lista.get(position).getModelo());
        holder.getLugar_recycler_home().setText(lista.get(position).getDireccion_entrega());
        holder.getEstado_recycler_home().setText(lista.get(position).getEstado());
        holder.getCantidad_recycler_home().setText(String.valueOf(lista.get(position).getCantidad()));
        if(lista.get(position).getEstado().equals("Pago Realizado")){
            holder.getEstado_recycler_home().setBackgroundColor(Color.parseColor("#B2FF5D"));

        }
        else{
            holder.getEstado_recycler_home().setBackgroundColor(Color.parseColor("#F7FF15"));
        }
        Glide.with(c).load(lista.get(position).getFoto()).centerCrop().override(450,450).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto());



    }

    @Override
    public int getItemCount() {
        return lista.size();
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
