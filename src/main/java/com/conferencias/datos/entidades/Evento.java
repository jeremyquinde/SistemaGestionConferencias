package com.conferencias.datos.entidades;

import java.time.LocalDateTime;

public class Evento {
    private int id;
    private String nombre;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private int capacidad;
    private String estado;
    private int ubicacionId;

    public Evento() {}

    public Evento(int id, String nombre, LocalDateTime fechaInicio, LocalDateTime fechaFin, int capacidad, String estado, int ubicacionId) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.capacidad = capacidad;
        this.estado = estado;
        this.ubicacionId = ubicacionId;
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
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public int getUbicacionId() {
        return ubicacionId;
    }
    public void setUbicacionId(int ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    @Override
    public String toString() {
        return "Evento [id=" + id + ", nombre=" + nombre + ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin + ", capacidad=" + capacidad + ", estado=" + estado +
                ", ubicacionId=" + ubicacionId + "]";
    }
}
