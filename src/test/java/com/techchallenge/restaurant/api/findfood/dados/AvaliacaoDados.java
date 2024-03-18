package com.techchallenge.restaurant.api.findfood.dados;

import com.techchallenge.restaurant.api.findfood.api.model.AvaliacaoDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Avaliacao;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;

public class AvaliacaoDados {

    public Restaurante criarRestaurante(){
        return Restaurante.builder()
                .id(1L)
                .nome("Outback São Paulo")
                .localizacao("Avenida Paulista")
                .horarioFuncionamento("18h00 Até 23h00")
                .tipoCozinha("Carnes")
                .build();
    }

    public AvaliacaoDTO criarAvaliacaoDto(){
        return AvaliacaoDTO.builder()
                .emailCliente("alberto@gmail.com")
                .nomeCliente("Alberto Torres")
                .pontuacao(4)
                .telefoneCliente("15 99331-2345")
                .comentario("Restaurante bom, boa estrutura.")
                .build();
    }

    public Avaliacao criarAvaliacao(){
        return Avaliacao.builder()
                .emailCliente("alberto@gmail.com")
                .nomeCliente("Alberto Torres")
                .pontuacao(4)
                .telefoneCliente("15 99331-2345")
                .comentario("Restaurante bom, boa estrutura.")
                .id(1L)
                .restaurante(criarRestaurante())
                .build();
    }

    public Avaliacao criarAvaliacaoComPontuacaoInvalidaAcima5(){
        var avaliacao = criarAvaliacao();
        avaliacao.setPontuacao(6);
        return avaliacao;
    }

    public Avaliacao criarAvaliacaoComPontuacaoInvalidaAbaixo0(){
        var avaliacao = criarAvaliacao();
        avaliacao.setPontuacao(-1);
        return avaliacao;
    }
}
