package com.techchallenge.restaurant.api.findfood.api.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoDTO {

    @NotEmpty(message = "A pontuação não foi preenchida")
    private Integer pontuacao;

    @NotEmpty(message = "O comentário não foi preenchido")
    private String comentario;

    @NotEmpty(message = "O Nome do Cliente não foi preenchido")
    private String nomeCliente;

    private String emailCliente;

    private String telefoneCliente;
}