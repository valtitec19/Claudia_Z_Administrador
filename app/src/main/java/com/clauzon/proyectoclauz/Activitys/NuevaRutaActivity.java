package com.clauzon.proyectoclauz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.clauzon.proyectoclauz.Clases.AdapterEstacion;
import com.clauzon.proyectoclauz.Clases.Estacion;
import com.clauzon.proyectoclauz.Clases.Metro;
import com.clauzon.proyectoclauz.Clases.Ruta;
import com.clauzon.proyectoclauz.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NuevaRutaActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private AdapterEstacion adapterEstacion;
    private String linea;
    private ArrayList<Estacion> estacions;
    private ArrayList<Estacion> new_ruta;
    private Ruta ruta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_ruta);
        Intent intent = getIntent();
        linea = intent.getExtras().getString("linea");
//        ruta =(Ruta) intent.getSerializableExtra("Nruta");
        if(intent.getSerializableExtra("estaciones") !=null){
            new_ruta =(ArrayList<Estacion>) intent.getSerializableExtra("estaciones");
            Log.e("Nueva ruta: ",String.valueOf(new_ruta.size()));

        }else{
            new_ruta=new ArrayList<>();
        }

        try {
            ruta=(Ruta)intent.getSerializableExtra("ruta");
            new_ruta=ruta.getEstaciones();
        }catch (Exception e){

        }
        //Toast.makeText(this, linea, Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_nueva_ruta_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterEstacion = new AdapterEstacion(this, new_ruta,linea);
        firebaseON();
        recyclerView.setAdapter(adapterEstacion);
    }

    public void firebaseON() {

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();//Catalogo de los productos

        reference.child("Metro" ).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Metro metro = dataSnapshot.getValue(Metro.class);
                if(metro.getLinea().equals(linea)){
                    estacions = metro.getEstaciones();
                    for (int i = 0; i < estacions.size(); i++) {
                        estacions.get(i).setLinea(linea);

                        adapterEstacion.add_lista(estacions.get(i));
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void regresar(View view) {
        Intent intent=new Intent(this,LineasActivity.class);
        intent.putExtra("estaciones",adapterEstacion.get_list());
        intent.putExtra("ruta",ruta);
        finish();
        startActivity(intent);
    }

    public void aceptar(View view) {
        Intent intent=new Intent(this,LineasActivity.class);
        intent.putExtra("estaciones",adapterEstacion.get_list());
        intent.putExtra("ruta",ruta);
        finish();
        startActivity(intent);
    }
}
