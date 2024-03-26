package com.techchallenge.restaurant.api.findfood.dados;

import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Avaliacao;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;

public class RestauranteDados {

    public static RestauranteDTO criarRestauranteDtoValido() {
        return RestauranteDTO.builder()
            .nome("Restaurante Teste")
            .localizacao("São Paulo, SP")
            .horarioFuncionamento("10:00 às 22:00")
            .tipoCozinha("Italiana")
            .quantidadeTotalDeMesas(20)
            .build();
    }

    public Restaurante criarRestauranteValido() {
        return Restaurante.builder()
                .id(1L)
                .nome("Restaurante Teste")
                .localizacao("São Paulo, SP")
                .horarioFuncionamento("10:00 às 22:00")
                .tipoCozinha("Italiana")
                .quantidadeTotalDeMesas(20)
                .build();
    }

}
