package com.techchallenge.restaurant.api.findfood.controller;

import com.techchallenge.restaurant.api.findfood.api.model.ReservaDTO;
import com.techchallenge.restaurant.api.findfood.dados.ReservaDados;
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
class ReservaControllerIntegrationTest extends ReservaDados {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }


    @Nested
    class reservarMesa {

        @Test
        void deveReservarMesaComSucesso() {
            Long restauranteId = 1L;
            ReservaDTO reservaDTO = criarReservaDto();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(reservaDTO)
            .when()
                    .post("/api/restaurantes/{restauranteId}/reservarMesa", restauranteId)
            .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("nomeCliente", equalTo(reservaDTO.getNomeCliente()))
                    .body("emailCliente", equalTo(reservaDTO.getEmailCliente()))
                    .body("telefoneCliente", equalTo(reservaDTO.getTelefoneCliente()))
                    .body("qtdPessoas", equalTo(reservaDTO.getQtdPessoas()));
        }

        @Test
        void deveLancarExceptionAoTentarReservarEmRestauranteInexistente() {
            Long restauranteId = 0L;
            ReservaDTO reservaDTO = criarReservaDto();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(reservaDTO)
            .when()
                    .post("/api/restaurantes/{restauranteId}/reservarMesa", restauranteId)
            .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("path", equalTo("/api/restaurantes/0/reservarMesa"))
                    .body("message", equalTo("Restaurante não existe"));
        }

        @Test
        void deveLancarExceptionAoTentarReservarEmRestauranteQueNaoPossuiMesasDisponiveis() {
            Long restauranteId = 1L;
            ReservaDTO reservaDTO = criarReservaDto();
            reservaDTO.setQtdPessoas(500);

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(reservaDTO)
            .when()
                    .post("/api/restaurantes/{restauranteId}/reservarMesa", restauranteId)
            .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("path", equalTo("/api/restaurantes/1/reservarMesa"))
                    .body("message", equalTo("Não há lugares disponíveis nesse horário para o Restaurante: Restaurante Teste"));
        }
    }

    @Nested
    class findAll {

        @Test
        void deveRetornarTodasAsReservasComSucesso() {
            Long restauranteId = 1L;

            given()
            .when()
                    .get("/api/restaurantes/{restauranteId}/reservas", restauranteId)
            .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void deveLancarExceptionAoMandarRestauranteInexistente() {
            Long restauranteId = 0L;

            given()
                    .when()
                    .get("/api/restaurantes/{restauranteId}/reservas", restauranteId)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("path", equalTo("/api/restaurantes/0/reservas"))
                    .body("message", equalTo("Restaurante não existe"));
        }
    }


    @Nested
    class findById {

        @Test
        void deveRetornarReservaComSucesso() {
            Long reservaId = 1L;

            given()
            .when()
                    .get("/api/restaurantes/reserva/{reservaId}", reservaId)
            .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void deveLancarExceptionAoNaoEncontrarReservar() {
            Long reservaId = 0L;

            given()
                    .when()
                    .get("/api/restaurantes/reserva/{reservaId}", reservaId)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

    }


    @Nested
    class deleteById {

        @Test
        void deveExcluirReservaComSucesso() {
            Long reservaID = 1L;

            given()
            .when()
                    .delete("/api/restaurantes/reserva/{reservaId}", reservaID)
            .then()
                    .statusCode(HttpStatus.OK.value());
        }
    }
}
