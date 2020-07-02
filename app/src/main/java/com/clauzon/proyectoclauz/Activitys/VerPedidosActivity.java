package com.clauzon.proyectoclauz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.proyectoclauz.Clases.Pedidos;
import com.clauzon.proyectoclauz.Clases.Repartidor;
import com.clauzon.proyectoclauz.Clases.Usuario;
import com.clauzon.proyectoclauz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerPedidosActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private Pedidos recibido;
    private String concepto2;

    private TextView usuario, descripcon, fecha, lugar, producto, costo, estado_pago, cantidad, repartidor_ver, telefono, hora, tipo_envio;
    private ImageView imageView;
    private RadioButton pendiente, cancelado, entregado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedidos);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        inicio_views();
        firebaseON();
        try {
            Intent i = getIntent();
            recibido = (Pedidos) i.getSerializableExtra("pedido");
            recupera_pedidos();
        } catch (Exception e) {

        }
        try {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.cancel(0);
            Intent i = getIntent();
            concepto2 = i.getExtras().getString("concepto2");
            databaseReference.child("Pedidos").child(concepto2).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    recibido = dataSnapshot.getValue(Pedidos.class);
                    recupera_pedidos();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }

    }

    public void inicio_views() {
        usuario = (TextView) findViewById(R.id.usuario_pedido_final);
        descripcon = (TextView) findViewById(R.id.descripcion_pedido_final);
        fecha = (TextView) findViewById(R.id.fecha_pedido_final);
        lugar = (TextView) findViewById(R.id.lugar_pedido_final);
        producto = (TextView) findViewById(R.id.producto_pedido_final);
        costo = (TextView) findViewById(R.id.costo_pedido_final);
        estado_pago = (TextView) findViewById(R.id.estado_pago);
        cantidad = (TextView) findViewById(R.id.cantidad_pedido_final);
        imageView = (ImageView) findViewById(R.id.imageView_pedido_finalizado);
        repartidor_ver = (TextView) findViewById(R.id.repatidor_ver_pedido);
        telefono = (TextView) findViewById(R.id.telefono_ver_pedido);
        pendiente = (RadioButton) findViewById(R.id.r_button_pendiente);
        cancelado = (RadioButton) findViewById(R.id.r_button_cancelado);
        entregado = (RadioButton) findViewById(R.id.r_button_entregado);
        hora = (TextView) findViewById(R.id.hora_pedido_final);
        tipo_envio = (TextView) findViewById(R.id.tipo_envio_pedido);
    }

    public void firebaseON() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void recupera_pedidos() {
        databaseReference.child("Pedidos/" + recibido.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Pedidos pedidos = dataSnapshot.getValue(Pedidos.class);
                producto.setText(pedidos.getNombre());
                descripcon.setText(pedidos.getDescripcion());
                lugar.setText(pedidos.getDireccion_entrega());
                fecha.setText("Fecha: " + pedidos.getFecha());
                if (pedidos.getHora_entrega().equals("00:00")) {
                    hora.setVisibility(View.GONE);
                } else {
                    hora.setText(pedidos.getHora_entrega());
                }

                if (pedidos.getCosto_envio() == 100) {
                    tipo_envio.setText("Entrega 3 días");
                }
                if (pedidos.getCosto_envio() == 120) {
                    tipo_envio.setText("Entrega 2 días");
                }
                if (pedidos.getCosto_envio() == 150) {
                    tipo_envio.setText("Entrega 1 días");
                } else {
                    tipo_envio.setVisibility(View.GONE);
                }
                costo.setText("$" + String.valueOf(pedidos.getCosto() * pedidos.getCantidad()));
                estado_pago.setText(pedidos.getEstado());
                cantidad.setText(recibido.getCantidad() + " Unidades");
                Glide.with(VerPedidosActivity.this).load(recibido.getFoto()).centerCrop().override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                databaseReference.child("Repartidores/" + recibido.getRepartidor_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Repartidor repartidor = dataSnapshot.getValue(Repartidor.class);
                        if (pedidos.getCosto_envio() == 0) {
                            repartidor_ver.setText(repartidor.getNombre() + " " + repartidor.getApellidos());
                        } else {
                            repartidor_ver.setText("Envío a domicilio");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario u = snapshot.getValue(Usuario.class);
                    if (u.getId().equals(recibido.getUsuario_id())) {
                        usuario.setText(u.getNombre() + " " + u.getApellidos());
                        telefono.setText(u.getTelefono());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Aceptar(View view) {
        if (cancelado.isChecked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerPedidosActivity.this);
            builder.setTitle("Pedido cancelado").setMessage("¿La cancelación require multa?");
            builder.setPositiveButton("Cancelar y multar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recibido.setRepartidor_id("no asignado");
                    recibido.setEstado("Cancelado");
                    databaseReference.child("Usuarios/" + recibido.getUsuario_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            usuario.setMultas(usuario.getMultas()+1);
                            databaseReference.child("Usuarios/"+recibido.getUsuario_id()).setValue(usuario);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    DatabaseReference databaseReference1 = database.getReference("Pedidos/" + recibido.getId());
                    databaseReference1.setValue(recibido);
                    startActivity(new Intent(VerPedidosActivity.this, PedidosActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("Solo Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseReference databaseReference1 = database.getReference("Pedidos/" + recibido.getId());
                    databaseReference1.setValue(recibido);
                    startActivity(new Intent(VerPedidosActivity.this, PedidosActivity.class));
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } else if (entregado.isChecked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerPedidosActivity.this);
            builder.setTitle("Pedido completado").setMessage("¿El pedido fue completado exitosamente?");
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recibido.setEstado("Completado");
                    DatabaseReference databaseReference1 = database.getReference("Pedidos/" + recibido.getId());
                    databaseReference1.setValue(recibido);
                    Intent intent = new Intent(VerPedidosActivity.this, PedidosActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } else if (pendiente.isChecked()) {
            startActivity(new Intent(VerPedidosActivity.this, PedidosActivity.class));
            finish();
        }
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
