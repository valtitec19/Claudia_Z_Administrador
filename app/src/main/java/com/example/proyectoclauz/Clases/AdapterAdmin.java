package com.example.proyectoclauz.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoclauz.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterAdmin extends RecyclerView.Adapter<HolderAdmin> implements View.OnClickListener{

    private List<Repartidor> lista = new ArrayList();
    private Context c;
    private View.OnClickListener listener;

    public AdapterAdmin() {
    }

    public AdapterAdmin(Context c) {
        this.c = c;
    }

    public void add_repartidor(Repartidor repartidor){
        lista.add(repartidor);
        notifyItemInserted(lista.size());
    }

    @NonNull
    @Override
    public HolderAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(c).inflate(R.layout.relleno_recycler_admin,parent,false);
        view.setOnClickListener(this);
        return new HolderAdmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAdmin holder, int position) {
        holder.getRepartidor().setText(lista.get(position).getNombre());
        holder.getTelefoto().setText(lista.get(position).getTelefono());
        holder.getCorreo_repartidor().setText(lista.get(position).getCorreo());
        Glide.with(c).
                load(lista.get(position).getImagenes().get(0))
                .centerCrop()
                .override(450, 450)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getFoto());
        if(lista.get(position).getEstado()){
            holder.getEstado().setImageDrawable(c.getResources().getDrawable(R.drawable.ic_check_black_24dp));
        }
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }
    public int getImage(String imageName) {

        int drawableResourceId = c.getResources().getIdentifier(imageName, "drawable", c.getPackageName());

        return drawableResourceId;
    }
    public List<Repartidor> getLista(){
        return lista;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }


}
