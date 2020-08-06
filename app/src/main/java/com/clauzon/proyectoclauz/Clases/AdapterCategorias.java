package com.clauzon.proyectoclauz.Clases;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.clauzon.proyectoclauz.Activitys.NewProduct;
import com.clauzon.proyectoclauz.Activitys.VerCategoriasActivity;
import com.clauzon.proyectoclauz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class AdapterCategorias extends RecyclerView.Adapter<HolderCategorias> implements View.OnClickListener {
    ArrayList<String> lista=new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Context context;
    private View.OnClickListener listener;
     int temp=0;

    public AdapterCategorias(Context context) {
        this.context = context;
    }

    public void add_lista(String cateforia){
        lista.add(cateforia);
        notifyItemInserted(this.lista.size());
    }

    @Override
    public void onClick(View view) {

    }

    @NonNull
    @Override
    public HolderCategorias onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.relleno_recycler_categoria, parent, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos
        view.setOnClickListener(this);
        return new HolderCategorias(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderCategorias holder, int position) {

        holder.getCategoria().setText(lista.get(position));
        holder.getBorrar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                databaseReference.child("Catalogo Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()){
                            Producto producto = ds.getValue(Producto.class);
                            if(producto.getCategoria().equals(holder.getCategoria().getText().toString())){
                                temp++;

                            }
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("¿Eliminar categoría?");
                        builder.setMessage("Hay "+String.valueOf(temp)+" productos en esta categoría");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Eliminar categoría", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(temp==0){
                                    DatabaseReference reference_borrar = database.getReference("Categorias");
                                    reference_borrar.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot ds :snapshot.getChildren()){
                                                String cat_temp=ds.getValue(String.class);
                                                if(cat_temp.equals(holder.getCategoria().getText().toString())){
                                                    DatabaseReference reference_borrar_final = database.getReference("Categorias/" + ds.getKey());
                                                    reference_borrar_final.removeValue();
                                                    context.startActivity(new Intent(context, VerCategoriasActivity.class));
                                                    ((Activity) context).finish();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }else {
                                    final EditText editText = new EditText(context);
                                    editText.setPadding(10,10,10,10);
                                    editText.setFocusable(true);
                                    final String id_random = UUID.randomUUID().toString();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Nueva categoría");
                                    builder.setMessage("Los productos seran actualizados a esta nueva categoría");
                                    builder.setView(editText);
                                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            final String categoria_temp = editText.getText().toString();
                                            if (categoria_temp.isEmpty()) {
                                                Toast.makeText(context, "Coloque una categoría valida", Toast.LENGTH_SHORT).show();
                                            } else {
                                                DatabaseReference reference = database.getReference("Categorias");
                                                DatabaseReference reference_borrar = database.getReference("Categorias");
                                                reference_borrar.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ds :snapshot.getChildren()){
                                                            String cat_temp=ds.getValue(String.class);
                                                            if(cat_temp.equals(holder.getCategoria().getText().toString())){
                                                                DatabaseReference reference_borrar_final = database.getReference("Categorias/" + ds.getKey());
                                                                reference_borrar_final.removeValue();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                reference.child(id_random).setValue(categoria_temp);
                                                databaseReference.child("Catalogo Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ds : snapshot.getChildren()){
                                                            Producto producto = ds.getValue(Producto.class);
                                                            if(producto.getCategoria().equals(holder.getCategoria().getText().toString())){
                                                                producto.setCategoria(categoria_temp);
                                                                DatabaseReference new_product=database.getReference().child("Catalogo Productos").child(producto.getId_producto());
                                                                new_product.setValue(producto);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


                                            }

                                            context.startActivity(new Intent(context, VerCategoriasActivity.class));
                                            ((Activity) context).finish();
                                        }
                                    });
                                    builder.create().show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.create().show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public ArrayList<String> get_Lista() {
        return lista;
    }
}
