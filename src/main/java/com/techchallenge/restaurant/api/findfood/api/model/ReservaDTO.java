package com.techchallenge.restaurant.api.findfood.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dataHoraInicio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dataHoraFim;

    private Integer qtdPessoas;

    private String nomeCliente;

    private String emailCliente;

    private String telefoneCliente;

}
