package com.techchallenge.restaurant.api.findfood.service;

import com.techchallenge.restaurant.api.findfood.dados.ReservaDados;
import com.techchallenge.restaurant.api.findfood.domain.exception.NaoHaMesasDisponiveisException;
import com.techchallenge.restaurant.api.findfood.domain.model.Reserva;
import com.techchallenge.restaurant.api.findfood.domain.repository.ReservaRepository;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import com.techchallenge.restaurant.api.findfood.domain.service.ReservaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServiceTest extends ReservaDados {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void deveReservarMesaComRestauranteComSucesso() {
        var restauranteId = 1L;
        var reservaDTO = criarReservaDto();
        var restaurante = criarRestauranteValido();

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
        when(modelMapper.map(reservaDTO, Reserva.class)).thenReturn(criarReserva());

        reservaService.reservarMesa(restauranteId, reservaDTO);

        verify(reservaRepository, times(1)).save(any());

    }

    @Test
    void deveLancarExceptionAoTentarReservarMesaComRestauranteNaoEncontrado() {
        var restauranteId = 1L;
        var reservaDTO = criarReservaDto();

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reservaService.reservarMesa(restauranteId, reservaDTO));

        verify(reservaRepository, never()).save(any());
    }

    @Test
    void deveBuscarTodasAsReservasPorRestauranteComSucesso() {
        var restauranteId = 1L;
        var restaurante = criarRestauranteValido();

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
        when(reservaRepository.findAll()).thenReturn(Collections.emptyList());

        var lista = reservaService.findAll(restauranteId);

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }


    @Test
    void deveRemoverReservaComSucesso() {
        Long reservaId = 1L;

        reservaService.delete(reservaId);

        verify(reservaRepository, times(1)).deleteById(reservaId);
    }


    @Test
    void deveLancarExceptionAoTentarReservarMesaSemLugaresDisponiveis() {
        var restauranteId = 1L;
        var reservaDTO = criarReservaDto();
        var restaurante = criarRestauranteValido();

        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
        when(modelMapper.map(reservaDTO, Reserva.class)).thenReturn(criarReservaSemLugaresDisponiveis());

        assertThrows(NaoHaMesasDisponiveisException.class, () -> reservaService.reservarMesa(restauranteId, reservaDTO));

    }

    @Test
    void deveBuscarReservaPeloIdComSucesso() {
        Long reservaId = 1L;
        var restauranteId = 1L;
        var reservaDTO = criarReservaDto();
        var reserva = criarReserva();

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        when(modelMapper.map(reservaDTO, Reserva.class)).thenReturn(criarReserva());

        var reservaRealizada = reservaService.findById(reservaId);

        verify(reservaRepository, times(1)).findById(reservaId);

    }


}
