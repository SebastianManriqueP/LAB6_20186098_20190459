package com.example.lab06_iot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;


import com.example.lab06_iot.Model.Actividades;
import com.example.lab06_iot.databinding.ActivityCrearBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearActivity extends AppCompatActivity {

    ActivityCrearBinding binding;
    FirebaseFirestore db;

    Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean comprobarImagen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        //AL darle al boton de subir imagen
        binding.buttonSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lanzar una actividad para seleccionar una imagen
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        //Al darle al boton crear
        binding.buttonCrear.setOnClickListener(view -> {
            //Validaciones
            if(TextUtils.isEmpty(binding.editTextActividad.getText().toString().trim())){
                Toast.makeText(this, "Ingresar nombre", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.editTextDescripcion.getText().toString())) {
                Toast.makeText(this, "Ingresar descripcion", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(binding.editTextFecha.getText().toString())) {
                Toast.makeText(this, "Ingresar fecha", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(binding.editTextInicio.getText().toString())) {
                Toast.makeText(this, "Ingresar hora de inicio", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(binding.editTextFinal.getText().toString())) {
                Toast.makeText(this, "Ingresar hora de fin", Toast.LENGTH_SHORT).show();
            }else if(comprobarImagen == false){
                Toast.makeText(this, "Ingresar una imagen", Toast.LENGTH_SHORT).show();
            }else {
                //obtener datos
                String nombre = binding.editTextActividad.getText().toString();
                String descripcion = binding.editTextDescripcion.getText().toString();
                String fecha = binding.editTextFecha.getText().toString();
                String inicio = binding.editTextInicio.getText().toString();
                String fin = binding.editTextFinal.getText().toString();
                Actividades act = new Actividades(nombre, descripcion, fecha, inicio, fin, "img" + nombre + ".jpg");
                //enviar imagen
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference imageRef = storageRef.child("img" + nombre + ".jpg");
                imageRef.putFile(imageUri);
                //enviar datos
                Bundle parametros = this.getIntent().getExtras();
                String usuario = parametros.getString("Usuario");
                db.collection("usuarios")
                        .document(usuario)
                        .collection("actividades")
                        .document()
                        .set(act).addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Creado", Toast.LENGTH_SHORT).show();
                        });
                Intent intent = new Intent(this, ListaActivity.class);
                intent.putExtra("Usuario", usuario);
                finishAffinity();
                startActivity(intent);
            }
        });
        //Boton regresar
        binding.imageView2.setOnClickListener(view -> {
            finish();
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Obtener la URI de la imagen seleccionada
            imageUri = data.getData();
            // Establecer la imagen en el ImageView utilizando View Binding
            binding.imageView4.setVisibility(View.VISIBLE);
            binding.imageView4.setImageURI(imageUri);
            comprobarImagen = true;
        }
    }

}