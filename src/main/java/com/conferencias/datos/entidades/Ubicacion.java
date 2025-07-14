package com.conferencias.datos.entidades;

public class Ubicacion {
    private int id;
    private String nombre;
    private String direccion;
    private int capacidad;
    private String tipo; // 'FISICA' o 'VIRTUAL'

    public Ubicacion() {}

    public Ubicacion(int id, String nombre, String direccion, int capacidad, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidad = capacidad;
        this.tipo = tipo;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Ubicacion [id=" + id + ", nombre=" + nombre + ", direccion=" + direccion +
                ", capacidad=" + capacidad + ", tipo=" + tipo + "]";
    }
}
