package com.techchallenge.restaurant.api.findfood.api.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteDTO {

    private Long id;

    @NotEmpty(message = "O nome não foi preenchido")
    private String nome;

    @NotEmpty(message = "A localizacao não foi preenchida")
    private String localizacao;

    @NotEmpty(message = "O tipoCozinha não foi preenchido")
    private String tipoCozinha;

    @NotEmpty(message = "O horarioFuncionamento não foi preenchido")
    private String horarioFuncionamento;

    @NotEmpty(message = "A quantidadeTotalDeMesas não foi preenchida")
    private Integer quantidadeTotalDeMesas;

}

