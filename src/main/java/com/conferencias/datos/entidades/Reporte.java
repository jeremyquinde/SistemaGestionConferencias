package com.conferencias.datos.entidades;

import java.time.LocalDateTime;

public class Reporte {
    private int id;
    private String tipo; // 'ASISTENCIA' o 'INGRESOS'
    private LocalDateTime fechaGeneracion;
    private String datosBrutos; // Almacenado en formato JSON o texto largo
    private int usuarioId;

    public Reporte() {}

    public Reporte(int id, String tipo, LocalDateTime fechaGeneracion, String datosBrutos, int usuarioId) {
        this.id = id;
        this.tipo = tipo;
        this.fechaGeneracion = fechaGeneracion;
        this.datosBrutos = datosBrutos;
        this.usuarioId = usuarioId;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    public String getDatosBrutos() {
        return datosBrutos;
    }
    public void setDatosBrutos(String datosBrutos) {
        this.datosBrutos = datosBrutos;
    }
    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "Reporte [id=" + id + ", tipo=" + tipo + ", fechaGeneracion=" + fechaGeneracion +
                ", datosBrutos=" + datosBrutos + ", usuarioId=" + usuarioId + "]";
    }
}
