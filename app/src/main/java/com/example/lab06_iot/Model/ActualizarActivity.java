package com.example.lab06_iot.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab06_iot.DatePickerFragment;
import com.example.lab06_iot.R;

public class ActualizarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);
        Intent intent = getIntent();
        String id = intent.getStringExtra("idActividad");
        String nombre = intent.getStringExtra("titulo");
        String descripcion = intent.getStringExtra("descripcion");
        String fecha = intent.getStringExtra("fecha");
        String horini = intent.getStringExtra("horini");
        String horfin = intent.getStringExtra("horfin");
        EditText name = findViewById(R.id.editTextActividad);
        EditText descrip = findViewById(R.id.editTextDescripcion);
        EditText date = findViewById(R.id.editTextFecha);
        EditText hori = findViewById(R.id.editTextInicio);
        EditText horf = findViewById(R.id.editTextFinal);

        name.setText(nombre);
        descrip.setText(descripcion);
        date.setText(fecha);
        hori.setText(horini);
        horf.setText(horfin);

        date.setOnClickListener(view -> {
            mostrarDateDialog();
        });

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(view -> {
            String nuevoNombre = name.getText().toString();
            String nuevaDescripcion = name.getText().toString();


        });

    }
    public void respuestaDateDialog(int year, int month, int day ){
        Log.d("msg-test","year selected: " + year);
        Log.d("msg-test","month selected: " + month);
        Log.d("msg-test","day selected: " + day);
    }
    public void mostrarDateDialog(){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(),"datepicker");
    }

}