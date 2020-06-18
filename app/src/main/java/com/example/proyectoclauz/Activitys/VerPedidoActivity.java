package com.example.proyectoclauz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoclauz.Clases.Pedidos;
import com.example.proyectoclauz.Clases.Usuario;
import com.example.proyectoclauz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerPedidoActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Pedidos recibido;


    private TextView usuario,descripcon,fecha,lugar,producto,costo,estado_pago,cantidad,hora;
    private ImageView imageView;
    private RadioButton pendiente,cancelado,entregado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedido);
        inicio_views();
        firebaseON();
        Intent i = getIntent();
        recibido = (Pedidos) i.getSerializableExtra("pedido");
        recupera_pedidos();

    }

    public void inicio_views(){
        usuario=(TextView)findViewById(R.id.usuario_pedido_final);
        descripcon=(TextView)findViewById(R.id.descripcion_pedido_final);
        fecha=(TextView)findViewById(R.id.fecha_pedido_final);
        lugar=(TextView)findViewById(R.id.lugar_pedido_final);
        producto=(TextView)findViewById(R.id.producto_pedido_final);
        costo=(TextView)findViewById(R.id.costo_pedido);
        estado_pago=(TextView)findViewById(R.id.total_pedido);
        cantidad=(TextView)findViewById(R.id.cantidad_pedido_final);
        imageView=(ImageView) findViewById(R.id.imageView_pedido_finalizado);
        hora=(TextView) findViewById(R.id.hora_pedido_final);

    }

    public void firebaseON(){
        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference();

    }

    public void recupera_pedidos(){
        databaseReference.child("Pedidos/"+recibido.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pedidos pedidos=dataSnapshot.getValue(Pedidos.class);
                producto.setText(pedidos.getNombre());
                descripcon.setText(pedidos.getDescripcion());
                lugar.setText(pedidos.getDireccion_entrega());
                fecha.setText(pedidos.getFecha());
                if(pedidos.getHora_entrega().equals("")){
                    hora.setText("no asignada");
                }else {
                    hora.setText(pedidos.getHora_entrega());
                }
                if(pedidos.getCosto_envio()>0){
                    hora.setVisibility(View.GONE);
                }else{
                    hora.setVisibility(View.VISIBLE);
                }
                costo.setText("$"+String.valueOf(pedidos.getCosto()*pedidos.getCantidad()));
                estado_pago.setText(pedidos.getEstado());
                cantidad.setText(recibido.getCantidad()+" Unidades");
                Glide.with(VerPedidoActivity.this).load(recibido.getFoto()).centerCrop().override(250, 250)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuario u=snapshot.getValue(Usuario.class);
                    if(u.getId().equals(recibido.getUsuario_id())){
                        usuario.setText("Pedido para "+u.getNombre()+" "+u.getApellidos());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Aceptar(View view) {
        startActivity(new Intent(VerPedidoActivity.this,MainActivity.class));
        finish();
    }
}
