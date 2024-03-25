package com.techchallenge.restaurant.api.findfood.api.controller;

import com.techchallenge.restaurant.api.findfood.api.model.AvaliacaoDTO;
import com.techchallenge.restaurant.api.findfood.domain.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/restaurantes")
@AllArgsConstructor
@Tag(name = "Gerenciamento de Avaliações", description = "Endpoints para avaliações de restaurantes")
public class AvaliacaoController {

    private final AvaliacaoService service;

    @PostMapping("/{restauranteId}/avaliar")
    @Operation(summary = "Registrar Avaliação", description = "Registra uma nova avaliação para um restaurante específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<String> registrarAvaliacao(@Parameter(description = "ID do restaurante") @PathVariable Long restauranteId, @RequestBody AvaliacaoDTO avaliacaoDTO) {
        service.registrarAvaliacao(restauranteId, avaliacaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Avaliação feita com sucesso");
    }

    @GetMapping("/{restauranteId}/avaliacoes")
    @Operation(summary = "Listar Avaliações", description = "Lista todas as avaliações de um restaurante específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliações encontradas"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<Collection<AvaliacaoDTO>> findAll(@Parameter(description = "ID do restaurante") @PathVariable Long restauranteId) {
        return ResponseEntity.ok(service.findAll(restauranteId));
    }
}