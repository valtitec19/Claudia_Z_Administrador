package com.clauzon.proyectoclauz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.proyectoclauz.Clases.AdapterInventory;
import com.clauzon.proyectoclauz.Clases.ImageAdapter;
import com.clauzon.proyectoclauz.Clases.Producto;
import com.clauzon.proyectoclauz.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private String url_foto = "";
    private String categoria,estado2;
    Boolean foto_cambiada = false;
    private RadioButton b_activo, b_inactivo;
    private ProgressDialog progressDialog;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private List<Producto> lista;
    private AdapterInventory adapterInventory;
    private ImageView foto_edit;
    private Button acptar, cancelar_edit;
    private Producto p, producto_update;
    private String nombre, name;
    private Producto p_recibido;
    private int pos;
    private Spinner spinner_categoria,spinner_estado2;
    private EditText nombre_p, descripcion, compra, venta, cantidad,edit_categotia;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<String> imagenes=new ArrayList<>();
    private ImageView eliminar;
    private float precio_compra,precio_venta;
    private ArrayList<String> categorias=new ArrayList<>();
    private ImageAdapter imageAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        firebaseOn();
        requestMultiplePermissions();
        eliminar=(ImageView)findViewById(R.id.eliminar_producto);
        nombre_p = (EditText) findViewById(R.id.nombre_del_producto_edit);
        descripcion=(EditText)findViewById(R.id.descripcion_del_producto_edit);
        compra = (EditText) findViewById(R.id.precio_compra_producto_edit);
        venta = (EditText) findViewById(R.id.precio_venta_producto_edit);
        cantidad = (EditText) findViewById(R.id.cantidad_item_edit);
        acptar = (Button) findViewById(R.id.acptar_edit);
        //foto_edit = (ImageView) findViewById(R.id.foto_edit);
        b_activo = (RadioButton) findViewById(R.id.radio_button_activo_edit);
        b_inactivo = (RadioButton) findViewById(R.id.radio_button_inactivo_edit);
        spinner_categoria=(Spinner)findViewById(R.id.spinner_edit_categoria);
        //edit_categotia=(EditText)findViewById(R.id.edit_product_categoria);
        spinner_estado2=(Spinner)findViewById(R.id.spinner_edit_estado2);
        progressDialog = new ProgressDialog(this);
        adapterInventory = new AdapterInventory();
        Bundle bundle = getIntent().getExtras();
        Intent i = getIntent();
        p_recibido = (Producto) i.getSerializableExtra("p_send");
        Log.e("Fotos recibidas", String.valueOf(p_recibido.getImagenes().size()) );
        cargar_producto(p_recibido);
        //Toast.makeText(this, p_recibido.getColor_producto(), Toast.LENGTH_SHORT).show();
        name = bundle.getString("name");
        //pos = bundle.getInt("pos");
        lista = new ArrayList<>();


        b_activo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.parseInt(cantidad.getText().toString()) > 0) {
                    b_activo.setChecked(true);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Cantidad no valida").setMessage("Debe existir al menos 1 un producto para habilitarlo en la tienda");
                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            b_activo.setChecked(false);
                            b_inactivo.setChecked(true);
                        }
                    });
                    builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.create().show();
                }
            }
        });

        b_inactivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 b_activo.setChecked(false);
                 b_inactivo.setChecked(true);

            }
        });


        acptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generar_producto_update();
                //Toast.makeText(EditActivity.this, producto_update.getId_producto(), Toast.LENGTH_SHORT).show();


            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("¿Desea eliminar este producto?");
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference().child("Catalogo Productos").child(p_recibido.getId_producto());
                        databaseReference.removeValue();

                        startActivity(new Intent(EditActivity.this, MainActivity.class));
                        finish();

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        imageAdapter=new ImageAdapter(this,imagenes,p_recibido);
        viewPager.setAdapter(imageAdapter);

    }


    public void cargar_producto(Producto p) {
        inicio_spinners();
//        for(int i=0;i<categorias.size();i++){
//            if(categorias.get(i).equals(p_recibido.getCategoria())){
//                spinner_categoria.setSelection(i);
//            }
//        }
        if (p.isEstado()) {
            b_activo.setChecked(true);
            b_inactivo.setChecked(false);
//            b_inactivo.setEnabled(false);
//            b_activo.setEnabled(true);
        } else {
            b_inactivo.setChecked(true);
            b_activo.setChecked(false);
//            b_inactivo.setEnabled(true);
//            b_activo.setEnabled(false);
        }
        nombre_p.setText(p.getNombre_producto());
        descripcion.setText(p.getDescripcion());
        precio_compra=p.getCompra_producto();
        precio_venta=p.getVenta_producto();
        compra.setText(String.valueOf(p.getCompra_producto()));
//        edit_categotia.setText(p.getCategoria());
        //compra.setText("$"+String.valueOf(p.getCompra_producto()));
        venta.setText(String.valueOf(p.getVenta_producto()));
        cantidad.setText(String.valueOf(p.getCantidad_producto()));


        if(p_recibido.getImagenes().size()>0){
            imagenes.addAll(p_recibido.getImagenes());
            Log.e("Funciona imagenes", String.valueOf(imagenes.size())  );
        }
//        Glide.with(this).
//                load(p.getFoto_producto())
//                .centerCrop()
//                .override(450, 450)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(foto_edit);


    }

    public void regresar() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void generar_producto_update() {
        inicio_spinners();
        String nombre = nombre_p.getText().toString();
        String descripcion = this.descripcion.getText().toString();
        String id = p_recibido.getId_producto();
        String foto = "";
        String cat=categoria;
        //Log.e("Nueva categoria", categoria );
        Boolean estado=p_recibido.isEstado();
        int cantidad = Integer.parseInt(this.cantidad.getText().toString());//Integer.parseInt(tx6.getText().toString());
        if (foto_cambiada == false) {
            foto = p_recibido.getFoto_producto();
        } else if (foto_cambiada == true) {
            foto = url_foto;
        }

        float compra = Float.parseFloat(this.compra.getText().toString());
        float venta = Float.parseFloat(this.venta.getText().toString());

        if (cantidad>0) {
            if(b_activo.isChecked()){
                estado=true;
                producto_update = new Producto(nombre, descripcion, id, foto, true, compra, venta, cantidad,estado2,categoria,imagenes);
                databaseReference.child("Catalogo Productos").child(p_recibido.getId_producto()).setValue(producto_update);
                regresar();
            }else{
                estado=false;
                producto_update = new Producto(nombre, descripcion, id, foto, false, compra, venta, cantidad,estado2,categoria,imagenes);
                databaseReference.child("Catalogo Productos").child(p_recibido.getId_producto()).setValue(producto_update);
                regresar();
            }
        }else if(cantidad==0&&b_activo.isChecked()){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Cantidad no valida").setMessage("Debe existir al menos 1 un producto para habilitarlo en la tienda");
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    b_activo.setChecked(false);
                    b_inactivo.setChecked(true);
                }
            });
            builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            builder.create().show();

        }else {
            estado=false;
            producto_update = new Producto(nombre, descripcion, id, foto, estado, compra, venta, cantidad,estado2,categoria,imagenes);
            databaseReference.child("Catalogo Productos").child(p_recibido.getId_producto()).setValue(producto_update);
            regresar();
        }
    }



    public void firebaseOn() {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public void Cancelar_Edit(View view) {
        regresar();
    }

    public void inicio_spinners(){
        spinner_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoria = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        DatabaseReference reference = database.getReference("Categorias");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String cat = dataSnapshot.getValue(String.class);
                categorias.add(cat);

                ArrayAdapter<String> categorias_adapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.simple_spinner_item, categorias);
                spinner_categoria.setAdapter(categorias_adapter);
                spinner_categoria.setSelection(categorias_adapter.getPosition(p_recibido.getCategoria()));
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


        ArrayAdapter<CharSequence> adapter_spinner_estado2 = ArrayAdapter.createFromResource(this, R.array.estado_producto, android.R.layout.simple_spinner_item);
        spinner_estado2.setAdapter(adapter_spinner_estado2);
        spinner_estado2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estado2 = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String c=p_recibido.getCategoria();
//        if (c != null) {
//            int spinnerPosition = adapter_spinner_categoria.getPosition(c);
//            spinner_categoria.setSelection(spinnerPosition);
//        }

        String e2=p_recibido.getEstado_producto();
        if (e2 != null) {
            int spinnerPosition = adapter_spinner_estado2.getPosition(e2);
            spinner_estado2.setSelection(spinnerPosition);
        }
    }

    ///////////////////////*************************//////////////////////////************

    public void edit_foto(View view) {
        showPictureDialog();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Cargar Imagen de producto");
        String[] pictureDialogItems = {
                "Subir foto desde galeria",
                "Tomar nueva foto"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                progressDialog.setTitle("Subiendo foto de producto");
                progressDialog.setMessage("Por favor espere");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Uri contentURI = data.getData();
                storageReference = firebaseStorage.getReference("IMAGENES CATALOGO PRODUCTOS");
                final StorageReference foto_subida = storageReference.child(contentURI.getLastPathSegment());
                foto_subida.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        foto_subida.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                foto_cambiada = true;
                                url_foto = uri.toString();
                                progressDialog.dismiss();
                                Glide.with(EditActivity.this).load(url_foto).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(foto_edit);
                                Toast.makeText(EditActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
//                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    imageButton.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            progressDialog.setTitle("Subiendo foto de producto");
            progressDialog.setMessage("Por favor espere");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            //imageButton.setImageBitmap(thumbnail);
            saveImage(thumbnail);

            Uri uri = getImageUri(this, thumbnail);
            storageReference = firebaseStorage.getReference("IMAGENES CATALOGO PRODUCTOS");
            final StorageReference foto_subida = storageReference.child(uri.getLastPathSegment());
            foto_subida.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    foto_subida.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            foto_cambiada = true;
                            url_foto = uri.toString();
                            progressDialog.dismiss();
                            Glide.with(EditActivity.this).load(url_foto).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(foto_edit);
                            Toast.makeText(EditActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });


            //Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
