package com.ceiba.reserva.servicio.testdatabuilder;

import com.ceiba.carro.modelo.entidad.Carro;
import com.ceiba.carro.modelo.servicio.testdatabuilder.CarroTestDataBuilder;
import com.ceiba.cliente.modelo.entidad.Cliente;
import com.ceiba.cliente.modelo.servicio.testdatabuilder.ClienteTestDataBuilder;
import com.ceiba.reserva.modelo.entidad.Reserva;
import com.ceiba.reserva.modelo.entidad.SolicitudReserva;

import java.time.LocalDateTime;

public class ReservaTestDataBuilder {

    private Cliente cliente;
    private Carro carro;
    private LocalDateTime fechaInicial;
    private LocalDateTime fechaFinal;

    public ReservaTestDataBuilder() {
        this.cliente = new ClienteTestDataBuilder().build();
        this.carro = new CarroTestDataBuilder().build();
        this.fechaInicial = LocalDateTime.now();
        this.fechaFinal = fechaInicial.plusDays(1);
    }

    public ReservaTestDataBuilder conCliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public ReservaTestDataBuilder conCarro(Carro carro) {
        this.carro = carro;
        return this;
    }

    public ReservaTestDataBuilder conFechaInicial(LocalDateTime fechaInicial) {
        this.fechaInicial = fechaInicial;
        return this;
    }

    public ReservaTestDataBuilder conFechaFinal(LocalDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
        return this;
    }

    public Reserva build() {
        SolicitudReserva solicitudReserva = new SolicitudReservaTestDataBuilder()
                .conCarro(carro)
                .conCliente(cliente)
                .conFechaInicial(fechaInicial)
                .conFechaFinal(fechaFinal)
                .build();
        return Reserva.crear(solicitudReserva);
    }
}
