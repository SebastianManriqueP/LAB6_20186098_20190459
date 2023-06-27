package com.example.lab06_iot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab06_iot.ListaActivity;
import com.example.lab06_iot.Model.Actividades;
import com.example.lab06_iot.Model.ActualizarActivity;
import com.example.lab06_iot.R;

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
                context.startActivity(intent);
            });
        }
    }

}
