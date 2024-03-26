package com.techchallenge.restaurant.api.findfood.api.controller;

import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import com.techchallenge.restaurant.api.findfood.domain.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@AllArgsConstructor
@Tag(name = "Gerenciamento de Restaurantes", description = "Endpoints para cadastro e pesquisa de restaurantes")
public class RestauranteController {

    private final RestauranteService service;

    @PostMapping("/registrarRestaurante")
    @Operation(summary = "Registrar Restaurante", description = "Registra um novo restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<String> save(@RequestBody RestauranteDTO restauranteDTO) {
        service.registrarRestaurante(restauranteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Restaurante Registrado com Sucesso");
    }

    @GetMapping("/pesquisa")
    @Operation(summary = "Pesquisar Restaurantes", description = "Pesquisa restaurantes por nome, localização e tipo de cozinha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado")
    })
    public ResponseEntity<List<Restaurante>> findRestaurantes(
            @Parameter(description = "Nome do restaurante") @RequestParam(name = "nome", required = false, defaultValue = "") String nome,
            @Parameter(description = "Localização do restaurante") @RequestParam(name = "localizacao", required = false, defaultValue = "") String localizacao,
            @Parameter(description = "Tipo de cozinha do restaurante") @RequestParam(name = "tipoCozinha", required = false, defaultValue = "") String tipoCozinha) {
        var restaurantes = service.buscarRestaurantePor(nome, localizacao, tipoCozinha);
        if (restaurantes.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(restaurantes);
        }
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar Todos os Restaurantes", description = "Lista todos os restaurantes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante cadastrado")
    })
    public ResponseEntity<List<Restaurante>> findAll() {
        var restaurantes = service.buscarTodosRestaurantes();
        if (restaurantes.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(restaurantes);
        }
    }

    @PutMapping("/atualizar/{restauranteId}")
    @Operation(summary = "Atualizar Restaurante", description = "Atualiza os dados de um restaurante existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteDTO> update(@PathVariable Long restauranteId, @RequestBody RestauranteDTO restauranteDTO) {
        restauranteDTO = service.atualizarRestaurante(restauranteId, restauranteDTO);
        return ResponseEntity.ok(restauranteDTO);
    }

    @DeleteMapping("/deletar/{restauranteId}")
    @Operation(summary = "Deletar Restaurante", description = "Exclui um restaurante pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long restauranteId) {
        try {
            service.deletarRestaurante(restauranteId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Logar a exceção e retornar um código de erro genérico (ex: 500)
            return ResponseEntity.notFound().build();
        }
    }
}