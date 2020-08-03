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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
;
import android.widget.EditText;
import android.widget.Spinner;

import com.clauzon.proyectoclauz.Clases.AdapterInventory;
//import com.example.proyectoclauz.Clases.HolderInventory;
import com.clauzon.proyectoclauz.Clases.Producto;
import com.clauzon.proyectoclauz.Activitys.EditActivity;
import com.clauzon.proyectoclauz.R;
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
public class InventoryFragment extends Fragment /*implements SearchView.OnQueryTextListener*/ {

    private SearchView searchView;
    private EditText busqueda;
    private Spinner spinner;
    private Toolbar toolbar;
    private List<Producto> lista=new ArrayList<>();
    String seleccion;
    private RecyclerView recyclerView;
    private AdapterInventory adapterInventory,adapterInventory2,adapterInventory3, adapter_temp;
    private Button button;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<String> imagenes=new ArrayList<>();
    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_inventory, container, false);

        String[] opciones = {"Todos los productos","Productos Activos","Productos Inactivos"};
        final ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(getContext(),R.layout.my_spinner,opciones);
        spinner= (Spinner)view.findViewById(R.id.spinner_inventory);
        spinner.setAdapter(adapter_spinner);
//        busqueda=(EditText)view.findViewById(R.id.busqueda_inventory);
//        busqueda.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                filter(editable.toString());
//            }
//        });
//        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seleccion=adapterView.getItemAtPosition(i).toString();
                if(seleccion.equals("Todos los productos")){
                    recyclerView.setAdapter(adapterInventory);
                }else if(seleccion.equals("Productos Activos")){
                    recyclerView.setAdapter(adapterInventory2);
                }else if(seleccion.equals("Productos Inactivos")){
                    recyclerView.setAdapter(adapterInventory3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Firebase

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Catalogo Productos");//Catalogo de los productos

        //Recycler Stuff

        recyclerView = (RecyclerView) view.findViewById(R.id.Recycler_Inventory);
        adapterInventory = new AdapterInventory(getActivity());
        adapterInventory2 = new AdapterInventory(getActivity());
        adapterInventory3 = new AdapterInventory(getActivity());
        adapter_temp= new AdapterInventory(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Producto p=adapterInventory.get_lista().get(recyclerView.getChildAdapterPosition(view));
                imagenes=adapterInventory.get_lista().get(recyclerView.getChildAdapterPosition(view)).getImagenes();
                //String name=p.getNombre_producto();
                Intent intent = new Intent(getActivity(), EditActivity.class);
                Producto p_send = generar_producto(p);
                Log.e("Id", p.getId_producto());
                intent.putExtra("name",adapterInventory.get_lista().get(recyclerView.getChildAdapterPosition(view)).getNombre_producto().toString());
                intent.putExtra("p_send",p_send);
                startActivity(intent);
            }
        });
        adapterInventory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Producto p=adapterInventory2.get_lista().get(recyclerView.getChildAdapterPosition(view));
                imagenes=adapterInventory3.get_lista().get(recyclerView.getChildAdapterPosition(view)).getImagenes();
                //String name=p.getNombre_producto();
                Intent intent = new Intent(getActivity(), EditActivity.class);
                Producto p_send = generar_producto(p);
                Log.e("Fotos enviadas", String.valueOf(imagenes.size()));
                intent.putExtra("name",adapterInventory2.get_lista().get(recyclerView.getChildAdapterPosition(view)).getNombre_producto().toString());
                intent.putExtra("p_send",p_send);
                startActivity(intent);
            }
        });
        adapterInventory3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Producto p=adapterInventory3.get_lista().get(recyclerView.getChildAdapterPosition(view));
                imagenes=adapterInventory3.get_lista().get(recyclerView.getChildAdapterPosition(view)).getImagenes();
                //String name=p.getNombre_producto();
                Intent intent = new Intent(getActivity(), EditActivity.class);
                Producto p_send = generar_producto(p);
                Log.e("Fotos enviadas", String.valueOf(imagenes.size()));
                intent.putExtra("name",adapterInventory3.get_lista().get(recyclerView.getChildAdapterPosition(view)).getNombre_producto().toString());
                intent.putExtra("p_send",p_send);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapterInventory);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Producto producto = dataSnapshot.getValue(Producto.class);
                adapterInventory.add_producto(producto);
                if(producto.isEstado()){
                    adapterInventory2.add_producto(producto);
                }else {
                    adapterInventory3.add_producto(producto);
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

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.manu_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void filter(String text) {
        ArrayList<Producto> filteredList = new ArrayList<>();
        List<Producto> lista= new ArrayList<>();
        lista=adapterInventory.getLista();
        for (Producto item : lista) {
            if (item.getNombre_producto().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
                adapter_temp.filterList(filteredList);
            }
        }
        adapterInventory.filterList(filteredList);
    }


    public Producto generar_producto(Producto producto){
        String nombre = producto.getNombre_producto();
        String descripcion=producto.getDescripcion();
        String estado_producto=producto.getEstado_producto();
        //String categoria=producto.getCategoria();
        String id = producto.getId_producto();
        String categoria=producto.getCategoria();
        String foto=producto.getFoto_producto();
        Boolean estado=producto.isEstado();
        imagenes=producto.getImagenes();
        ArrayList<String> array_colres=producto.getColores();
        ArrayList<String> array_tamaños=producto.getTamanos();
        ArrayList<String> array_modelos=producto.getModelos();
        float compra = producto.getCompra_producto();
        float venta = producto.getVenta_producto();
        float oferta = producto.getOferta();
        int cantidad = producto.getCantidad_producto();
        return producto = new Producto(nombre, descripcion, id, foto, estado, compra, venta,oferta, cantidad,estado_producto,categoria,imagenes,array_colres,array_tamaños,array_modelos);
    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Add your menu entries here
//        inflater.inflate(R.menu.manu_toolbar, menu) ;
//        MenuItem menuItem=menu.findItem(R.id.search_menu);
//        SearchView searchView=(SearchView)menuItem.getActionView();
//        searchView.setOnQueryTextListener(this);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    ////////*****************METODOS SEARCHVIEW////////////////////////*************************
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//
//        String user_input=newText.toLowerCase();
//        List<Producto> nuevos_productos=new ArrayList<>();
//        List<Producto> nuevos_productos_enviar=new ArrayList<>();
//        List<Producto> todos_los_productos=new ArrayList<>();
//        ArrayList<String> nombres=new ArrayList<>();
//        for(int i=0; i<adapterInventory.getItemCount();i++){
//            nombres.add(adapterInventory.nombre_p(i));
//            todos_los_productos.add(adapterInventory.cual_producto(nombres.get(i)));
//        }
//        for(String name : nombres){
//            if(name.toLowerCase().contains(user_input)){
//                nuevos_productos_enviar.add(adapterInventory.cual_producto(name));
//                nuevos_productos.add(adapterInventory.cual_producto(name));
//
//
//            }
//        }
//        adapterInventory.filterList(nuevos_productos,todos_los_productos,nuevos_productos_enviar);
//        return true;
//    }

}
