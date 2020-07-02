package com.clauzon.proyectoclauz.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;

import com.clauzon.proyectoclauz.Clases.Estacion;
import com.clauzon.proyectoclauz.Clases.Metro;
import com.clauzon.proyectoclauz.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AjustesActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button metro;
    private Resources res;
    private ArrayList<Estacion> estaciones = new ArrayList<>();
    private Metro NM = new Metro();
    private Metro m;
    private int[] lineas={R.array.Linea1,R.array.Linea2,R.array.Linea3,R.array.Linea4,R.array.Linea5,R.array.Linea6,R.array.Linea7,R.array.Linea8,R.array.Linea9,R.array.Linea12,R.array.LineaA,R.array.LineaB};
    private String[] nombres={"Linea 1","Linea 2","Linea 3","Linea 4","Linea 5","Linea 6","Linea 7","Linea 8","Linea 9","Linea 12","Linea A","Linea B"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        res = getResources();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Metro");
//        metro=(Button)findViewById(R.id.metro);
        //llenar_linea("Linea1",R.array.Linea1);
//        metro.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                for(int i=0;i<lineas.length;i++){
//                    NM=llenar_linea(nombres[i],lineas[i]);
//                    databaseReference.child(nombres[i]).setValue(NM);
//                }
//            }
//        });

    }

    public Metro llenar_linea(String nombre, int array){
        String[] arreglo= res.getStringArray(array);
        estaciones.clear();
        for(int i=0;i<arreglo.length;i++){
            Estacion e=new Estacion();
            e.setNombre(arreglo[i]);
            e.setEstado(true);
            estaciones.add(e);
        }
        m=new Metro(estaciones,nombre);
        return m;
    }
}
