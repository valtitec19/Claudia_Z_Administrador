package com.clauzon.proyectoclauz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.clauzon.proyectoclauz.Clases.AdapterUtilidades;
import com.clauzon.proyectoclauz.Clases.Pedidos;
import com.clauzon.proyectoclauz.Clases.Producto;
import com.clauzon.proyectoclauz.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UtilidadesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterUtilidades adapterUtilidades;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private float total;
    private TextView utilidad_total;
    private TextView fecha_inicio, fecha_fin;
    private int dia, mes, año;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilidades);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        fecha_inicio = (TextView) findViewById(R.id.fecha_inicio_utilidades);
        fecha_fin = (TextView) findViewById(R.id.fecha_fina_utilidades);
        firebaseON();
        utilidad_total = (TextView) findViewById(R.id.total_utilidades);
        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_utilidades);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterUtilidades = new AdapterUtilidades(this);
        databaseReference.child("Pedidos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Pedidos pedidos = dataSnapshot.getValue(Pedidos.class);
                if (pedidos.getEstado().equals("Completado")) {
                    adapterUtilidades.add_pedido(pedidos);
                    databaseReference.child("Catalogo Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Producto producto = ds.getValue(Producto.class);
                                if (pedidos.getProducto_id().equals(producto.getId_producto())) {
                                    float temp = Float.parseFloat(utilidad_total.getText().toString());
                                    utilidad_total.setText(String.valueOf(temp + ((producto.getVenta_producto() - producto.getCompra_producto()) * pedidos.getCantidad())));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
        recyclerView.setAdapter(adapterUtilidades);

        fecha_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                año = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UtilidadesActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        fecha_inicio.setText(i + "-" + i1 + "-" + i2);
                    }
                }
                        , año, mes, dia);
                datePickerDialog.show();
            }
        });

        fecha_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                año = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UtilidadesActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        fecha_fin.setText(i + "-" + i1 + "-" + i2);
                    }
                }
                        , año, mes, dia);
                datePickerDialog.show();
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
        databaseReference = database.getReference();
    }

    public void total() {
        Toast.makeText(this, String.valueOf(adapterUtilidades.getGanancias().size()), Toast.LENGTH_SHORT).show();
    }

    public void fecha_inicio(View view) {
        final Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        año = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog =new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                fecha_inicio.setText(i2+"-"+i1+"-"+i);
            }
        },año,mes,dia);
        datePickerDialog.show();
    }

    public void fecha_fin(View view) {
        final Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        año = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog =new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                fecha_fin.setText(i2+"-"+i1+"-"+i);
            }
        },año,mes,dia);
        datePickerDialog.show();
    }
}
