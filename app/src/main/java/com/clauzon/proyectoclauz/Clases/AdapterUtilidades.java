package com.clauzon.proyectoclauz.Clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.proyectoclauz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterUtilidades extends RecyclerView.Adapter<HolderUtilidades> implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Pedidos> lista = new ArrayList<>();
    private ArrayList<String> ganancias = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    float compra, ganancia;

    public AdapterUtilidades(Context context) {
        this.context = context;
    }

    public void add_pedido(Pedidos pedidos) {
        this.lista.add(pedidos);
        notifyItemInserted(this.lista.size());
    }


    @NonNull
    @Override
    public HolderUtilidades onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_tuilidades, parent, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos
        view.setOnClickListener(this);
        return new HolderUtilidades(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderUtilidades holder, final int position) {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference= database.getReference();
        holder.getProducto().setText(lista.get(position).getNombre());
        holder.getVenta().setText(String.valueOf(lista.get(position).getCosto()));
        holder.getCantidad().setText(String.valueOf(lista.get(position).getCantidad()));
        reference.child("Catalogo Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Producto producto = ds.getValue(Producto.class);
                    if(producto.getId_producto().equals(lista.get(position).getProducto_id())){
                        compra = producto.getCompra_producto();
                        ganancia = (lista.get(position).getCosto() * lista.get(position).getCantidad()) - (lista.get(position).getCantidad() * producto.getCompra_producto());
                        holder.getCompra().setText(String.valueOf(compra));
                        holder.getGanancia().setText(String.valueOf(ganancia));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Repartidores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Repartidor repartidor = ds.getValue(Repartidor.class);
                    if (repartidor.getId().equals(lista.get(position).getRepartidor_id())) {
                        holder.getRepartidor().setText(repartidor.getNombre());
                        holder.getCuenta_repartidor().setText(repartidor.getTarjeta());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.getFecha().setText(lista.get(position).getFecha());
        holder.getHora().setText(lista.get(position).getHora_entrega());
        holder.getLugar().setText(lista.get(position).getDireccion_entrega());

        reference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Usuario usuario=ds.getValue(Usuario.class);
                    if(lista.get(position).getUsuario_id().equals(usuario.getId())){
                        holder.getCliente().setText(usuario.getNombre());
                        holder.getTelefono().setText(usuario.getTelefono());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.getEstatus().setText(lista.get(position).getEstado());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public List<Pedidos> get_lista() {

        return lista;
    }

    public ArrayList<String> getGanancias() {

        return ganancias;
    }
}
