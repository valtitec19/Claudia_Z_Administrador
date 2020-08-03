package com.clauzon.proyectoclauz.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.clauzon.proyectoclauz.Activitys.NotifiaccionActivity;
import com.clauzon.proyectoclauz.Activitys.RutasActivity;
import com.clauzon.proyectoclauz.Activitys.VerPedidoActivity;
import com.clauzon.proyectoclauz.Clases.AdapterHome;
import com.clauzon.proyectoclauz.Clases.Pedidos;
import com.clauzon.proyectoclauz.Clases.Producto;
import com.clauzon.proyectoclauz.Activitys.NewProduct;
import com.clauzon.proyectoclauz.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Button button;
    private FloatingActionButton fab1, fab2,fab3;
    private SearchView searchView;
    private List<Producto> lista;
    private Spinner spinner;
    private Toolbar toolbar;
    String seleccion;
    private RecyclerView recyclerView;
    private AdapterHome adapterHome;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Floating Action Button
        firebaseON();
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        fab1 = (FloatingActionButton) view.findViewById(R.id.action1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.action2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.action3);
        lista= new ArrayList<>();
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewProduct.class);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RutasActivity.class));

            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotifiaccionActivity.class));
            }
        });
        //RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.Recycler_Home);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterHome = new AdapterHome(getActivity());
        recyclerView.setAdapter(adapterHome);

        //Firebase
        adapterHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pedidos pedidos=adapterHome.ger_lista().get(recyclerView.getChildAdapterPosition(view));
                Intent intent=new Intent(getActivity(), VerPedidoActivity.class);
                intent.putExtra("pedido",pedidos);
                startActivity(intent);

            }
        });


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pedidos pedidos= dataSnapshot.getValue(Pedidos.class);
                if (pedidos.getEstado().equals("Pago Realizado") || pedidos.getEstado().equals("Pago pendiente (En efectivo)")) {
                    if(pedidos.getCosto_envio()>0){
                        if(pedidos.getId_compra()==null || pedidos.getId_compra().equals("")){
                            adapterHome.add_producto(pedidos);
                        }
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
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                lista.clear();
//                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    databaseReference.child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Producto producto = dataSnapshot1.getValue(Producto.class);
//                            lista.add(producto);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        return view;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Add your menu entries here
//        inflater.inflate(R.menu.menu_home, menu);
//        MenuItem menuItem = menu.findItem(R.id.icono_logo);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
////        if (id == R.id.fav) {
////            startActivity(new Intent(getActivity(), FavActivity.class));
////
////        }
////        if (id == R.id.car) {
////            verifica_pedidos();
////        }
//        return super.onOptionsItemSelected(item);
//    }


    public void firebaseON(){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Pedidos");//Catalogo de los productos
    }

}
