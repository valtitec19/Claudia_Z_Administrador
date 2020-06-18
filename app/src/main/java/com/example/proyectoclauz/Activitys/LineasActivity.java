package com.example.proyectoclauz.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoclauz.Clases.Estacion;
import com.example.proyectoclauz.Clases.Repartidor;
import com.example.proyectoclauz.Clases.Ruta;
import com.example.proyectoclauz.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class LineasActivity extends AppCompatActivity {
    private Resources resources;
    private Button button;
    private CircleImageView imagen1, imagen2, imagen3, imagen4, imagen5, imagen6, imagen7, imagen8, imagen9, imagen12, imagenA, imagenB;
    private ArrayList<Estacion> new_ruta;
    private TextView vista_previa_ruta,nombre;
    private ArrayList<String> repartidores=new ArrayList<>();
    Ruta ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineas);
        inicio_imagenes();
        Intent intent = getIntent();
        vista_previa_ruta=(TextView)findViewById(R.id.vista_previa_ruta);
        nombre=(TextView)findViewById(R.id.nombre_ruta);
        if (intent.getSerializableExtra("estaciones") != null) {
            new_ruta = (ArrayList<Estacion>) intent.getSerializableExtra("estaciones");
            for(int i=0;i<new_ruta.size();i++){
                vista_previa_ruta.setText(vista_previa_ruta.getText().toString()+"; "+new_ruta.get(i).getNombre()+", "+new_ruta.get(i).getLinea()+" - "+new_ruta.get(i).getHora());
            }
        } else {
            new_ruta = new ArrayList<>();
        }
        try {
            ruta=(Ruta)intent.getSerializableExtra("ruta");
            new_ruta=ruta.getEstaciones();
            nombre.setText(ruta.getNombre());
        }catch (Exception e){
            ruta=new Ruta();
            ruta.setNombre("");
        }




    }

    public void l1(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 1");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void l2(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 2");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void l3(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 3");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void l4(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 4");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void l5(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 5");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void l6(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 6");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void l7(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 7");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void l8(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 8");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void l9(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 9");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void l12(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea 12");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void lA(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea A");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void lB(View view) {
        Intent intent = new Intent(LineasActivity.this, NuevaRutaActivity.class);
        intent.putExtra("linea", "Linea B");
        intent.putExtra("estaciones",new_ruta);
        intent.putExtra("ruta",ruta);
        startActivity(intent);
    }

    public void inicio_imagenes() {
        imagen1 = (CircleImageView) findViewById(R.id.image1);
        imagen2 = (CircleImageView) findViewById(R.id.image2);
        imagen3 = (CircleImageView) findViewById(R.id.image3);
        imagen4 = (CircleImageView) findViewById(R.id.image4);
        imagen5 = (CircleImageView) findViewById(R.id.image5);
        imagen6 = (CircleImageView) findViewById(R.id.image6);
        imagen7 = (CircleImageView) findViewById(R.id.image7);
        imagen8 = (CircleImageView) findViewById(R.id.image8);
        imagen9 = (CircleImageView) findViewById(R.id.image9);
        imagen12 = (CircleImageView) findViewById(R.id.image12);
        imagenA = (CircleImageView) findViewById(R.id.imageA);
        imagenB = (CircleImageView) findViewById(R.id.imageB);

        Glide.with(LineasActivity.this).load(getImage("linea1")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen1);
        Glide.with(LineasActivity.this).load(getImage("linea2")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen2);
        Glide.with(LineasActivity.this).load(getImage("linea3")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen3);
        Glide.with(LineasActivity.this).load(getImage("linea4")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen4);
        Glide.with(LineasActivity.this).load(getImage("linea5")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen5);
        Glide.with(LineasActivity.this).load(getImage("linea6")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen6);
        Glide.with(LineasActivity.this).load(getImage("linea7")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen7);
        Glide.with(LineasActivity.this).load(getImage("linea8")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen8);
        Glide.with(LineasActivity.this).load(getImage("linea9")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen9);
        Glide.with(LineasActivity.this).load(getImage("linea12")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen12);
        Glide.with(LineasActivity.this).load(getImage("lineaa")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagenA);
        Glide.with(LineasActivity.this).load(getImage("lineab")).centerCrop().override(800, 800).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagenB);


    }

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }

    public void cancelar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancelar nueva ruta").setMessage("¿Desea cancelar la creación de la ruta?");
        builder.setCancelable(false);
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(LineasActivity.this, MainActivity.class));
                finish();
            }
        });
        builder.create().show();
    }

    public void aceptar(View view) {
        if(new_ruta.size()==0){
            Toast.makeText(this, "Agregue estaciones", Toast.LENGTH_SHORT).show();
        }else if(nombre.getText().toString().isEmpty()){
            Toast.makeText(this, "Escriba el nombre de la ruta", Toast.LENGTH_SHORT).show();
        }else if(ruta.getNombre().equals("")){
            String value= UUID.randomUUID().toString();
            Ruta ruta=new Ruta(nombre.getText().toString(),value,true,new_ruta,"");
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Rutas").child(value).setValue(ruta);
            startActivity(new Intent(this,RutasActivity.class));
            finish();
        }else {
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Rutas").child(ruta.getId()).setValue(ruta);
            startActivity(new Intent(this,RutasActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancelar nueva ruta").setMessage("¿Desea cancelar la creación de la ruta?");
        builder.setCancelable(false);
        builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(LineasActivity.this, MainActivity.class));
                finish();
            }
        });
        builder.create().show();
    }
}
