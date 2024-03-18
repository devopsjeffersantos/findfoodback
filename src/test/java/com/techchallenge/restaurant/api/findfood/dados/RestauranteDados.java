package com.techchallenge.restaurant.api.findfood.dados;

import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;

public class RestauranteDados {

    public static RestauranteDTO criarRestauranteDtoValido() {
        return RestauranteDTO.builder()
            .id(1L)
            .nome("Restaurante Teste")
            .localizacao("São Paulo, SP")
            .horarioFuncionamento("10:00 às 22:00")
            .tipoCozinha("Italiana")
            .quantidadeTotalDeMesas(20)
            .build();
    }

    public static Restaurante criarRestauranteValido() {
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
