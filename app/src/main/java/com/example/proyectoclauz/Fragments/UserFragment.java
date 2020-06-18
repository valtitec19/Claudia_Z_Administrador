package com.example.proyectoclauz.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.proyectoclauz.Activitys.AjustesActivity;
import com.example.proyectoclauz.Activitys.PedidosActivity;
import com.example.proyectoclauz.Activitys.RepartidoresActivity;
import com.example.proyectoclauz.Activitys.RutasActivity;
import com.example.proyectoclauz.Activitys.UtilidadesActivity;
import com.example.proyectoclauz.Clases.AdapterAdmin;
import com.example.proyectoclauz.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    private Button utiliddes,repartidores, Pedidos_a_asignar,ajustes,pedidos;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterAdmin adapterAdmin;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button rutas;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        firebaseON();
        utiliddes=(Button)view.findViewById(R.id.utilidades);
        repartidores=(Button)view.findViewById(R.id.repartidores);
        ajustes=(Button)view.findViewById(R.id.ajustes);
        rutas=(Button)view.findViewById(R.id.rutas);
        rutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RutasActivity.class));
            }
        });
        pedidos=(Button)view.findViewById(R.id.pedidos);
        utiliddes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UtilidadesActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        repartidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RepartidoresActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AjustesActivity.class));
            }
        });

        pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PedidosActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.manu_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void firebaseON(){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Repartidores");//Catalogo de los productos
    }
}
