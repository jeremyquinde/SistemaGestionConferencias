package com.conferencias.datos.entidades;

public class Ponente {
    private int id;
    private String nombre;
    private String especialidad;
    private String email;
    private double tarifaHora;
    private boolean disponibilidad;

    public Ponente() {}

    public Ponente(int id, String nombre, String especialidad, String email, double tarifaHora, boolean disponibilidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.email = email;
        this.tarifaHora = tarifaHora;
        this.disponibilidad = disponibilidad;
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
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public double getTarifaHora() {
        return tarifaHora;
    }
    public void setTarifaHora(double tarifaHora) {
        this.tarifaHora = tarifaHora;
    }
    public boolean isDisponibilidad() {
        return disponibilidad;
    }
    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    @Override
    public String toString() {
        return "Ponente [id=" + id + ", nombre=" + nombre + ", especialidad=" + especialidad +
                ", email=" + email + ", tarifaHora=" + tarifaHora + ", disponibilidad=" + disponibilidad + "]";
    }
}
