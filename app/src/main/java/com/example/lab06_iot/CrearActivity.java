package com.example.lab06_iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;


import com.example.lab06_iot.Model.Actividades;
import com.example.lab06_iot.databinding.ActivityCrearBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class CrearActivity extends AppCompatActivity {

    ActivityCrearBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        Actividades act = new Actividades("Correr2","Co","18/11/2025","8:30 pm","9:30 pm","a");

        binding.buttonCrear.setOnClickListener(view -> {
            db.collection("usuarios")
                    .document("a20186098")
                    .collection("actividades")
                    .document("HU5YEC9EvaTufcTIBPx7")
                    .set(act).addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Creado", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}