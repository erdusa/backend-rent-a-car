package com.ceiba.reserva.adaptador.repositorio;

import com.ceiba.carro.adaptador.repositorio.MapeoCarro;
import com.ceiba.carro.modelo.entidad.Carro;
import com.ceiba.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import com.ceiba.infraestructura.jdbc.sqlstatement.SqlStatement;
import com.ceiba.reserva.modelo.entidad.Reserva;
import com.ceiba.reserva.puerto.repositorio.RepositorioReserva;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class RepositorioReservaPostgreSQL implements RepositorioReserva {

    public static final String EL_CARRO_NO_ESTA_DISPONIBLE = "El carro no está disponible";
    @SqlStatement(namespace = "reserva", value = "crear.sql")
    private static String sqlCrear;
    @SqlStatement(namespace = "reserva", value = "cancelar.sql")
    private static String sqlCancelar;
    @SqlStatement(namespace = "reserva", value = "consultar")
    private static String sqlConsultar;
    @SqlStatement(namespace = "reserva", value = "obtenerCarroSiNoEstaReservado")
    private static String sqlObtenerCarroSiNoEstaReservado;

    private static final Logger LOGGER = Logger.getLogger(RepositorioReservaPostgreSQL.class.getName());
    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    public RepositorioReservaPostgreSQL(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
    }

    @Override
    public Long crear(Reserva reserva) {
        return this.customNamedParameterJdbcTemplate.crear(reserva, sqlCrear);
    }

    @Override
    public void cancelar(Reserva reserva) {
        this.customNamedParameterJdbcTemplate.actualizar(reserva, sqlCancelar);
    }

    @Override
    public Reserva consultar(Long idReserva) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", idReserva);

        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlConsultar, paramSource, new MapeoReserva());
    }

    @Override
    public Carro obtenerCarroSiNoEstaReservado(Long idCarro, LocalDate fechaInicial, LocalDate fechaFinal) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("idCarro", idCarro);
        paramSource.addValue("fechainicial", fechaInicial);
        paramSource.addValue("fechafinal", fechaFinal);
        try {
            return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlObtenerCarroSiNoEstaReservado, paramSource, new MapeoCarro());
        } catch (EmptyResultDataAccessException e) {
            LOGGER.log(Level.INFO, EL_CARRO_NO_ESTA_DISPONIBLE, e);
        }
        return null;
    }
}
