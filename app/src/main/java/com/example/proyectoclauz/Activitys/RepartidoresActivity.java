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

import com.example.proyectoclauz.Clases.AdapterAdmin;
import com.example.proyectoclauz.Clases.Repartidor;
import com.example.proyectoclauz.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RepartidoresActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterAdmin adapterAdmin;
    private Toolbar toolbar;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidores);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        firebaseON();

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_admin);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterAdmin = new AdapterAdmin(this);
        adapterAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RepartidoresActivity.this, ActivarRepartidorActivity.class);
                intent.putExtra("repartidor",adapterAdmin.getLista().get(recyclerView.getChildAdapterPosition(view)));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapterAdmin);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Repartidor repartidor=dataSnapshot.getValue(Repartidor.class);
                adapterAdmin.add_repartidor(repartidor);
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
    public void firebaseON(){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Repartidores");
    }
}
