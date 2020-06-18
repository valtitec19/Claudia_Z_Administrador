package com.example.proyectoclauz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectoclauz.Clases.AdapterAdmin;
import com.example.proyectoclauz.Clases.AdapterPedidos;
import com.example.proyectoclauz.Clases.Pedidos;
import com.example.proyectoclauz.Clases.Repartidor;
import com.example.proyectoclauz.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PedidosActivity extends AppCompatActivity {

    private Repartidor r_recibido;
    private RecyclerView recyclerView;
    private AdapterPedidos adapterPedidos,adapterPedidos2,adapterPedidos3,adapterPedidos4;
    private Toolbar toolbar;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Spinner spinner;
    private String seleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        rellena_recycler();
    }

    public void rellena_recycler() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        firebaseON();
        inicio_spinner();
        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_pedidos_fragment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterPedidos = new AdapterPedidos(this);
        adapterPedidos2 = new AdapterPedidos(this);
        adapterPedidos3 = new AdapterPedidos(this);
        adapterPedidos4 = new AdapterPedidos(this);
        adapterPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pedidos pedidos = adapterPedidos.getLista().get(recyclerView.getChildAdapterPosition(view));
                Intent intent = new Intent(PedidosActivity.this, VerPedidosActivity.class);
                intent.putExtra("pedido", pedidos);
                startActivity(intent);
            }
        });
        adapterPedidos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pedidos pedidos = adapterPedidos.getLista().get(recyclerView.getChildAdapterPosition(view));
                Intent intent = new Intent(PedidosActivity.this, VerPedidosActivity.class);
                intent.putExtra("pedido", pedidos);
                startActivity(intent);
            }
        });

        adapterPedidos3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pedidos pedidos = adapterPedidos.getLista().get(recyclerView.getChildAdapterPosition(view));
                Intent intent = new Intent(PedidosActivity.this, VerPedidosActivity.class);
                intent.putExtra("pedido", pedidos);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapterPedidos);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pedidos pedidos = dataSnapshot.getValue(Pedidos.class);
                if (pedidos.getEstado().equals("Pago pendiente (En efectivo)") || pedidos.getEstado().equals("Pago Realizado")) {
                    adapterPedidos.add_pedido(pedidos);
                    if(pedidos.getCosto_envio()>0){
                        adapterPedidos2.add_pedido(pedidos);
                    }else if(pedidos.getCosto_envio()==0){
                        adapterPedidos3.add_pedido(pedidos);
                    }

                }else if(pedidos.getEstado().equals("Cancelado")){
                    adapterPedidos4.add_pedido(pedidos);
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

    private void inicio_spinner() {
        String[] opciones = {"Todas las entregas","Entregas Personales", "Entregas a domicilio","Pedidos cancelados"};
        final ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(this,R.layout.spinner,opciones);
        spinner= (Spinner)findViewById(R.id.spinner_pedidos);
        spinner.setAdapter(adapter_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seleccion=adapterView.getItemAtPosition(i).toString();
                if(seleccion.equals("Todas las entregas")){
                    recyclerView.setAdapter(adapterPedidos);
                }else if(seleccion.equals("Entregas Personales")){
                    recyclerView.setAdapter(adapterPedidos3);
                }else if(seleccion.equals("Entregas a domicilio")){
                    recyclerView.setAdapter(adapterPedidos2);
                }else if(seleccion.equals("Pedidos cancelados")){
                    recyclerView.setAdapter(adapterPedidos4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

    public void firebaseON() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Pedidos");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
