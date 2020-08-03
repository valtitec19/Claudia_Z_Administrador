package com.clauzon.proyectoclauz.Clases;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.proyectoclauz.Activitys.EditActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imagenes = new ArrayList<>();
    private ArrayList<String> imagenes_temp;
    private Producto producto;

    @Override
    public int getCount() {
        return imagenes.size();
    }

    public ImageAdapter(Context context, ArrayList<String> imagenes, Producto producto) {
        this.context = context;
        this.imagenes = imagenes;
        this.producto = producto;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);
        Glide.with(context).load(imagenes.get(position)).centerCrop().fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        container.addView(imageView, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar imagen");
                builder.setCancelable(false);
                builder.setPositiveButton("¿Desea eliminar esta foto?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imagenes_temp=producto.getImagenes();
                        Log.e("Antes", String.valueOf(imagenes_temp.size()) );
                        imagenes_temp.remove(imagenes.get(position));
                        Log.e("Temp", String.valueOf(imagenes_temp.size()) );
                        producto.setImagenes(imagenes_temp);

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Catalogo Productos").child(producto.getId_producto()).setValue(producto);
                        Intent intent=new Intent(context,EditActivity.class);
                        intent.putExtra("p_send",producto);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });

        return imageView;

    }

    public ArrayList<String> getArray() {
        // Create a storage reference from our app

        return producto.getImagenes();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }


}
