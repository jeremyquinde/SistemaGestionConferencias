package com.conferencias.datos.entidades;

public class Patrocinador {
    private int id;
    private String nombre;
    private String nivel; // 'ORO', 'PLATA', 'BRONCE'
    private double aporteEconomico;
    private int eventoId;

    public Patrocinador() {}

    public Patrocinador(int id, String nombre, String nivel, double aporteEconomico, int eventoId) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.aporteEconomico = aporteEconomico;
        this.eventoId = eventoId;
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
    public String getNivel() {
        return nivel;
    }
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
    public double getAporteEconomico() {
        return aporteEconomico;
    }
    public void setAporteEconomico(double aporteEconomico) {
        this.aporteEconomico = aporteEconomico;
    }
    public int getEventoId() {
        return eventoId;
    }
    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    @Override
    public String toString() {
        return "Patrocinador [id=" + id + ", nombre=" + nombre + ", nivel=" + nivel +
                ", aporteEconomico=" + aporteEconomico + ", eventoId=" + eventoId + "]";
    }
}
