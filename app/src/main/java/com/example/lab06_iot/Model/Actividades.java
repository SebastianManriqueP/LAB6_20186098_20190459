package com.example.lab06_iot.Model;

public class Actividades {
    private String titulo;
    private String descripcion;
    private String fecha;
    private String inicio;
    private String fin;
    private String imagen;

    private String id;

    public Actividades() {
    }

    public Actividades(String titulo, String descripcion, String fecha, String inicio, String fin, String imagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.inicio = inicio;
        this.fin = fin;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
