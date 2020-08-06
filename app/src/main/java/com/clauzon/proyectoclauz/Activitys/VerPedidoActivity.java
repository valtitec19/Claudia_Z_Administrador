package com.clauzon.proyectoclauz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.proyectoclauz.Clases.Pedidos;
import com.clauzon.proyectoclauz.Clases.Usuario;
import com.clauzon.proyectoclauz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerPedidoActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Pedidos recibido;


    private TextView usuario, descripcon, tipo_envio, producto, costo, cantidad, direccion, telefono, txt_tipo_envio;
    private CircleImageView imageView;
    private RadioButton pendiente, enviado;
    private EditText no_seguimiento;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedido);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        inicio_views();
        firebaseON();
        Intent i = getIntent();
        recibido = (Pedidos) i.getSerializableExtra("pedido");
        recupera_pedidos();

    }

    public void inicio_views() {
        usuario = (TextView) findViewById(R.id.cliente_enviar);
        descripcon = (TextView) findViewById(R.id.descripcion_enviar);
        tipo_envio = (TextView) findViewById(R.id.tipo_envio_enviar);
        producto = (TextView) findViewById(R.id.producto_enviar);
        costo = (TextView) findViewById(R.id.costo_enviar);
        cantidad = (TextView) findViewById(R.id.cantidad_enviar);
        imageView = (CircleImageView) findViewById(R.id.circlar_image_enviar);
        no_seguimiento = (EditText) findViewById(R.id.no_seguimiento);
        pendiente = (RadioButton) findViewById(R.id.radio_button_envio_pendiente_enviar);
        enviado = (RadioButton) findViewById(R.id.radio_button_envio_enviado_enviar);
        direccion = (TextView) findViewById(R.id.direccion_enviar);
        telefono = (TextView) findViewById(R.id.cliente_telefono);

    }

    public void firebaseON() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

    }

    public void recupera_pedidos() {
        databaseReference.child("Pedidos/" + recibido.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pedidos pedidos = dataSnapshot.getValue(Pedidos.class);
                try {
                    producto.setText(pedidos.getNombre());
                    descripcon.setText("");
                    if (pedidos.getColor() != null && !pedidos.getColor().equals("")) {
                        descripcon.setText(descripcon.getText().toString() + "Color: " + pedidos.getColor());
                    }
                    if (pedidos.getTamano() != null && !pedidos.getTamano().equals("")) {
                        descripcon.setText(descripcon.getText().toString() + " Tamaño: " + pedidos.getTamano());
                    }
                    if (pedidos.getModelo() != null && !pedidos.getModelo().equals("")) {
                        descripcon.setText(descripcon.getText().toString() + " Modelo: " + pedidos.getModelo());
                    }
                    if (descripcon.getText().toString().isEmpty()) {
                        descripcon.setText(pedidos.getDescripcion());
                    }

                    String[] direccion_2 = pedidos.getDireccion_entrega().split(",");
                    String name = direccion_2[0];
                    String direccion_final = direccion_2[1];
                    Log.e("nomnbre: ", name);
                    Log.e("direccion: ", direccion_final);
                    //direccion.setText(pedidos.getDireccion_entrega());
                    direccion.setText(name + "\n");
                    for (int i = 1; i < direccion_2.length - 1; i++) {
                        if (i == 1) {
                            direccion.setText(direccion.getText().toString() + "" + direccion_2[i]);
                        } else {
                            direccion.setText(direccion.getText().toString() + ", " + direccion_2[i]);
                        }
                    }

                    costo.setText("$" + String.valueOf(pedidos.getCosto() * pedidos.getCantidad()) + "\n +" + pedidos.getCosto_envio() + " de envío");
                    if (recibido.getCantidad() == 1) {
                        cantidad.setText(recibido.getCantidad() + " Producto");
                    } else {
                        cantidad.setText(recibido.getCantidad() + " Productos");
                    }
                    if (recibido.getCosto_envio() == 150) {
                        tipo_envio.setText("Al día siguiente, CDMX");
                    } else if (recibido.getCosto_envio() == 120) {
                        tipo_envio.setText("Correos de México");
                    } else {
                        tipo_envio.setText("FedEx");
                    }
                    Glide.with(VerPedidoActivity.this).load(recibido.getFoto()).centerCrop().override(250, 250)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                } catch (Exception e) {

                }

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
                    try {
                        if (u.getId().equals(recibido.getUsuario_id())) {
                            usuario.setText(u.getNombre() + " " + u.getApellidos());
                            telefono.setText(u.getTelefono());
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void crear_notificacion(String token, String titulo, String detalle, String imagen) {

        RequestQueue mRequestQue = Volley.newRequestQueue(this);

        JSONObject json = new JSONObject();
        try {

            json.put("to", token);

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("titulo", titulo);
            notificationObj.put("detalle", detalle);
            notificationObj.put("imagen", imagen);


            //replace notification with data when went send data
            json.put("data", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json, null, null) {


                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAE3HNDFU:APA91bEmPKbwtdaQIrU9g2GmxBEwy7zqHzdwG-L3I7o6HzrKhJ5BupTBTqhN67ytbObOv_NUILcDMaG-HwCLi2tEFKDwOWShs14ZOGpWZOh2DJNhxwjAQIfPtWgn7sxWuDR9VfT4uPQW");
                    return header;
                }
            };


            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void Aceptar(View view) {

        if (enviado.isChecked() && !no_seguimiento.getText().toString().isEmpty()) {
            recibido.setId_compra(no_seguimiento.getText().toString());
            databaseReference.child("Pedidos/" + recibido.getId()).setValue(recibido);
            ;
            databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Usuario u = snapshot.getValue(Usuario.class);
                        try {
                            if (u.getId().equals(recibido.getUsuario_id())) {
                                usuario.setText(u.getNombre() + " " + u.getApellidos());
                                crear_notificacion(u.getToken(), "Tu pedidos ha sido enviado", recibido.getNombre(), recibido.getFoto());
                            }
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            startActivity(new Intent(VerPedidoActivity.this, MainActivity.class));
            finish();
            Toast.makeText(this, "Notificación enviada al cliente", Toast.LENGTH_SHORT).show();
        } else if (enviado.isChecked() && no_seguimiento.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerPedidoActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Envío pendiente").setMessage("No se ha asignado el numero de seguimiento para el cliente");
            builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(VerPedidoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.create().show();
        } else if (pendiente.isChecked() && !no_seguimiento.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerPedidoActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Envío pendiente").setMessage("¿El pedido ha sido enviado?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(VerPedidoActivity.this, "Notificación enviada al cliente", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(VerPedidoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.create().show();
        } else {
            Toast.makeText(this, "Envío pendiente a entregar", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VerPedidoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manu_inventory, menu);
        return true;
    }
}
