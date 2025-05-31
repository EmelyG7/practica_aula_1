package org.pucmm.asig1;

public class Empleado {
    private String nombre;
    private TipoEmpleado tipo;
    private double tarifaPorHora;
    private boolean autorizado;

    public Empleado(String nombre, TipoEmpleado tipo, double tarifaPorHora, boolean autorizado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tarifaPorHora = tarifaPorHora;
        this.autorizado = autorizado;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public TipoEmpleado getTipo() {
        return tipo;
    }

    public boolean isAutorizado() {
        return autorizado;
    }
    public double getTarifaPorHora() {
        return tarifaPorHora;
    }
}