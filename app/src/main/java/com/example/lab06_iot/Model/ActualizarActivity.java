package com.example.lab06_iot.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.lab06_iot.DatePickerFragment;
import com.example.lab06_iot.R;
import com.example.lab06_iot.TimePickerFragment;
import com.google.protobuf.StringValue;

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

        hori.setOnClickListener(view -> {
            mostrarTimeDialog();
        });
        horf.setOnClickListener(view -> {
            mostrarTimeDialog2();
        });

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(view -> {
            String nuevoNombre = name.getText().toString();
            String nuevaDescripcion = name.getText().toString();


        });


    }
    public void respuestaDateDialog(int year, int month, int day ){

        EditText t = findViewById(R.id.editTextFecha);
        t.setText(String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year));

    }
    public void mostrarDateDialog(){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(),"datepicker");

    }
    public void respuestaTimeDialog(int hour, int minute){
        EditText t = findViewById(R.id.editTextInicio);
        t.setText(String.valueOf(hour)+":"+String.valueOf(minute));
    }
    public void respuestaTimeDialog2(int hour,int minute){
        EditText t = findViewById(R.id.editTextFinal);
        t.setText(String.valueOf(hour)+":"+String.valueOf(minute));
    }
    public void mostrarTimeDialog(){
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(),"timepicker");
    }
    public void mostrarTimeDialog2(){
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(),"timepicker2");
    }
}