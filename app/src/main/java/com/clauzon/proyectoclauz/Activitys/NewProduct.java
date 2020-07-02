package com.clauzon.proyectoclauz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.proyectoclauz.Clases.AdapterInventory;
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
import java.util.UUID;

public class NewProduct extends AppCompatActivity {

    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    ////////////////***********************************/////////////////************
    private Intent CropIntent;
    private String url_foto = "";
    private Uri contentURI;
    private boolean val1 = false, val2 = false;
    private Button btn1, btn2;
    String value, estado_producto, categoria;
    boolean estado = false;
    Producto producto;
    private RadioButton b_activo, b_inactivo;
    private EditText tx1, tx2, tx3, tx4, tx5, tx6,edit_categoria;
    private Spinner spinner, spinner_categoria;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private AdapterInventory adapterInventory;
    private ImageButton imageButton;
    private ProgressDialog progressDialog;
    private static final int SEND_PHOTO = 1;
    private ArrayList<String> imagenes=new ArrayList<>();
    private ArrayList<CharSequence> categorias=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        requestMultiplePermissions();
        btn1 = (Button) findViewById(R.id.add_producto);
        btn2 = (Button) findViewById(R.id.cancelar_producto);
        tx1 = (EditText) findViewById(R.id.nombre_del_producto_nuevo);
        tx2 = (EditText) findViewById(R.id.descripcion_producto);
        tx4 = (EditText) findViewById(R.id.precio_compra_producto_nuevo);
        tx5 = (EditText) findViewById(R.id.precio_venta_producto_nuevo);
        tx6 = (EditText) findViewById(R.id.cantidad_nuevo_item);
        edit_categoria=(EditText)findViewById(R.id.new_product_categoria);
        b_activo = (RadioButton) findViewById(R.id.radio_button_activo);
        b_inactivo = (RadioButton) findViewById(R.id.radio_button_inactivo);
        imageButton = (ImageButton) findViewById(R.id.imagen_nuevo_producto);
        progressDialog = new ProgressDialog(this);
        //adapterInventory = new AdapterInventory(this);
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = database.getReference();//Catalogo de los productos
        inicio_spinners();

        DatabaseReference reference=database.getReference("Categorias");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String cat=dataSnapshot.getValue(String.class);
                categorias.add(cat);
                Log.e("CATEGORIAS ", cat );
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
        //Toast.makeText(this, String.valueOf(categorias.size()), Toast.LENGTH_SHORT).show();
    }

    //Funciones
    public void Aceptar_Nuevo_Producto(View view) {
        validacion_producto();
    }

    public void Cancelar_Nuevo_Producto(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Subir_foto(View view) {
        for(int i=0;i<4;i++){
            showPictureDialog();
        }
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/jpeg");
//        intent.putExtra(intent.EXTRA_LOCAL_ONLY, true);
//        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), SEND_PHOTO);
    }

    public void validacion_producto() {
        if (tx1.getText().toString().isEmpty() || tx2.getText().toString().isEmpty() || tx5.getText().toString().isEmpty()
                || tx4.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Datos incompletos").setMessage("Por favor rellene todos los campos");
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Mensaje("No se creo el producto");
                    Intent intent = new Intent(NewProduct.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            builder.create().show();


        } else {

            val1 = true;
        }
        if (url_foto.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Datos incompletos").setMessage("Por favor cargue una foto del producto");
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Mensaje("No se creo el producto");
                    Intent intent = new Intent(NewProduct.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            builder.create().show();
        } else {
            val2 = true;
        }
        if (val1 && val2) {
            ValidacionCorrecta();
        }
    }

    public Producto generar_producto() {
        inicio_spinners();
        String nombre = tx1.getText().toString();
        String descripcion = tx2.getText().toString();
        String cat=edit_categoria.getText().toString();
        value = UUID.randomUUID().toString();
        String id = value;
        String foto = imagenes.get(0);
        if (b_inactivo.isChecked() == true) {
            estado = false;
        } else {
            estado = true;
        }
        float compra = Float.parseFloat(tx4.getText().toString());
        float venta = Float.parseFloat(tx5.getText().toString());
        int cantidad = Integer.parseInt(tx6.getText().toString());//Integer.parseInt(tx6.getText().toString());
        producto = new Producto(nombre, descripcion, id, foto, estado, compra, venta, cantidad, estado_producto,cat,imagenes);
        return producto;
    }


    public void Mensaje(String mensaje) {
        Toast.makeText(NewProduct.this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void ValidacionCorrecta() {
        Producto p = generar_producto();
        databaseReference.child("Catalogo Productos").child(value).setValue(p);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Producto creado exitosamente", Toast.LENGTH_SHORT).show();
    }

    public void inicio_spinners() {
        ArrayAdapter<CharSequence> adapter_spinner = ArrayAdapter.createFromResource(this, R.array.estado_producto, android.R.layout.simple_spinner_item);
        spinner = (Spinner) findViewById(R.id.spinner_new_product);
        spinner.setAdapter(adapter_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                estado_producto = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        ArrayAdapter<CharSequence> adapter_spinner_categoria = ArrayAdapter.createFromResource(this, R.array.Categoria, android.R.layout.simple_spinner_item);
//        spinner_categoria = (Spinner) findViewById(R.id.spinner_categoria_new_product);
//        spinner_categoria.setAdapter(adapter_spinner_categoria);
//        spinner_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                categoria = adapterView.getItemAtPosition(i).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        String c=producto.getCategoria();
//        if (c != null) {
//            int spinnerPosition = adapter_spinner_categoria.getPosition(c);
//            spinner_categoria.setSelection(spinnerPosition);
//        }

//        String e2=producto.getEstado_producto();
//        if (e2 != null) {
//            int spinnerPosition = adapter_spinner_estado2.getPosition(e2);
//            spinner_estado2.setSelection(spinnerPosition);
//        }
    }

    //***********///////////***************//////////////////**********************////////////

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Cargar Imagen de producto");
        pictureDialog.setCancelable(false);
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
                contentURI = data.getData();
                //CropImage();
                progressDialog.setTitle("Subiendo foto de producto");
                progressDialog.setMessage("Por favor espere");
                progressDialog.setCancelable(false);
                progressDialog.show();
                storageReference = firebaseStorage.getReference("IMAGENES CATALOGO PRODUCTOS");
                final StorageReference foto_subida = storageReference.child(contentURI.getLastPathSegment());
                foto_subida.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        foto_subida.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                url_foto = uri.toString();
                                imagenes.add(url_foto);
                                progressDialog.dismiss();
                                Glide.with(NewProduct.this).load(imagenes.get(0)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageButton);
                                Toast.makeText(NewProduct.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();

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

                            url_foto = uri.toString();
                            imagenes.add(url_foto);
                            progressDialog.dismiss();
                            Glide.with(NewProduct.this).load(imagenes.get(0)).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageButton);
                            Toast.makeText(NewProduct.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });


            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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

    //////////////////////***************************************/////////////////////
    private void CropImage() {

        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(contentURI, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);
        } catch (ActivityNotFoundException ex) {

        }

    }

}
