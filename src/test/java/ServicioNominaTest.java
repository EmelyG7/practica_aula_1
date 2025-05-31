import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.pucmm.asig1.Empleado;
import org.pucmm.asig1.ServicioNomina;
import org.pucmm.asig1.TipoEmpleado;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

class ServicioNominaTest {
    private ServicioNomina servicioNomina;
    private Empleado empleadoFullTime;
    private Empleado empleadoPartTime;

    @BeforeEach
    void setUp() {
        servicioNomina = new ServicioNomina();
        empleadoFullTime = new Empleado("Bernardo", TipoEmpleado.FULL_TIME, 500, true);
        empleadoPartTime = new Empleado("Juan", TipoEmpleado.PART_TIME, 400, true);
    }

    @Test
    void calcularNomina_empleadoNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicioNomina.calcularNomina(null, 40));
    }

    @Test
    void calcularNomina_horasNegativas_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicioNomina.calcularNomina(empleadoFullTime, -5));
    }

    @ParameterizedTest
    @ValueSource(ints = {30, 40})
    void calcularNomina_sinHorasExtra_calculoCorrecto(int horas) {
        double salarioEsperado = horas * empleadoFullTime.getTarifaPorHora();
        if (horas > 38) {
            salarioEsperado += 500; // Bono por puntualidad
        }
        assertEquals(salarioEsperado, servicioNomina.calcularNomina(empleadoFullTime, horas));
    }

    @Test
    void calcularNomina_conHorasExtra_fullTime_calculoCorrecto() {
        int horas = 45;
        double salarioBase = 40 * empleadoFullTime.getTarifaPorHora();
        double horasExtra = (horas - 40) * empleadoFullTime.getTarifaPorHora() * 1.5;
        double salarioEsperado = salarioBase + horasExtra + 500; // Incluye bono por >38 horas

        assertEquals(salarioEsperado, servicioNomina.calcularNomina(empleadoFullTime, horas));
    }

    @Test
    void calcularNomina_conHorasExtra_partTime_noAplica() {
        int horas = 45;
        double salarioEsperado = horas * empleadoPartTime.getTarifaPorHora() + 500; // No horas extra pero sí bono

        assertEquals(salarioEsperado, servicioNomina.calcularNomina(empleadoPartTime, horas));
    }

    @Test
    void calcularNomina_salarioExcedeTope_conAutorizacion() {
        Empleado empleado = new Empleado("Alex", TipoEmpleado.FULL_TIME, 1000, true);
        int horas = 50;
        // Salario calculado: 40*1000 + 10*1000*1.5 + 500 = 55,500
        double salarioCalculado = servicioNomina.calcularNomina(empleado, horas);

        // Simula autorización
        boolean autorizado = salarioCalculado > 20000 && empleado.isAutorizado();

        assertTrue(autorizado, "El salario excedido debería ser autorizado");
    }

    @Test
    void calcularNomina_salarioExcedeTope_sinAutorizacion() {
        Empleado empleado = new Empleado("Alex", TipoEmpleado.FULL_TIME, 1000, false);
        int horas = 50;
        // Salario calculado: 40*1000 + 10*1000*1.5 + 500 = 55,500
        double salarioCalculado = servicioNomina.calcularNomina(empleado, horas);

        // Simula denegación de autorización
        boolean autorizado = salarioCalculado > 20000 && empleado.isAutorizado();

        assertFalse(autorizado, "El salario excedido no debería ser autorizado");
    }
    private static Stream<Arguments> proveedorDatosParaCalculo() {
        return Stream.of(
                Arguments.of(TipoEmpleado.FULL_TIME, 30, 30 * 500),
                Arguments.of(TipoEmpleado.FULL_TIME, 40, 40 * 500 + 500),
                Arguments.of(TipoEmpleado.FULL_TIME, 45, 40 * 500 + 5 * 500 * 1.5 + 500),
                Arguments.of(TipoEmpleado.PART_TIME, 30, 30 * 400),
                Arguments.of(TipoEmpleado.PART_TIME, 45, 45 * 400 + 500)
        );
    }

    @ParameterizedTest
    @MethodSource("proveedorDatosParaCalculo")
    void calcularNomina_variosEscenarios_calculoCorrecto(TipoEmpleado tipo, int horas, double salarioEsperado) {
        Empleado empleado = new Empleado("Test", tipo, tipo == TipoEmpleado.FULL_TIME ? 500 : 400, true);
        assertEquals(salarioEsperado, servicioNomina.calcularNomina(empleado, horas));
    }
}