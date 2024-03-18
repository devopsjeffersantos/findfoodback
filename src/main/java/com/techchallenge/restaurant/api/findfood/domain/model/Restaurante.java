package com.techchallenge.restaurant.api.findfood.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_restaurante")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private String localizacao;

    private String tipoCozinha;

    private String horarioFuncionamento;

    private int quantidadeTotalDeMesas;

}
