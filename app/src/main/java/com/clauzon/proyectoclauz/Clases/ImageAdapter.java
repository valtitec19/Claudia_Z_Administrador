package com.clauzon.proyectoclauz.Clases;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imagenes = new ArrayList<>();
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
        ImageView imageView = new ImageView(context);
        Glide.with(context).load(imagenes.get(position)).centerCrop().fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        container.addView(imageView, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Nueva imagen");
                builder.setCancelable(false);
                builder.setPositiveButton("Eliminar esta foto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        quitar_imagen(imagenes.get(position));
                    }
                });
                builder.setNegativeButton("Agregar nueva foto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });

        return imageView;

    }

    private void quitar_imagen(String url) {
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("IMAGENES CATALOGO PRODUCTOS/" + url);

        // Delete the file
//        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(context, "Imagen eliminada", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Uh-oh, an error occurred!
//                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
