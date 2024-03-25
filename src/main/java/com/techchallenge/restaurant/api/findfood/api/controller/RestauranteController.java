package com.techchallenge.restaurant.api.findfood.api.controller;

import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import com.techchallenge.restaurant.api.findfood.domain.service.RestauranteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@AllArgsConstructor
public class RestauranteController {

    private final RestauranteService service;

    @PostMapping("/registrarRestaurante")
    public ResponseEntity<String> save(@RequestBody RestauranteDTO restauranteDTO) {
        service.registrarRestaurante(restauranteDTO);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("Restaurante Registrado com Sucesso");
    }

    @GetMapping("/pesquisa")
    public ResponseEntity<List<Restaurante>> findRestaurantes(@RequestParam(name = "nome", required = false, defaultValue = "") String nome,
                                                              @RequestParam(name = "localizacao", required = false, defaultValue = "") String localizacao,
                                                              @RequestParam(name = "tipoCozinha", required = false, defaultValue = "") String tipoCozinha) {
        var restaurante = service.buscarRestaurantePor(nome, localizacao, tipoCozinha);
        if(restaurante.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(restaurante);
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Restaurante>> findAll() {
        var restaurantes = service.buscarTodosRestaurantes();
        if(restaurantes.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(restaurantes);
        }
    }

    @PutMapping("/atualizar/{restauranteId}")
    public ResponseEntity<RestauranteDTO> update(@PathVariable Long restauranteId, @RequestBody RestauranteDTO restauranteDTO) {
        restauranteDTO = service.atualizarRestaurante(restauranteId, restauranteDTO);
        return ResponseEntity.ok(restauranteDTO);
    }

    @DeleteMapping("/deletar/{restauranteId}")
    public ResponseEntity<String> delete(@PathVariable Long restauranteId) {
        service.deletarRestaurante(restauranteId);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Restaurante Deletado com Sucesso");
    }
}
