package com.techchallenge.restaurant.api.findfood.controller;

import com.techchallenge.restaurant.api.findfood.api.model.AvaliacaoDTO;
import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import static com.techchallenge.restaurant.api.findfood.dados.RestauranteDados.criarRestauranteDtoValido;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class RestauranteControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("Testes de Registro de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class registrarRestaurante {
        @Test
        @Order(1)
        void devePermitirRegistrarRestaurante() {

            // Arrange
            Long restauranteId = 1L;
            RestauranteDTO restauranteDTO = criarRestauranteDtoValido();

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteDTO)

            // Act
            .when()
                .post("/api/restaurantes/registrarRestaurante")

            // Assert
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(equalTo("Restaurante Registrado com Sucesso"));

        }

        @Test
        @Order(2)
        void deveLancarExcecaoAoSalvarRestauranteComNomeVazio() {

            // Arrange
            Long restauranteId = 1L;
            RestauranteDTO restauranteDTO = criarRestauranteDtoValido();
            restauranteDTO.setNome("");

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteDTO)

            // Act
            .when()
                .post("/api/restaurantes/registrarRestaurante")

            // Assert
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE) // Verifica o tipo de conteúdo JSON
                .body("timestamp", notNullValue())
                .body("status", equalTo(400))
                .body("message", equalTo("Inconsistencia nos campos informados."))
                .body("path", equalTo("/api/restaurantes/registrarRestaurante"));
        }
    }

    @Nested
    @DisplayName("Testes de Atualização de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class atualizarRestaurante {
        @Test
        @Order(1)
        void devePermitirAtualizarRestaurantes() {

            // Arrange
            Long restauranteId = 1L;
            RestauranteDTO restauranteDTO = criarRestauranteDtoValido();

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteDTO)

            // Act
            .when()
                .put("/api/restaurantes/atualizar/{restauranteId}", restauranteId)

            // Assert
            .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("nome", equalTo(restauranteDTO.getNome()))
                .body("localizacao", equalTo(restauranteDTO.getLocalizacao()))
                .body("tipoCozinha", equalTo(restauranteDTO.getTipoCozinha()))
                .body("horarioFuncionamento", equalTo(restauranteDTO.getHorarioFuncionamento()))
                .body("quantidadeTotalDeMesas", equalTo(restauranteDTO.getQuantidadeTotalDeMesas()));
        }

        @Test
        @Order(2)
        void deveLancarExcecaoAoAtualizarRestauranteInexistente() {

            // Arrange
            Long restauranteId = 100L;
            RestauranteDTO restauranteDTO = criarRestauranteDtoValido();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(restauranteDTO)

            // Act
            .when()
                .put("/api/restaurantes/atualizar/{restauranteId}", restauranteId)

            // Assert
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE) // Verifica o tipo de conteúdo JSON
                .body("timestamp", notNullValue())
                .body("status", equalTo(404))
                .body("message", equalTo("Restaurante não foi encontrado"))
                .body("path", equalTo("/api/restaurantes/atualizar/100"));

        }
    }

    @Nested
    @DisplayName("Testes de Exclusão de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class deletarRestaurante {
        @Test
        @Order(1)
        @Sql(scripts = "/delete.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void devePermitirDeletarRestaurantes() {

            // Arrange
            Long restauranteId = 1L;
            RestauranteDTO restauranteDTO = criarRestauranteDtoValido();

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteDTO)

                // Act
            .when()
                .delete("/api/restaurantes/deletar/{restauranteId}", restauranteId)

                // Assert
            .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/plain;charset=UTF-8") // Verifica o tipo de conteúdo JSON
                .body(equalTo("Restaurante Deletado com Sucesso"));
        }

        @Test
        @Order(2)
        void deveLancarExcecaoAoDeletarRestauranteInexistente() {

            // Arrange
            Long restauranteId = 100L;
            RestauranteDTO restauranteDTO = criarRestauranteDtoValido();

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteDTO)

            // Act
            .when()
                .delete("/api/restaurantes/deletar/{restauranteId}", restauranteId)

            // Assert
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE) // Verifica o tipo de conteúdo JSON
                .body("timestamp", notNullValue())
                .body("status", equalTo(404))
                .body("message", equalTo("Restaurante com ID '100' não foi encontrado para exclusão."))
                .body("path", equalTo("/api/restaurantes/deletar/100"));

        }
    }

    @Nested
    @DisplayName("Testes de Busca de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class buscarRestaurante {
        @Test
        @Order(1)
        void devePermitirBuscarRestaurantePorNome() throws InterruptedException {

            // Arrange
            String restauranteNome = "Restaurante Teste";

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteNome)

            // Act
            .when()
                .get("/api/restaurantes/pesquisa")

            // Assert
            .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("[0].nome", equalTo(restauranteNome))
                .body("[0].localizacao", equalTo("São Paulo"))
                .body("[0].tipoCozinha", equalTo("Carnes e Churrasco"))
                .body("[0].horarioFuncionamento", equalTo("10h00 até 22h00"))
                .body("[0].quantidadeTotalDeMesas", equalTo(20));
        }

        @Test
        @Order(2)
        void devePermitirBuscarRestaurantePorLocalizacao() throws InterruptedException {

            // Arrange
            String restauranteLocalizacao = "São Paulo";

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteLocalizacao)

            // Act
            .when()
                .get("/api/restaurantes/pesquisa")

            // Assert
            .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("[0].nome", equalTo("Restaurante Teste"))
                .body("[0].localizacao", equalTo("São Paulo"))
                .body("[0].tipoCozinha", equalTo("Carnes e Churrasco"))
                .body("[0].horarioFuncionamento", equalTo("10h00 até 22h00"))
                .body("[0].quantidadeTotalDeMesas", equalTo(20));

        }

        @Test
        @Order(3)
        void devePermitirBuscarRestaurantePorTipoCozinha() throws InterruptedException {

            // Arrange
            String restauranteTipoCozinha = "Carnes e Churrasco";

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(restauranteTipoCozinha)

            // Act
            .when()
                .get("/api/restaurantes/pesquisa")

            // Assert
            .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("[0].nome", equalTo("Restaurante Teste"))
                .body("[0].localizacao", equalTo("São Paulo"))
                .body("[0].tipoCozinha", equalTo("Carnes e Churrasco"))
                .body("[0].horarioFuncionamento", equalTo("10h00 até 22h00"))
                .body("[0].quantidadeTotalDeMesas", equalTo(20));
        }

        @Test
        @Order(5)
        void devePermitirBuscarTodosRestaurantes() {

            // Arrange
            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)

            // Act
            .when()
                .get("/api/restaurantes/todos")

            // Assert
            .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("[0].nome", equalTo("Restaurante Teste"))
                .body("[0].localizacao", equalTo("São Paulo"))
                .body("[0].tipoCozinha", equalTo("Carnes e Churrasco"))
                .body("[0].horarioFuncionamento", equalTo("10h00 até 22h00"))
                .body("[0].quantidadeTotalDeMesas", equalTo(20));

        }

        @Test
        @Order(6)
        void deveLancarExcecaoAoBuscarNomeRestauranteInexistente() {

            // Arrange
            String restauranteNome = "lalalala";

            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("nome", restauranteNome)
                .param("localizacao", StringUtils.EMPTY)
                .param("tipoCozinha", StringUtils.EMPTY)

            // Act
            .when()
                .get("/api/restaurantes/pesquisa")

            // Assert
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("timestamp", notNullValue())
                .body("status", equalTo(404))
                .body("message", equalTo("Restaurante com nome '"+restauranteNome+"' não foi encontrado."))
                .body("path", equalTo("/api/restaurantes/pesquisa"));

        }

        @Test
        @Order(7)
        void deveLancarExcecaoAoBuscarLocalizacaoRestauranteInexistente() {

            // Arrange
            String restauranteLocalizacao = "Lalalala";

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("nome", StringUtils.EMPTY)
                    .param("localizacao", restauranteLocalizacao)
                    .param("tipoCozinha", StringUtils.EMPTY)

                    // Act
                    .when()
                    .get("/api/restaurantes/pesquisa")

                    // Assert
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("timestamp", notNullValue())
                    .body("status", equalTo(404))
                    .body("message", equalTo("Restaurante com localização '"+restauranteLocalizacao+"' não foi encontrado."))
                    .body("path", equalTo("/api/restaurantes/pesquisa"));

        }

        @Test
        @Order(8)
        void deveLancarExcecaoAoBuscarTipoCozinhaRestauranteInexistente() {

            // Arrange
            String restauranteTipoCozinha = "Lalalala";

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("nome", StringUtils.EMPTY)
                    .param("localizacao", StringUtils.EMPTY)
                    .param("tipoCozinha", restauranteTipoCozinha)

                    // Act
                    .when()
                    .get("/api/restaurantes/pesquisa")

                    // Assert
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body("timestamp", notNullValue())
                    .body("status", equalTo(404))
                    .body("message", equalTo("Restaurante com tipo de cozinha '"+restauranteTipoCozinha+"' não foi encontrado."))
                    .body("path", equalTo("/api/restaurantes/pesquisa"));

        }

        @Test
        @Order(9)
        @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Executa um script SQL para limpar as tabelas antes do método de teste
        void deveLancarExcecaoAoBuscarTodosRestaurantes() {

            // Arrange
            given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            // Act
            .when()
                .get("/api/restaurantes/todos")
            // Assert
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE) // Verifica o tipo de conteúdo JSON
                .body("timestamp", notNullValue())
                .body("status", equalTo(404))
                .body("message", equalTo("Nenhum Restaurante Cadastrado"))
                .body("path", equalTo("/api/restaurantes/todos"));


        }
    }

}
