package com.example.lab06_iot.Model;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lab06_iot.DatePickerFragment;
import com.example.lab06_iot.ListaActivity;
import com.example.lab06_iot.R;
import com.example.lab06_iot.TimePickerFragment;
import com.example.lab06_iot.TimePickerFragment2;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;

import java.time.LocalTime;

public class ActualizarActivity extends AppCompatActivity {

    Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);
        Intent intent = getIntent();
        String usuario = intent.getStringExtra("usuario");
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

        Button buttonact = findViewById(R.id.buttonActualizar);

        buttonact.setOnClickListener(view -> {
            Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
            intent2.setType("image/*");
            startActivityForResult(intent2, PICK_IMAGE_REQUEST);
        });

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(view -> {
            String nuevoNombre = name.getText().toString();
            String nuevaDescripcion = descrip.getText().toString();
            String nuevaFecha = date.getText().toString();
            String nuevaHori = hori.getText().toString();
            String nuevaHorf = horf.getText().toString();

            //Verificar tiempo de inicio menor a final
            String[] inicioList = nuevaHori.split(":");
            String[] finalList = nuevaHorf.split(":");
            LocalTime tiempoInicio = LocalTime.of(Integer.parseInt(inicioList[0]), Integer.parseInt(inicioList[1]));
            LocalTime tiempoFinal = LocalTime.of(Integer.parseInt(finalList[0]), Integer.parseInt(finalList[1]));
            int comparacion = tiempoInicio.compareTo(tiempoFinal);
            if (comparacion < 0) {
                Actividades actualizadaActividad = new Actividades();
                actualizadaActividad.setId(id);
                actualizadaActividad.setDescripcion(nuevaDescripcion);
                actualizadaActividad.setTitulo(nuevoNombre);
                actualizadaActividad.setFecha(nuevaFecha);
                actualizadaActividad.setInicio(nuevaHori);
                actualizadaActividad.setFin(nuevaHorf);
                actualizadaActividad.setImagen("img" + nuevoNombre + ".jpg");
                UploadTask uploadTask = null;
                //enviar imagen
                if(imageUri != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference imageRef = storageRef.child("img" + nuevoNombre + ".jpg");
                    uploadTask = imageRef.putFile(imageUri);
                }
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("usuarios")
                        .document(usuario)
                        .collection("actividades")
                        .document(id)
                        .set(actualizadaActividad).addOnSuccessListener(unused -> {
                            Toast.makeText(this, "ACTUALIZADO", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> e.printStackTrace());
                Intent intentN = new Intent(this, ListaActivity.class);
                intentN.putExtra("Usuario", usuario);
                if(imageUri != null) {
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        finishAffinity();
                        startActivity(intentN);
                    });
                }else{
                    finishAffinity();
                    startActivity(intentN);
                }
            }else{
                Toast.makeText(this, "Tiempo de inicio debe ser menor al final", Toast.LENGTH_SHORT).show();
            }
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
        TimePickerFragment2 timePickerFragment2 = new TimePickerFragment2();
        timePickerFragment2.show(getSupportFragmentManager(),"timepicker2");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Obtener la URI de la imagen seleccionada
            imageUri = data.getData();
            // Establecer la imagen en el ImageView utilizando View Binding
            ImageView imageView = findViewById(R.id.imageView5);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(imageUri);
        }
    }
}