package com.clauzon.proyectoclauz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.clauzon.proyectoclauz.Clases.AdapterEnvio;
import com.clauzon.proyectoclauz.Clases.Pedidos;
import com.clauzon.proyectoclauz.Clases.Repartidor;
import com.clauzon.proyectoclauz.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EnvioPedidosActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Repartidor r_recibido;
    private RecyclerView recyclerView;
    private AdapterEnvio adapterEnvio;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button aceptar;
    private ArrayList<String> lista_pedidos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_pedidos);
        firebaseON();
        rellena_recycler();
    }

    public void rellena_recycler(){
        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_envio_pedidos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterEnvio = new AdapterEnvio(this);
        adapterEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        recyclerView.setAdapter(adapterEnvio);
        databaseReference.child("Pedidos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Pedidos pedidos = dataSnapshot.getValue(Pedidos.class);
                if(pedidos.getEstado().equals("Pago pendiente (En efectivo)") || pedidos.getEstado().equals("Pago Realizado")){
                    if(pedidos.getCosto_envio()>0){
                        adapterEnvio.add_pedido(pedidos);
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
    public void firebaseON(){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manu_inventory, menu);
        return true;
    }
}
