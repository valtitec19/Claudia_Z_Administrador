package com.example.proyectoclauz.Clases;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoclauz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterPedidos extends RecyclerView.Adapter<HolderPedidos> implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private List<Pedidos> lista = new ArrayList<>();
    private ArrayList<String> pedidos = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;
    private String nombres = "";
    private int hora,minutos;
    public AdapterPedidos(Context context) {
        this.context = context;
    }

    public void add_pedido(Pedidos pedidos) {
        this.lista.add(pedidos);
        notifyItemInserted(this.lista.size());
    }

    @NonNull
    @Override
    public HolderPedidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_pedidos, parent, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos
        view.setOnClickListener(this);
        return new HolderPedidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderPedidos holder, final int position) {

//        if(lista.get(position).getEstado().equals("Pago pendiente (En efectivo)") || lista.get(position).getEstado().equals("Pagado")){
        holder.getProductos_pedido().setText(lista.get(position).getNombre());
        holder.getLugar().setText("Lugar: " + lista.get(position).getDireccion_entrega());
        holder.getFecha().setText("Fecha: " + lista.get(position).getFecha());
        if (lista.get(position).getHora_entrega().equals("00:00")) {
            holder.getHora().setVisibility(View.GONE);


        } else if(lista.get(position).getHora_entrega().equals("")){
            holder.getHora().setText("Hora no asignada");
        }else{
            holder.getHora().setText(lista.get(position).getHora_entrega());
            holder.getHora().setVisibility(View.VISIBLE);
        }
        holder.getCantidad().setText(String.valueOf(lista.get(position).getCantidad()) + " Piezas de: ");
        holder.getEstado().setText(lista.get(position).getEstado());
        float cantidad = lista.get(position).getCantidad() * lista.get(position).getCosto();
        //holder.getCosto().setText("Costo: $"+String.valueOf(cantidad));
        Glide.with(context).load(lista.get(position).getFoto()).centerCrop().override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.getFoto());
//            databaseReference.child("Usuarios/"+lista.get(position).getUsuario_id()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Usuario usuario=dataSnapshot.getValue(Usuario.class);
//                    holder.getCliente().setText("Cliente: "+usuario.getNombre());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

        //  }

        if(lista.get(position).getCosto_envio()==0){
            holder.getEntrega().setText("Entrega Personal");
        }else {
            holder.getEntrega().setText("Entrega a domicilio");
        }
        holder.getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lista.get(position).getHora_entrega().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Hora de entrega no asignada");
                    builder.setMessage("Asgine una hora de entrega");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            holder.getCheckBox().setChecked(false);

                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            holder.getCheckBox().setChecked(false);
                        }
                    });
                    builder.create().show();
                }else{
                    if (holder.getCheckBox().isChecked()) {
                        pedidos.add(lista.get(position).getId());
                    } else {
                        pedidos.remove(lista.get(position).getId());
                    }
                }
            }
        });

//        holder.getHora().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final Calendar calendar = Calendar.getInstance();
//                hora = calendar.get(Calendar.HOUR_OF_DAY);
//                minutos = calendar.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                        holder.getHora().setText(i+":"+i1);
//                        Pedidos pedidos=lista.get(position);
//                        pedidos.setHora_entrega(holder.getHora().getText().toString());
//                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                        DatabaseReference databaseReference = database.getReference();
//                        databaseReference.child("Pedidos").child(lista.get(position).getId()).setValue(pedidos);
//                    }
//                }, hora, minutos, false);
//                timePickerDialog.show();
//
//            }
//        });

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

    public ArrayList<String> lista_pedidos() {
        return pedidos;
    }

    public List<Pedidos> getLista() {
        return lista;
    }
}
