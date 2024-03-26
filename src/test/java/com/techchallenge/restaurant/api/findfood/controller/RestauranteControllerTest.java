package com.techchallenge.restaurant.api.findfood.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.restaurant.api.findfood.api.controller.RestauranteController;
import com.techchallenge.restaurant.api.findfood.api.model.AvaliacaoDTO;
import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import com.techchallenge.restaurant.api.findfood.domain.service.RestauranteService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.techchallenge.restaurant.api.findfood.dados.ReservaDados.criarRestauranteValido;
import static com.techchallenge.restaurant.api.findfood.dados.RestauranteDados.criarRestauranteDtoValido;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestauranteController.class)
@AutoConfigureMockMvc
public class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestauranteService restauranteService;

    @MockBean
    private RestauranteRepository restauranteRepository;

    @Nested
    @DisplayName("Testes de Registro de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class registrarRestaurante {
        @Test
        @Order(1)
        void devePermitirRegistrarRestaurante() throws Exception {

            // Arrange
            RestauranteDTO restauranteDTO = criarRestauranteDtoValido();
            doNothing().when(restauranteService).registrarRestaurante(any(RestauranteDTO.class));

            // Act & Assert
            mockMvc.perform(post("/api/restaurantes/registrarRestaurante")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restauranteDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Restaurante Registrado com Sucesso"));
                verify(restauranteService, times(1)).registrarRestaurante(restauranteDTO);

        }

    }

    @Nested
    @DisplayName("Testes de Atualização de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class atualizarRestaurante {
        @Test
        @Order(1)
        void devePermitirAtualizarRestaurantes() throws Exception {

            // Arrange
            Long restauranteId = 1L;
            Restaurante restaurante = criarRestauranteValido();
            RestauranteDTO restauranteDTO = criarRestauranteDtoValido();
            when(restauranteService.atualizarRestaurante(anyLong(), any(RestauranteDTO.class))).thenReturn(restauranteDTO);

            // Act & Assert
            mockMvc.perform(put("/api/restaurantes/atualizar/{restauranteId}", restauranteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restauranteDTO)))
                .andExpect(status().isOk());
                verify(restauranteService, times(1)).atualizarRestaurante(restauranteId, restauranteDTO);
        }
    }
    @Nested
    @DisplayName("Testes de Exclusão de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class deletarRestaurante {
        @Test
        @Order(1)
        void devePermitirDeletarRestaurantes() throws Exception {

            // Arrange
            Long restauranteId = 1L;
            doNothing().when(restauranteService).deletarRestaurante(anyLong());

            // Act & Assert
            mockMvc.perform(delete("/api/restaurantes/deletar/{restauranteId}", restauranteId))
                .andExpect(status().isNoContent());
                verify(restauranteService, times(1)).deletarRestaurante(anyLong());
        }
    }

    @Nested
    @DisplayName("Testes de Busca de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class buscarRestaurante {

        @Test
        @Order(1)
        void devePermitirBuscarRestaurantePor() throws Exception {

            // Arrange
            Restaurante restaurante = criarRestauranteValido(); // Supondo que criarRestauranteValido() retorne um objeto Restaurante válido

            List<Restaurante> restaurantes = Collections.singletonList(restaurante);

            when(restauranteService.buscarRestaurantePor(any(), any(), any())).thenReturn(restaurantes);

            // Act & Assert
            mockMvc.perform(get("/api/restaurantes/pesquisa"))
                    .andExpect(status().isOk());

            verify(restauranteService, times(1)).buscarRestaurantePor(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
        }

        @Test
        @Order(5)
        void devePermitirBuscarTodosRestaurantes() throws Exception {

            // Arrange
            List<Restaurante> restaurante = Arrays.asList(criarRestauranteValido(), criarRestauranteValido());
            when(restauranteService.buscarTodosRestaurantes()).thenReturn(restaurante);

            // Act & Assert
            mockMvc.perform(get("/api/restaurantes/todos"))
                .andExpect(status().isOk());
                verify(restauranteService, times(1)).buscarTodosRestaurantes();

        }
    }
}
