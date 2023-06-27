package com.example.lab06_iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.lab06_iot.Model.Usuario;
import com.example.lab06_iot.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        //Registro
        binding.textRegistro.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistroActivity.class);
            startActivity(intent);
        });
        //Iniciar sesion
        binding.buttonIngresar.setOnClickListener(view -> {
            //Validaciones de campos
            if(TextUtils.isEmpty(binding.editTextTextPersonName.getText().toString().trim())){
                Toast.makeText(this, "Ingresar Correo", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.editTextTextPersonName.getText().toString())) {
                Toast.makeText(this, "Ingresar Contraseña", Toast.LENGTH_SHORT).show();
            }else{
                //Ingresar a la APP
                db.collection("usuarios")
                                .document(binding.editTextTextPersonName.getText().toString())
                                .get()
                                .addOnCompleteListener(task ->{
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Usuario usuario = document.toObject(Usuario.class);
                                            if(usuario.getContrasenha().equals(binding.editTextTextPassword.getText().toString())){
                                                Intent intent = new Intent(this, ListaActivity.class);
                                                intent.putExtra("Usuario", usuario.getCorreo());
                                                startActivity(intent);
                                            }else {
                                                Toast.makeText(this, "Error en correo o contraseña", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(this, "No existe user", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.d("msg-test", "get failed with ", task.getException());
                                    }

                                });

            }
        });
    }
}