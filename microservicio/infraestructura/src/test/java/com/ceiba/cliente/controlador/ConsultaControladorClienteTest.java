package com.ceiba.cliente.controlador;

import com.ceiba.ApplicationMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationMock.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WebMvcTest(ConsultaControladorCliente.class)
class ConsultaControladorClienteTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deberiaConsultarCliente() throws Exception {
        // arrange
        // act - assert
        mockMvc.perform(get("/clientes/{documento}", "8000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("documento", is("8000")))
                .andExpect(jsonPath("nombre", is("EUDIS RENE DUARTE")));

    }

    @Test
    void deberiaRetornarErroSiClienteNoExiste() throws Exception {
        // arrange
        // act - assert
        mockMvc.perform(get("/clientes/{documento}", "123456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("{'mensaje':'No existe un cliente con ese documento'}"));

    }

}