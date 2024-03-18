package com.techchallenge.restaurant.api.findfood.api.controller;

import com.techchallenge.restaurant.api.findfood.api.model.AvaliacaoDTO;
import com.techchallenge.restaurant.api.findfood.domain.service.AvaliacaoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@RestController
@RequestMapping("/api/restaurantes")
@AllArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService service;

    @PostMapping("/{restauranteId}/avaliar")
    public ResponseEntity<String> registrarAvaliacao(@PathVariable Long restauranteId, @RequestBody AvaliacaoDTO avaliacaoDTO) {
        service.registrarAvaliacao(restauranteId, avaliacaoDTO);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("Avaliação feita com sucesso");
    }

    @GetMapping("/{restauranteId}/avaliacoes")
    public ResponseEntity<Collection<AvaliacaoDTO>> findAll(@PathVariable Long restauranteId){
        return ResponseEntity.ok(service.findAll(restauranteId));
    }

}
