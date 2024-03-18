package com.techchallenge.restaurant.api.findfood.service;

import com.techchallenge.restaurant.api.findfood.domain.model.Avaliacao;
import com.techchallenge.restaurant.api.findfood.domain.repository.AvaliacaoRepository;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import com.techchallenge.restaurant.api.findfood.dados.AvaliacaoDados;
import com.techchallenge.restaurant.api.findfood.domain.service.AvaliacaoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvaliacaoServiceTest extends AvaliacaoDados {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class registrarAvaliacao {
        @Test
        void deveRegistrarAvaliacaoComSucesso() {
            var restauranteId = 1L;
            var avaliacaoDTO = criarAvaliacaoDto();
            var restaurante = criarRestaurante();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(avaliacaoDTO, Avaliacao.class)).thenReturn(criarAvaliacao());

            avaliacaoService.registrarAvaliacao(restauranteId, avaliacaoDTO);

            verify(avaliacaoRepository, times(1)).save(any());
        }

        @Test
        void deveLancarExceptionAoTentarRegistrarAvaliacaoComRestauranteNaoEncontrado() {
            var restauranteId = 1L;
            var avaliacaoDTO = criarAvaliacaoDto();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> avaliacaoService.registrarAvaliacao(restauranteId, avaliacaoDTO));

            verify(avaliacaoRepository, never()).save(any());
        }

        @Test
        void deveLancarExceptionAoTentarRegistrarAvaliacaoComPontuacaoAcimaDe5() {
            var restauranteId = 1L;
            var avaliacaoDTO = criarAvaliacaoDto();
            var restaurante = criarRestaurante();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(avaliacaoDTO, Avaliacao.class)).thenReturn(criarAvaliacaoComPontuacaoInvalidaAcima5());

            assertThrows(IllegalArgumentException.class, () -> avaliacaoService.registrarAvaliacao(restauranteId, avaliacaoDTO));

            verify(avaliacaoRepository, never()).save(any());
        }

        @Test
        void deveLancarExceptionAoTentarRegistrarAvaliacaoComPontuacaoAbaixoDe0() {
            var restauranteId = 1L;
            var restaurante = criarRestaurante();
            var avaliacaoDTO = criarAvaliacaoDto();
            avaliacaoDTO.setPontuacao(6);

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(avaliacaoDTO, Avaliacao.class)).thenReturn(criarAvaliacaoComPontuacaoInvalidaAbaixo0());

            assertThrows(IllegalArgumentException.class, () -> avaliacaoService.registrarAvaliacao(restauranteId, avaliacaoDTO));

            verify(avaliacaoRepository, never()).save(any());
        }
    }

    @Nested
    class findAll {
        @Test
        void deveRetornarTodasAsAvaliacoesComSucesso() {
            var restauranteId = 1L;
            var restaurante = criarRestaurante();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(avaliacaoRepository.findAll()).thenReturn(Collections.emptyList());

            var lista = avaliacaoService.findAll(restauranteId);

            assertNotNull(lista);
            assertTrue(lista.isEmpty());
        }

        @Test
        void deveLancarExceptionAoTentarEncontrarAvalicoesEmRestauranteQueNaoExiste() {
            var restauranteId = 1L;

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> avaliacaoService.findAll(restauranteId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Restaurante não foi encontrada");

        }
    }
}
