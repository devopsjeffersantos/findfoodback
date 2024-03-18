package com.techchallenge.restaurant.api.findfood.controller;

import com.techchallenge.restaurant.api.findfood.api.model.AvaliacaoDTO;
import com.techchallenge.restaurant.api.findfood.dados.AvaliacaoDados;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AvaliacaoControllerIntegrationTest extends AvaliacaoDados {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class registrarAvaliacao {

        @Test
        void deveRegistrarAvaliacaoComSucesso() {
            Long restauranteId = 1L;
            AvaliacaoDTO avaliacaoDTO = criarAvaliacaoDto();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(avaliacaoDTO)
            .when()
                    .post("/api/restaurantes/{restauranteId}/avaliar", restauranteId)
            .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body(equalTo("Avaliação feita com sucesso"));
        }

        @Test
        void deveLancarExceptionQuandoTentarRegistrarAvaliacaoParaRestauranteInexistente() {
            Long restauranteId = 2L;
            AvaliacaoDTO avaliacaoDTO = criarAvaliacaoDto();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(avaliacaoDTO)
            .when()
                    .post("/api/restaurantes/{restauranteId}/avaliar", restauranteId)
            .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("path", equalTo("/api/restaurantes/2/avaliar"))
                    .body("message", equalTo("Restaurante não foi encontrada"));
        }

        @Test
        void deveLancarExceptionQuandoTentarRegistrarAvaliacaoComPontuacaoMaiorQue5() {
            Long restauranteId = 1L;
            AvaliacaoDTO avaliacaoDTO = criarAvaliacaoDto();
            avaliacaoDTO.setPontuacao(6);

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(avaliacaoDTO)
            .when()
                    .post("/api/restaurantes/{restauranteId}/avaliar", restauranteId)
            .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("path", equalTo("/api/restaurantes/1/avaliar"))
                    .body("message", equalTo("Pontuação para a reserva precisa ser um valor de 0 a 5"));
        }

        @Test
        void deveLancarExceptionQuandoTentarRegistrarAvaliacaoComPontuacaoMenorQue0() {
            Long restauranteId = 1L;
            AvaliacaoDTO avaliacaoDTO = criarAvaliacaoDto();
            avaliacaoDTO.setPontuacao(-1);

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(avaliacaoDTO)
            .when()
                    .post("/api/restaurantes/{restauranteId}/avaliar", restauranteId)
            .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("path", equalTo("/api/restaurantes/1/avaliar"))
                    .body("message", equalTo("Pontuação para a reserva precisa ser um valor de 0 a 5"));
        }
    }


    @Nested
    class findAll {

        @Test
        void deveRetornarTodasAsAvaliacoesComSucesso() {
            Long restauranteId = 1L;

            given()
            .when()
                    .get("/api/restaurantes/{restauranteId}/avaliacoes", restauranteId)
            .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void deveLancarExceptionQuandoNaoEncontrarRestaurante() {
            Long restauranteId = 2L;

            given()
            .when()
                    .get("/api/restaurantes/{restauranteId}/avaliacoes", restauranteId)
            .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("path", equalTo("/api/restaurantes/2/avaliacoes"))
                    .body("message", equalTo("Restaurante não foi encontrada"));

        }
    }
}