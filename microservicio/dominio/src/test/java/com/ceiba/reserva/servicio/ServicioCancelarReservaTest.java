package com.ceiba.reserva.servicio;

import com.ceiba.BasePrueba;
import com.ceiba.dominio.excepcion.ExcepcionSinDatos;
import com.ceiba.dominio.excepcion.ExcepcionValorInvalido;
import com.ceiba.reserva.modelo.entidad.Reserva;
import com.ceiba.reserva.puerto.repositorio.RepositorioReserva;
import com.ceiba.reserva.servicio.testdatabuilder.ReservaTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

class ServicioCancelarReservaTest {

    public static final String SOLO_SE_PUEDE_CANCELAR_LA_RESERVA_2_DIAS_ANTES_Y_MAXIMO_HASTA_LAS_7_PM = "Sólo se puede cancelar la reserva 2 días antes y máximo hasta las 7 PM";
    public static final String NO_EXISTE_LA_RESERVA_PARA_EL_ID_PROPORCIONADO = "No existe la reserva para el id proporcionado";

    private RepositorioReserva repositorioReserva;
    private ServicioCancelarReserva servicioCancelarReserva;

    @BeforeEach
    void setUp() {
        repositorioReserva = Mockito.mock(RepositorioReserva.class);
        servicioCancelarReserva = new ServicioCancelarReserva(repositorioReserva);
    }

    @Test
    void deberiaCancelarLaReservaCorrectamente() {
        //arrange
        LocalDateTime fechaInicial = LocalDateTime.now().plusDays(2);
        Reserva reserva = new ReservaTestDataBuilder()
                .conFechaInicial(fechaInicial)
                .conFechaFinal(fechaInicial.plusDays(1))
                .build();
        Mockito.when(repositorioReserva.consultar(Mockito.anyLong())).thenReturn(reserva);
        // act
        servicioCancelarReserva.ejecutar(Mockito.anyLong());
        // assert
        Mockito.verify(repositorioReserva, Mockito.times(1))
                .cancelar(Mockito.any());
        Mockito.verify(repositorioReserva, Mockito.times(1))
                .consultar(Mockito.anyLong());
    }

    @Test
    void deberiaLanzarErrorSiNoExisteLaReserva() {
        //arrange
        Mockito.when(repositorioReserva.consultar(Mockito.anyLong())).thenReturn(null);
        // act - assert
        BasePrueba.assertThrows(() -> servicioCancelarReserva.ejecutar(Mockito.anyLong()), ExcepcionSinDatos.class,
                NO_EXISTE_LA_RESERVA_PARA_EL_ID_PROPORCIONADO);
        Mockito.verify(repositorioReserva, Mockito.times(0))
                .cancelar(Mockito.any());
        Mockito.verify(repositorioReserva, Mockito.times(1))
                .consultar(Mockito.anyLong());
    }

    @Test
    void deberiaLanzarErrorSiLaFechaActualNoPermiteCancelar() {
        //arrange
        LocalDateTime fechaInicial = LocalDateTime.now().plusDays(1);
        Reserva reserva = new ReservaTestDataBuilder()
                .conFechaInicial(fechaInicial)
                .conFechaFinal(fechaInicial.plusDays(1))
                .build();
        Mockito.when(repositorioReserva.consultar(Mockito.anyLong())).thenReturn(reserva);
        // act - assert
        BasePrueba.assertThrows(() -> servicioCancelarReserva.ejecutar(Mockito.anyLong()), ExcepcionValorInvalido.class,
                SOLO_SE_PUEDE_CANCELAR_LA_RESERVA_2_DIAS_ANTES_Y_MAXIMO_HASTA_LAS_7_PM);
        Mockito.verify(repositorioReserva, Mockito.times(0))
                .cancelar(reserva);
        Mockito.verify(repositorioReserva, Mockito.times(1))
                .consultar(Mockito.anyLong());
    }
}