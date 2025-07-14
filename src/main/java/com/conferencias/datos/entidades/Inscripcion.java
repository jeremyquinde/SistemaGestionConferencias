package com.conferencias.datos.entidades;

import java.time.LocalDateTime;

public class Inscripcion {
    private int id;
    private int usuarioId;
    private int eventoId;
    private LocalDateTime fechaInscripcion;
    private String estado; // 'CONFIRMADA' o 'PENDIENTE'

    public Inscripcion() {}

    public Inscripcion(int id, int usuarioId, int eventoId, LocalDateTime fechaInscripcion, String estado) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.fechaInscripcion = fechaInscripcion;
        this.estado = estado;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public int getEventoId() {
        return eventoId;
    }
    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }
    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }
    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Inscripcion [id=" + id + ", usuarioId=" + usuarioId +
                ", eventoId=" + eventoId + ", fechaInscripcion=" + fechaInscripcion +
                ", estado=" + estado + "]";
    }
}
