package com.techchallenge.restaurant.api.findfood.api.controller;

import com.techchallenge.restaurant.api.findfood.api.model.ReservaDTO;
import com.techchallenge.restaurant.api.findfood.domain.service.ReservaService;
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
@Tag(name = "Gerenciamento de Reservas", description = "Endpoints para reservas de mesa em restaurantes")
public class ReservaController {

    private final ReservaService service;

    @PostMapping("/{restauranteId}/reservarMesa")
    @Operation(summary = "Reservar Mesa", description = "Registra uma nova reserva de mesa para um restaurante específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva de mesa registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado/Não há mesas disponíveis para reserva")
    })
    public ResponseEntity<ReservaDTO> reservarMesa(@Parameter(description = "ID do restaurante") @PathVariable Long restauranteId, @RequestBody ReservaDTO reservaDTO) {
        var reservarMesa = service.reservarMesa(restauranteId, reservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservarMesa);
    }

    @GetMapping("/{restauranteId}/reservas")
    @Operation(summary = "Listar Reservas", description = "Lista todas as reservas de um restaurante específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<Collection<ReservaDTO>> findAll(@Parameter(description = "ID do restaurante") @PathVariable Long restauranteId) {
        var reservas = service.findAll(restauranteId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/reserva/{reservaId}")
    @Operation(summary = "Buscar Reserva por ID", description = "Busca uma reserva pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public ResponseEntity<ReservaDTO> findById(@Parameter(description = "ID da reserva") @PathVariable Long reservaId) {
        var reserva = service.findById(reservaId);
        return ResponseEntity.ok(reserva);
    }

    @DeleteMapping("/reserva/{reservaId}")
    @Operation(summary = "Excluir Reserva", description = "Exclui uma reserva pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public ResponseEntity<String> excluirReserva(@Parameter(description = "ID da reserva") @PathVariable Long reservaId) {
        service.delete(reservaId);
        return ResponseEntity.ok().body("Reserva excluída com sucesso");
    }
}