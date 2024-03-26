package com.techchallenge.restaurant.api.findfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.restaurant.api.findfood.api.controller.ReservaController;
import com.techchallenge.restaurant.api.findfood.api.model.ReservaDTO;
import com.techchallenge.restaurant.api.findfood.dados.ReservaDados;
import com.techchallenge.restaurant.api.findfood.domain.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc
class ReservaControllerTest extends ReservaDados {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveReservarMesaComSucesso() throws Exception {
        Long restauranteId = 1L;
        ReservaDTO reservaDTO = criarReservaDto();

        when(reservaService.reservarMesa(anyLong(), any(ReservaDTO.class))).thenReturn(reservaDTO);

        mockMvc.perform(post("/api/restaurantes/{restauranteId}/reservarMesa", restauranteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))
                        .andExpect(status().isCreated())
                        .andExpect(content().json(objectMapper.writeValueAsString(reservaDTO)));

        verify(reservaService, times(1)).reservarMesa(anyLong(), any(ReservaDTO.class));
    }

    @Test
    void deveRetornarTodasAsReservasParaUmRestauranteComSucesso() throws Exception {
        Long restauranteId = 1L;
        List<ReservaDTO> reservas = Arrays.asList(criarReservaDto(), criarReservaDto());

        when(reservaService.findAll(restauranteId)).thenReturn(reservas);

        mockMvc.perform(get("/api/restaurantes/{restauranteId}/reservas", restauranteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(reservas.size()));

        verify(reservaService, times(1)).findAll(restauranteId);
    }

    @Test
    void deveRetornarUmaReservaComSucesso() throws Exception {
        ReservaDTO reservaDTO = criarReservaDto();

        when(reservaService.findById(anyLong())).thenReturn(reservaDTO);

        mockMvc.perform(get("/api/restaurantes/reserva/{reservaId}", reservaDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reservaDTO)));

        verify(reservaService, times(1)).findById(anyLong());
    }

    @Test
    void deveExcluirUmaReservaComSucesso() throws Exception {
        ReservaDTO reservaDTO = criarReservaDto();

        doNothing().when(reservaService).delete(anyLong());

        mockMvc.perform(delete("/api/restaurantes/reserva/{reservaId}", reservaDTO.getId()))
                .andExpect(status().isNoContent());

        verify(reservaService, times(1)).delete(anyLong());
    }

}
