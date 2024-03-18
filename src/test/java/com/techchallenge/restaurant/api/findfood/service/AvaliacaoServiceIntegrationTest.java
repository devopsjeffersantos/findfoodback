package com.techchallenge.restaurant.api.findfood.service;

import com.techchallenge.restaurant.api.findfood.domain.repository.AvaliacaoRepository;
import com.techchallenge.restaurant.api.findfood.domain.service.AvaliacaoService;
import com.techchallenge.restaurant.api.findfood.dados.AvaliacaoDados;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AvaliacaoServiceIntegrationTest extends AvaliacaoDados {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private AvaliacaoService avaliacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class registrarAvaliacao {
        @Test
        @Order(1)
        void deveRegistrarAvaliacaoComSucesso() {
            var restauranteId = 1L;
            var avaliacaoDTO = criarAvaliacaoDto();

            var contador = avaliacaoRepository.count();
            avaliacaoService.registrarAvaliacao(restauranteId, avaliacaoDTO);
            var contadorAtualizado = avaliacaoRepository.count();

            assertThat(contador + 1).isEqualTo(contadorAtualizado);
        }

        @Test
        void deveLancarExceptionAoTentarRegistrarAvaliacaoComRestauranteNaoEncontrado() {
            var restauranteId = 0L;
            var avaliacaoDTO = criarAvaliacaoDto();

            assertThatThrownBy(() -> avaliacaoService.registrarAvaliacao(restauranteId, avaliacaoDTO))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Restaurante não foi encontrada");
        }

        @Test
        void deveLancarExceptionAoTentarRegistrarAvaliacaoComPontuacaoAcimaDe5() {
            var restauranteId = 1L;
            var avaliacaoDTO = criarAvaliacaoDto();
            avaliacaoDTO.setPontuacao(6);

            assertThatThrownBy(() -> avaliacaoService.registrarAvaliacao(restauranteId, avaliacaoDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Pontuação para a reserva precisa ser um valor de 0 a 5");
        }

        @Test
        void deveLancarExceptionAoTentarRegistrarAvaliacaoComPontuacaoAbaixoDe0() {
            var restauranteId = 1L;
            var avaliacaoDTO = criarAvaliacaoDto();
            avaliacaoDTO.setPontuacao(-1);

            assertThatThrownBy(() -> avaliacaoService.registrarAvaliacao(restauranteId, avaliacaoDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Pontuação para a reserva precisa ser um valor de 0 a 5");
        }
    }

    @Nested
    class findAll {
        @Test
        @Order(2)
        void deveRetornarTodasAsAvaliacoesComSucesso() {
            var restauranteId = 1L;

            var result = avaliacaoService.findAll(restauranteId);

            assertThat(result).isNotEmpty();
        }

        @Test
        void deveLancarExceptionAoTentarEncontrarAvalicoesEmRestauranteQueNaoExiste() {
            var restauranteId = 0L;

            assertThatThrownBy(() -> avaliacaoService.findAll(restauranteId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Restaurante não foi encontrada");

        }
    }
}
