package com.example.lab06_iot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lab06_iot.Adapter.ActividadAdapter;
import com.example.lab06_iot.Model.Actividades;
import com.example.lab06_iot.databinding.ActivityListaBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    ActivityListaBinding binding;
    FirebaseFirestore db;

    ActividadAdapter actividadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        Bundle parametros = this.getIntent().getExtras();
        List<Actividades> actividadesList = new ArrayList<>();
        String usuario = parametros.getString("Usuario");
        db.collection("usuarios")
                .document(usuario)
                .collection("actividades")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Actividades actividad = document.toObject(Actividades.class);
                            actividad.setId(document.getId());
                            actividadesList.add(actividad);
                        }
                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        //Listar Actividades:
                        //enviar al adapter
                        actividadAdapter = new ActividadAdapter();
                        actividadAdapter.setUser(usuario);
                        actividadAdapter.setListaActividades(actividadesList);
                        actividadAdapter.setContext(ListaActivity.this);
                        binding.recyclerView.setAdapter(actividadAdapter);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ListaActivity.this));
                    }

                });

        //Ir a agregar Actividad
        binding.imageMas.setOnClickListener(view -> {
            Intent intent = new Intent(this, CrearActivity.class);
            intent.putExtra("Usuario", usuario);
            startActivity(intent);
        });
    }


    }
