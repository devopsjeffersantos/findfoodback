package com.techchallenge.restaurant.api.findfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.restaurant.api.findfood.api.controller.AvaliacaoController;
import com.techchallenge.restaurant.api.findfood.api.model.AvaliacaoDTO;
import com.techchallenge.restaurant.api.findfood.domain.service.AvaliacaoService;
import com.techchallenge.restaurant.api.findfood.dados.AvaliacaoDados;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvaliacaoController.class)
@AutoConfigureMockMvc
class AvaliacaoControllerTest extends AvaliacaoDados {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvaliacaoService avaliacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRegistrarAvaliacaoComSucesso() throws Exception {
        Long restauranteId = 1L;
        AvaliacaoDTO avaliacaoDTO = criarAvaliacaoDto();

        doNothing().when(avaliacaoService).registrarAvaliacao(anyLong(), any(AvaliacaoDTO.class));

        mockMvc.perform(post("/api/restaurantes/{restauranteId}/avaliar", restauranteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avaliacaoDTO)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string("Avaliação feita com sucesso"));

        verify(avaliacaoService, times(1)).registrarAvaliacao(restauranteId, avaliacaoDTO);
    }

    @Test
    void deveRetornarTodasAsAvaliacoesComSucesso() throws Exception {
        Long restauranteId = 1L;
        List<AvaliacaoDTO> avaliacoes = Arrays.asList(criarAvaliacaoDto(), criarAvaliacaoDto());

        when(avaliacaoService.findAll(restauranteId)).thenReturn(avaliacoes);

        mockMvc.perform(get("/api/restaurantes/{restauranteId}/avaliacoes", restauranteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(avaliacoes.size()));

        verify(avaliacaoService, times(1)).findAll(restauranteId);
    }

}
