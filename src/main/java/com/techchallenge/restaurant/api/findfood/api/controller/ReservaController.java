package com.techchallenge.restaurant.api.findfood.api.controller;

import com.techchallenge.restaurant.api.findfood.api.model.ReservaDTO;
import com.techchallenge.restaurant.api.findfood.domain.service.ReservaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/api/restaurantes")
@AllArgsConstructor
public class ReservaController {

    private final ReservaService service;

    @PostMapping("/{restauranteId}/reservarMesa")
    public ResponseEntity<ReservaDTO> reservarMesa(@PathVariable Long restauranteId, @RequestBody ReservaDTO reservaDTO) {
        var reservarMesa = service.reservarMesa(restauranteId, reservaDTO);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(reservarMesa);
    }

    @GetMapping("/{restauranteId}/reservas")
    public ResponseEntity<Collection<ReservaDTO>> findAll(@PathVariable Long restauranteId) {
        var reservas = service.findAll(restauranteId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<ReservaDTO> findById(@PathVariable Long reservaId) {
        var reserva = service.findById(reservaId);
        return ResponseEntity.ok(reserva);
    }

    @DeleteMapping("/reserva/{reservaId}")
    public ResponseEntity<String> excluirReserva(@PathVariable Long reservaId) {
        service.delete(reservaId);
        return ResponseEntity.ok().body("Reserva excluída com sucesso");
    }

}
