package org.pucmm.asig1;

public class Empleado {
    private String nombre;
    private TipoEmpleado tipo;
    private double tarifaPorHora;

    public Empleado(String nombre, TipoEmpleado tipo, double tarifaPorHora) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tarifaPorHora = tarifaPorHora;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public TipoEmpleado getTipo() {
        return tipo;
    }

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }
}