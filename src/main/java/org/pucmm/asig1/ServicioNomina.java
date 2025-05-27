package org.pucmm.asig1;

// ServicioNomina.java
public class ServicioNomina {
    private static final double TOPE_SALARIO = 20000;
    private static final double TASA_HORAS_EXTRA = 1.5;
    private static final double BONO_PUNTUALIDAD = 500;
    private static final int HORAS_BONO = 38;
    private static final int HORAS_NORMALES = 40;

    public double calcularNomina(Empleado empleado, int horasTrabajadas) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }

        if (horasTrabajadas < 0) {
            throw new IllegalArgumentException("Las horas trabajadas no pueden ser negativas");
        }

        double salario = calcularSalarioBase(empleado, horasTrabajadas);
        if (salario > TOPE_SALARIO) {
            // Si el salario excede el tope, no se aplica bono ni horas extra
            return TOPE_SALARIO;
        }
        salario = aplicarHorasExtra(empleado, horasTrabajadas, salario);
        salario = aplicarBonoPuntualidad(horasTrabajadas, salario);

        return salario;
    }

    private double calcularSalarioBase(Empleado empleado, int horasTrabajadas) {
        // Para PART_TIME, todas las horas se pagan igual
        if (empleado.getTipo() == TipoEmpleado.PART_TIME) {
            return horasTrabajadas * empleado.getTarifaPorHora();
        }
        // Para FULL_TIME, solo las primeras 40 horas son normales
        int horasNormales = Math.min(horasTrabajadas, HORAS_NORMALES);
        return horasNormales * empleado.getTarifaPorHora();
    }

    private double aplicarHorasExtra(Empleado empleado, int horasTrabajadas, double salario) {
        // Solo aplica a FULL_TIME
        if (empleado.getTipo() == TipoEmpleado.FULL_TIME && horasTrabajadas > HORAS_NORMALES) {
            int horasExtra = horasTrabajadas - HORAS_NORMALES;
            salario += horasExtra * empleado.getTarifaPorHora() * TASA_HORAS_EXTRA;
        }
        return salario;
    }

    private double aplicarBonoPuntualidad(int horasTrabajadas, double salario) {
        if (horasTrabajadas > HORAS_BONO) {
            salario += BONO_PUNTUALIDAD;
        }
        return salario;
    }
}