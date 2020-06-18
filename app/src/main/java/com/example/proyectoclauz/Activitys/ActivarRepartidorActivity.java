package com.example.proyectoclauz.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectoclauz.Clases.Repartidor;
import com.example.proyectoclauz.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivarRepartidorActivity extends AppCompatActivity {

    private Repartidor r_recibido;
    private TextView txt_nombre,txt_correo,txt_telefono,txt_direccion,txt_horario;
    private RadioButton estado_on,estado_off;
    private CircleImageView circleImageView;
    private Button btn_confirmar;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ImageView frontal,trasera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);
        firebaseOn();
        Intent i = getIntent();
        r_recibido = (Repartidor) i.getSerializableExtra("repartidor");
        //Toast.makeText(this, String.valueOf(r_recibido.getCobertura().size()), Toast.LENGTH_SHORT).show();
        txt_nombre=(TextView)findViewById(R.id.repartidor_confirm);
        txt_correo=(TextView)findViewById(R.id.correo_confirm);
        txt_telefono=(TextView)findViewById(R.id.telefono_confirm);
        txt_direccion=(TextView)findViewById(R.id.direccion_confirm);
        txt_horario=(TextView)findViewById(R.id.horario_confirm);
        estado_on=(RadioButton)findViewById(R.id.activo_confirm);
        estado_off=(RadioButton)findViewById(R.id.inactivo_confirm);
        circleImageView=(CircleImageView)findViewById(R.id.foto_confirm);
        btn_confirmar=(Button)findViewById(R.id.boton_confirmar);
        frontal=(ImageView)findViewById(R.id.id_frontal);
        trasera=(ImageView)findViewById(R.id.id_trasera);
        cargar_repartidor();

        estado_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivarRepartidorActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Activacion de Repartidor").setMessage("Al confirmar "+r_recibido.getNombre()+" sera habilitado para realizar entregas\n ¿Desea continuar?");
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        estado_on.setChecked(true);
                        estado_off.setChecked(false);
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        estado_on.setChecked(false);
                        estado_off.setChecked(true);
                        Toast.makeText(ActivarRepartidorActivity.this, "No se realizo ningun cambio", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivarRepartidorActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.create().show();
            }
        });

        estado_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivarRepartidorActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Activacion de Repartidor").setMessage("Al confirmar "+r_recibido.getNombre()+" no podra realizar entregas\n ¿Desea continuar?");
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        estado_on.setChecked(false);
                        estado_off.setChecked(true);

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        estado_on.setChecked(true);
                        estado_off.setChecked(false);
                        Toast.makeText(ActivarRepartidorActivity.this, "No se realizo ningun cambio", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivarRepartidorActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.create().show();
            }
        });
    }

    public void cargar_repartidor(){
        txt_nombre.setText(r_recibido.getNombre());
        txt_correo.setText(r_recibido.getCorreo());
        txt_telefono.setText(r_recibido.getTelefono());
        if(r_recibido.getEstado()){
            estado_on.setChecked(true);
            estado_off.setChecked(false);
        }else{
            estado_on.setChecked(false);
            estado_off.setChecked(true);
        }
        txt_direccion.setText(r_recibido.getDireccion());
        txt_horario.setText(r_recibido.getHorario_inicio()+"-"+r_recibido.getHorario_fin());
        Glide.with(this).
                load(r_recibido.getImagenes().get(0))
                .centerCrop()
                .override(450, 450)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circleImageView);
        Glide.with(this).
                load(r_recibido.getImagenes().get(1))
                .centerCrop()
                .override(450, 450)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(frontal);
        Glide.with(this).
                load(r_recibido.getImagenes().get(2))
                .centerCrop()
                .override(450, 450)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(trasera);
    }

    public void AceptarConfirmar(View view) {
        if(estado_on.isChecked()){
            Boolean estado=true;
            r_recibido.setEstado(estado);
            databaseReference.child("Repartidores").child(r_recibido.getId()).setValue(r_recibido);
            startActivity(new Intent(this,MainActivity.class));
        }
        else {
            Boolean estado=false;
            r_recibido.setEstado(estado);
            databaseReference.child("Repartidores").child(r_recibido.getId()).setValue(r_recibido);
            startActivity(new Intent(this,MainActivity.class));
        }
        }

    public void firebaseOn() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }
}
