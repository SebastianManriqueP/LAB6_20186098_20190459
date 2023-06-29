package com.example.lab06_iot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.time.LocalDate;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lab06_iot.Adapter.ActividadAdapter;
import com.example.lab06_iot.Model.Actividades;
import com.example.lab06_iot.databinding.ActivityListaBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    ActivityListaBinding binding;
    FirebaseFirestore db;

    ActividadAdapter actividadAdapter;

    //variables para filtro
    Calendar calendar = Calendar.getInstance();
    int ahno = calendar.get(Calendar.YEAR);
    int mes = calendar.get(Calendar.MONTH);
    int dia = calendar.get(Calendar.DAY_OF_MONTH);
    int hora = calendar.get(Calendar.HOUR_OF_DAY);
    int minutos = calendar.get(Calendar.MINUTE);
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
                        if(actividadesList.isEmpty()){
                            //si no hay actividades
                            binding.textNO.setVisibility(View.VISIBLE);
                        }else {
                            //enviar al adapter:
                            actividadAdapter = new ActividadAdapter();
                            actividadAdapter.setUser(usuario);
                            actividadAdapter.setListaActividades(actividadesList);
                            actividadAdapter.setContext(ListaActivity.this);

                            binding.recyclerView.setAdapter(actividadAdapter);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(ListaActivity.this));
                        }
                    }

                });

        //Ir a agregar Actividad
        binding.imageMas.setOnClickListener(view -> {
            Intent intent = new Intent(this, CrearActivity.class);
            intent.putExtra("Usuario", usuario);
            startActivity(intent);
        });

        //FILTRO:
        //Fecha INICIO
        binding.editTextInFecha.setOnClickListener(view -> {
            mostrarDateDialogIn();
        });
        //Hora INICIO
        binding.editTextInHora.setOnClickListener(view -> {
            mostrarTimeDialogIni();
        });
        //Fecha FIN
        binding.editTextFinFecha.setOnClickListener(view -> {
            mostrarDateDialogFin();
        });
        //Hora FIN
        binding.editTextFinHora.setOnClickListener(view -> {
            mostrarTimeDialogFin();
        });
        //BOTON APLICAR:
        binding.button3.setOnClickListener(view -> {
            //Validaciones
            //Hora:
            String horaInicio = binding.editTextInHora.getText().toString();
            String horaFin = binding.editTextFinHora.getText().toString();
            String[] horaIn = horaInicio.split(":");
            String[] horaF = horaFin.split(":");
            //fechas:
            String fechaInicio = binding.editTextInFecha.getText().toString();
            String fechaFin = binding.editTextFinFecha.getText().toString();
            String[] fechaIn = fechaInicio.split("/");
            String[] fechaF = fechaFin.split("/");
            if (Integer.parseInt(fechaF[2])<Integer.parseInt(fechaIn[2])) {
                fechaMayor();
            } else if (Integer.parseInt(fechaF[1])<Integer.parseInt(fechaIn[1]) && Integer.parseInt(fechaF[2])<=Integer.parseInt(fechaIn[2]) ) {
                fechaMayor();
            } else if (Integer.parseInt(fechaF[0])<Integer.parseInt(fechaIn[0]) && Integer.parseInt(fechaF[1])<Integer.parseInt(fechaIn[1]) && Integer.parseInt(fechaF[2])<=Integer.parseInt(fechaIn[2]) ) {
                fechaMayor();
            } else if (Integer.parseInt(fechaF[0])<Integer.parseInt(fechaIn[0]) && Integer.parseInt(fechaF[1])==Integer.parseInt(fechaIn[1]) && Integer.parseInt(fechaF[2])<=Integer.parseInt(fechaIn[2]) ) {
                fechaMayor();
            } else if (Integer.parseInt(fechaF[0])==Integer.parseInt(fechaIn[0]) && Integer.parseInt(fechaF[1])==Integer.parseInt(fechaIn[1]) && Integer.parseInt(fechaF[2])==Integer.parseInt(fechaIn[2])) {
                if(Integer.parseInt(horaF[0])<Integer.parseInt(horaIn[0])){
                    HoraMayor();
                } else if (Integer.parseInt(horaF[0])==Integer.parseInt(horaIn[0]) &&  Integer.parseInt(horaF[1])<=Integer.parseInt(horaIn[1])) {
                    HoraMayor();
                } else {
                    List<Actividades> actividadesListFiltrado = new ArrayList<>();
                    for (Actividades actividades : actividadesList) {
                        String[] actividadesFecha = actividades.getFecha().split("/");
                        String[] actividadesHoraInicio = actividades.getInicio().split(":");
                        String[] actividadesHoraFin = actividades.getFin().split(":");

                        LocalDate fecha1 = LocalDate.of(Integer.parseInt(actividadesFecha[2]), Integer.parseInt(actividadesFecha[1]), Integer.parseInt(actividadesFecha[0]));
                        LocalDate fecha2 = LocalDate.of(Integer.parseInt(fechaIn[2]), Integer.parseInt(fechaIn[1]), Integer.parseInt(fechaIn[0]));
                        LocalDate fecha3 = LocalDate.of(Integer.parseInt(fechaF[2]), Integer.parseInt(fechaF[1]), Integer.parseInt(fechaF[0]));

                        int comparacion = fecha1.compareTo(fecha2);

                        if (comparacion > 0) {
                            //fecha es posterior a fecha in filtro
                            int comparacion2 = fecha1.compareTo(fecha3);
                            if (comparacion2 < 0) {
                                actividadesListFiltrado.add(actividades);
                            }else if (comparacion2 == 0) {
                                LocalTime tiempoinicioAct = LocalTime.of(Integer.parseInt(actividadesHoraInicio[0]), Integer.parseInt(actividadesHoraInicio[1]));
                                LocalTime tiempoinFiltro = LocalTime.of(Integer.parseInt(horaF[0]), Integer.parseInt(horaF[1]));

                                int comparaciontiempo = tiempoinicioAct.compareTo(tiempoinFiltro);

                                if (comparaciontiempo > 0) {
                                    System.out.println("tiempo1 es posterior a tiempo2");
                                } else if (comparaciontiempo < 0) {
                                    System.out.println("tiempo1 es anterior a tiempo2");
                                    actividadesListFiltrado.add(actividades);
                                }
                            }
                        } else if (comparacion < 0) {
                            //fecha1 es anterior a fecha2
                        } else {
                            //fecha1 es igual a inicioFiltro
                            //Comparar por horas
                            LocalTime tiempoinicioAct = LocalTime.of(Integer.parseInt(actividadesHoraInicio[0]), Integer.parseInt(actividadesHoraInicio[1]));
                            LocalTime tiempoFinFiltro = LocalTime.of(Integer.parseInt(horaIn[0]), Integer.parseInt(horaIn[1]));

                            int comparaciontiempo = tiempoinicioAct.compareTo(tiempoFinFiltro);

                            if (comparaciontiempo > 0) {
                                System.out.println("tiempo1 es posterior a tiempo2");
                                actividadesListFiltrado.add(actividades);
                            } else if (comparaciontiempo < 0) {
                                System.out.println("tiempo1 es anterior a tiempo2");
                            } else {
                                System.out.println("tiempo1 es igual a tiempo2");
                                actividadesListFiltrado.add(actividades);
                            }
                        }
                    }
                    //mandar al filto:
                    if (actividadesListFiltrado.isEmpty()) {
                        //si no hay actividades
                        binding.textNO.setText("No se hay actividades para estas fechas");
                        binding.textNO.setVisibility(View.VISIBLE);
                    }
                    actividadAdapter.setListaActividades(actividadesListFiltrado);
                    binding.recyclerView.setAdapter(actividadAdapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(ListaActivity.this));

                }
            } else {
                List<Actividades> actividadesListFiltrado = new ArrayList<>();
                for (Actividades actividades : actividadesList) {
                    String[] actividadesFecha = actividades.getFecha().split("/");
                    String[] actividadesHoraInicio = actividades.getInicio().split(":");
                    String[] actividadesHoraFin = actividades.getFin().split(":");

                    LocalDate fecha1 = LocalDate.of(Integer.parseInt(actividadesFecha[2]), Integer.parseInt(actividadesFecha[1]), Integer.parseInt(actividadesFecha[0]));
                    LocalDate fecha2 = LocalDate.of(Integer.parseInt(fechaIn[2]), Integer.parseInt(fechaIn[1]), Integer.parseInt(fechaIn[0]));
                    LocalDate fecha3 = LocalDate.of(Integer.parseInt(fechaF[2]), Integer.parseInt(fechaF[1]), Integer.parseInt(fechaF[0]));

                    int comparacion = fecha1.compareTo(fecha2);

                    if (comparacion > 0) {
                        //fecha es posterior a fecha in filtro
                        int comparacion2 = fecha1.compareTo(fecha3);
                        if (comparacion2 < 0) {
                            actividadesListFiltrado.add(actividades);
                        }else if (comparacion2 == 0) {
                            LocalTime tiempoinicioAct = LocalTime.of(Integer.parseInt(actividadesHoraInicio[0]), Integer.parseInt(actividadesHoraInicio[1]));
                            LocalTime tiempoinFiltro = LocalTime.of(Integer.parseInt(horaF[0]), Integer.parseInt(horaF[1]));

                            int comparaciontiempo = tiempoinicioAct.compareTo(tiempoinFiltro);

                            if (comparaciontiempo > 0) {
                                System.out.println("tiempo1 es posterior a tiempo2");
                            } else if (comparaciontiempo < 0) {
                                System.out.println("tiempo1 es anterior a tiempo2");
                                actividadesListFiltrado.add(actividades);
                            }
                        }
                    } else if (comparacion < 0) {
                        //fecha1 es anterior a fecha2
                    } else {
                        //fecha1 es igual a inicioFiltro
                        //Comparar por horas
                        LocalTime tiempoinicioAct = LocalTime.of(Integer.parseInt(actividadesHoraInicio[0]), Integer.parseInt(actividadesHoraInicio[1]));
                        LocalTime tiempoFinFiltro = LocalTime.of(Integer.parseInt(horaIn[0]), Integer.parseInt(horaIn[1]));

                        int comparaciontiempo = tiempoinicioAct.compareTo(tiempoFinFiltro);

                        if (comparaciontiempo > 0) {
                            System.out.println("tiempo1 es posterior a tiempo2");
                            actividadesListFiltrado.add(actividades);
                        } else if (comparaciontiempo < 0) {
                            System.out.println("tiempo1 es anterior a tiempo2");
                        } else {
                            System.out.println("tiempo1 es igual a tiempo2");
                            actividadesListFiltrado.add(actividades);
                        }
                    }
                }
                //mandar al filto:
                if (actividadesListFiltrado.isEmpty()) {
                    //si no hay actividades
                    binding.textNO.setText("No se hay actividades para estas fechas");
                    binding.textNO.setVisibility(View.VISIBLE);
                }
                    actividadAdapter.setListaActividades(actividadesListFiltrado);
                    binding.recyclerView.setAdapter(actividadAdapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(ListaActivity.this));

            }
        });
    }
    public void mostrarTimeDialogIni() {
        TimePickerDialog timePickerDialog =new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hora, int minuto) {
                String minSTr = ""+minuto;
                if (minuto < 10) {
                    minSTr = "0" + minuto;
                }
                binding.editTextInHora.setText(hora + ":" + minSTr);
            }
        },hora,minutos,true);
        timePickerDialog.show();
    }

    public void mostrarTimeDialogFin() {
        TimePickerDialog timePickerDialog =new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hora, int minuto) {
                String minSTr = ""+minuto;
                if (minuto < 10) {
                    minSTr = "0" + minuto;
                }
                binding.editTextFinHora.setText(hora + ":" + minSTr);
            }
        },hora,minutos,true);
        timePickerDialog.show();
    }

    public void mostrarDateDialogIn(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.editTextInFecha.setText(day+"/"+(month+1)+"/"+year);
            }
        },ahno,mes,dia);
        datePickerDialog.show();
    }
    public void mostrarDateDialogFin(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.editTextFinFecha.setText(day+"/"+(month+1)+"/"+year);
            }
        },ahno,mes,dia);
        datePickerDialog.show();
    }

    public void fechaMayor(){
        Toast.makeText(this, "La fecha de fin debe ser mayor a la de inicio", Toast.LENGTH_SHORT).show();
    }

    public void HoraMayor(){
        Toast.makeText(this, "La Hora de fin debe ser mayor a la de inicio", Toast.LENGTH_SHORT).show();
    }

    }
