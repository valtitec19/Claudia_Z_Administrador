package com.clauzon.proyectoclauz.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.proyectoclauz.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdapterEnvio extends RecyclerView.Adapter<HolderEnvio> implements View.OnClickListener{
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Pedidos> lista = new ArrayList<>();
    private ArrayList<String> pedidos = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    private String nombres = "";

    public AdapterEnvio(Context context) {
        this.context = context;
    }

    public void add_pedido(Pedidos pedidos) {
        this.lista.add(pedidos);
        notifyItemInserted(this.lista.size());
    }



    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    @NonNull
    @Override
    public HolderEnvio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.relleno_recycler_envio, parent, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos
        view.setOnClickListener(this);
        return new HolderEnvio(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEnvio holder, int position) {
        //        if(lista.get(position).getEstado().equals("Pago pendiente (En efectivo)") || lista.get(position).getEstado().equals("Pagado")){
        holder.getProducto().setText((lista.get(position).getCantidad()+" ")+lista.get(position).getNombre());
        holder.getLugar().setText("Lugar: " + lista.get(position).getDireccion_entrega());
        holder.getFecha().setText("Fecha: " + lista.get(position).getFecha());
        holder.getEstado().setText(lista.get(position).getEstado());
        float cantidad = lista.get(position).getCantidad() * lista.get(position).getCosto();
        //holder.getCosto().setText("Costo: $"+String.valueOf(cantidad));
        Glide.with(context).load(lista.get(position).getFoto()).centerCrop().override(300, 300)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto());


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public ArrayList<String> lista_pedidos() {
        return pedidos;
    }
}
