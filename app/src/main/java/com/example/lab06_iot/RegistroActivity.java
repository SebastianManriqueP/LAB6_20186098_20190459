package com.example.lab06_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.lab06_iot.Model.Usuario;
import com.example.lab06_iot.databinding.ActivityRegistroBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity {

    FirebaseFirestore db;
    ActivityRegistroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        //boton de Regreso
        binding.imageView2.setOnClickListener(view -> {
            finish();
        });
        //Validacion de form
        binding.buttonRegistrar.setOnClickListener(view -> {
            //Validación de todos los campos:
            if( TextUtils.isEmpty(binding.editNombre.getText().toString().trim()) ){
                Toast.makeText(this, "Ingresar Nombre", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.editTextCorreo.getText().toString().trim())) {
                Toast.makeText(this, "Ingresar Correo", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.editTextTextPassword.getText().toString().trim())) {
                Toast.makeText(this, "Ingresar contraseña", Toast.LENGTH_SHORT).show();
            }else{
                //Agregar el usuario
                Usuario usuario = new Usuario();
                usuario.setNombre(binding.editNombre.getText().toString());
                usuario.setCorreo(binding.editTextCorreo.getText().toString());
                usuario.setContrasenha(binding.editTextTextPassword.getText().toString());
                db.collection("usuarios")
                        .document(binding.editTextCorreo.getText().toString())
                        .set(usuario)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Usuario Creado", Toast.LENGTH_SHORT).show();
                        });
                finish();
            }
        });




    }
}