package com.example.lab06_iot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lab06_iot.ListaActivity;
import com.example.lab06_iot.Model.Actividades;
import com.example.lab06_iot.Model.ActualizarActivity;
import com.example.lab06_iot.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>{

    private List<Actividades> listaActividades;
    private Context context;


    public List<Actividades> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(List<Actividades> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_actividad,parent,false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadAdapter.ActividadViewHolder holder, int position) {
        Actividades act = listaActividades.get(position);
        holder.actividades = act;
        //modificar:
        TextView textViewTiutlo = holder.itemView.findViewById(R.id.textNombre);
        textViewTiutlo.setText(act.getTitulo());
        TextView textViewDescripcion = holder.itemView.findViewById(R.id.textDescripcion);
        textViewDescripcion.setText(act.getDescripcion());
        TextView textViewFecha = holder.itemView.findViewById(R.id.Fecha);
        textViewFecha.setText(act.getFecha());
        TextView textViewInicio = holder.itemView.findViewById(R.id.inicio);
        textViewInicio.setText(act.getInicio());
        TextView textViewFin = holder.itemView.findViewById(R.id.fin);
        textViewFin.setText(act.getFin());
        //Imagen:
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(act.getImagen());
        ImageView imageView = holder.itemView.findViewById(R.id.imageView3);
        Glide.with(context)
                .load(imageRef)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }

    public class ActividadViewHolder extends RecyclerView.ViewHolder {

        Actividades actividades;
        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            Button botonInfo = itemView.findViewById(R.id.buttonInfo);
            botonInfo.setOnClickListener(view -> {
                Intent intent = new Intent(context, ActualizarActivity.class);
                intent.putExtra("idActividad", actividades.getId());
                intent.putExtra("titulo",actividades.getTitulo());
                intent.putExtra("descripcion",actividades.getDescripcion());
                intent.putExtra("fecha",actividades.getFecha());
                intent.putExtra("horini",actividades.getInicio());
                intent.putExtra("horfin",actividades.getFin());
                context.startActivity(intent);
            });
            Button borrar = itemView.findViewById(R.id.buttonBorrar);
            borrar.setOnClickListener(view -> {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("actividades").document(actividades.getId()).delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Unable to delete!", Toast.LENGTH_SHORT).show();
                        Log.d("Firebase", "onComplete: Error Unable to delete : " + task.getException());
                    }
                });
                notifyDataSetChanged();
            });
        }
    }

}
