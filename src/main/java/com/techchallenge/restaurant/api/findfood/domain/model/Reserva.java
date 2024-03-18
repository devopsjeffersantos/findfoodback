package com.techchallenge.restaurant.api.findfood.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_reserva")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime dataHoraInicio;

    private LocalDateTime dataHoraFim;

    private Integer qtdPessoas;

    @ManyToOne
    private Restaurante restaurante;

    private String nomeCliente;

    private String emailCliente;

    private String telefoneCliente;

}
