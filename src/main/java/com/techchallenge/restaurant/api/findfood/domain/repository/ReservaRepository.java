package com.techchallenge.restaurant.api.findfood.domain.repository;

import com.techchallenge.restaurant.api.findfood.domain.model.Reserva;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.restaurante = :restaurante " +
            "AND (r.dataHoraInicio BETWEEN :inicioReserva AND :fimReserva OR r.dataHoraFim BETWEEN :inicioReserva AND :fimReserva)")
    List<Reserva> findReservasNoIntervaloDaNovaReservaSolicitada(Restaurante restaurante, LocalDateTime inicioReserva, LocalDateTime fimReserva);

    List<Reserva> findAllByRestaurante(Restaurante restaurante);
}
