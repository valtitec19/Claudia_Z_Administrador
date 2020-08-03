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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.clauzon.proyectoclauz.Clases.Repartidor;
import com.clauzon.proyectoclauz.Clases.Usuario;
import com.clauzon.proyectoclauz.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifiaccionActivity extends AppCompatActivity {
    private EditText titulo, mensaje;
    private ImageView imageView;
    private Button enviar, cancelar;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private ProgressDialog progressDialog;
    private static final int SEND_PHOTO = 1;
    private String url_foto = "";
    private Intent CropIntent;
    private Uri contentURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiaccion);
        inicio_vies();
    }

    private void inicio_vies() {
        titulo = (EditText) findViewById(R.id.titulo_notificacion);
        mensaje = (EditText) findViewById(R.id.mensaje_notificacion);
        imageView = (ImageView) findViewById(R.id.imageview_notificacion);
        enviar = (Button) findViewById(R.id.enviar_notificacion);
        cancelar = (Button) findViewById(R.id.cancelar_notififcacion);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Usuarios");
        firebaseStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    public void enviar_notificacion(View view) {
        if (!titulo.getText().toString().isEmpty()) {
            if (!mensaje.getText().toString().isEmpty()) {
                if(!url_foto.isEmpty()){
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            try {
                                crear_notificacion(usuario.getToken(), titulo.getText().toString(), mensaje.getText().toString(), url_foto);
                            } catch (Exception e) {

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
                    Toast.makeText(NotifiaccionActivity.this, "Notificación enviada", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NotifiaccionActivity.this,MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(this, "Debes seleccionar una imagen dando click sobre la camara", Toast.LENGTH_SHORT).show();
                }

            } else {
                mensaje.setError("Campo necesario");
            }

        } else {
            titulo.setError("Compo necesario");
        }
    }


    public void cancelar_notificacion(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void subir_imagenen_notificacion(View view) {
        showPictureDialog();
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


    ///////////////////////////////////////////////////////

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Cargar Imagen");
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
                progressDialog.setTitle("Subiendo imagen");
                progressDialog.setMessage("Por favor espere");
                progressDialog.setCancelable(false);
                progressDialog.show();
                storageReference = firebaseStorage.getReference("IMAGENES NOTIFIACIÓN");
                final StorageReference foto_subida = storageReference.child(contentURI.getLastPathSegment());
                foto_subida.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        foto_subida.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                url_foto = uri.toString();

                                progressDialog.dismiss();
                                Glide.with(NotifiaccionActivity.this).load(url_foto).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                                Toast.makeText(NotifiaccionActivity.this, "Foto subidda con exito", Toast.LENGTH_SHORT).show();

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
            progressDialog.setTitle("Subiendo imagen");
            progressDialog.setMessage("Por favor espere");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);
            Uri uri = getImageUri(this, thumbnail);
            storageReference = firebaseStorage.getReference("IMAGENES NOTIFICACIÓN");
            final StorageReference foto_subida = storageReference.child(uri.getLastPathSegment());
            foto_subida.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    foto_subida.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            url_foto = uri.toString();
                            progressDialog.dismiss();
                            Glide.with(NotifiaccionActivity.this).load(url_foto).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                            Toast.makeText(NotifiaccionActivity.this, "Imagen subidda con exito", Toast.LENGTH_SHORT).show();

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


}