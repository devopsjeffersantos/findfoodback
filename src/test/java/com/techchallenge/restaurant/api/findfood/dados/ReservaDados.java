package com.techchallenge.restaurant.api.findfood.dados;

import com.techchallenge.restaurant.api.findfood.api.model.ReservaDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Reserva;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;

import java.time.LocalDateTime;

public class ReservaDados {

    public ReservaDTO criarReservaDto(){
        return ReservaDTO.builder()
                .id(1L)
                .nomeCliente("João da Silva")
                .emailCliente("joao.silva@gmail.com")
                .dataHoraInicio(LocalDateTime.now())
                .dataHoraFim(LocalDateTime.now().plusHours(2))
                .telefoneCliente("15 99332-3456")
                .qtdPessoas(5)
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

    public Reserva criarReserva(){
        return Reserva.builder()
                .id(1L)
                .nomeCliente("João da Silva")
                .emailCliente("joao.silva@gmail.com")
                .dataHoraInicio(LocalDateTime.now())
                .dataHoraFim(LocalDateTime.now().plusHours(2))
                .telefoneCliente("15 99332-3456")
                .qtdPessoas(5)
                .build();
    }

    public Reserva criarReservaSemLugaresDisponiveis() {
        var reserva = criarReserva();
        reserva.setQtdPessoas(1000);
        return reserva;
    }

}
