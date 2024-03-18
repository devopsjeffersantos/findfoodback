package com.techchallenge.restaurant.api.findfood.service;

import com.techchallenge.restaurant.api.findfood.api.model.ReservaDTO;
import com.techchallenge.restaurant.api.findfood.dados.ReservaDados;
import com.techchallenge.restaurant.api.findfood.domain.exception.NaoHaMesasDisponiveisException;
import com.techchallenge.restaurant.api.findfood.domain.repository.ReservaRepository;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import com.techchallenge.restaurant.api.findfood.domain.service.ReservaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ReservaServiceIntegrationTest extends ReservaDados {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ReservaService reservaService;

    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


    }


    @Test
    void deveReservarComSucesso() {
        var restaurante = criarRestauranteValido();
        var reserva = criarReservaDto();

        var restauranteSalvo = restauranteRepository.save(restaurante);

        var reservaArmazenada = reservaService.reservarMesa(restauranteSalvo.getId(), reserva);

        assertThat(reservaArmazenada)
                .isNotNull()
                .isInstanceOf(ReservaDTO.class);
        assertThat(reservaArmazenada.getId())
                .isNotNull();
    }

    @Test
    void deveBuscarReservaPeloIdComSucesso() {
        var restauranteId = 1L;
        var reserva = criarReservaDto();

        var reservaRealizada = reservaService.reservarMesa(restauranteId, reserva);

        var buscarReserva = reservaService.findById(reservaRealizada.getId());


        assertThat(buscarReserva)
                .isNotNull()
                .isInstanceOf(ReservaDTO.class);

    }

    @Test
    void deveLancarExceptionAoBuscarReservaPeloIdNaoEncontrado() {
        var reservaId = 0L;

        assertThatThrownBy(() -> reservaService.findById(reservaId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Reserva não encontrada!");
    }

    @Test
    void deveLancarExceptionAoTentarReservarMesaComRestauranteNaoEncontrado() {
        var restauranteId = 0L;
        var reservaDTO = criarReservaDto();

        assertThatThrownBy(() -> reservaService.reservarMesa(restauranteId, reservaDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurante não existe");
    }

    @Test
    void deveLancarExceptionAoTentarReservarMesaComRestauranteSemLugaresDisponiveis() {
        var restauranteId = 1L;
        var reservaDTO = criarReservaDto();
        reservaDTO.setQtdPessoas(4000);



            assertThatThrownBy(() -> reservaService.reservarMesa(restauranteId, reservaDTO))
                    .isInstanceOf(NaoHaMesasDisponiveisException.class)
                    .hasMessage("Não há lugares disponíveis nesse horário para o Restaurante: Restaurante Teste");



    }

    @Test
    void deveLancarExceptionAoTentarReservarMesaComRestauranteSemHorarioDisponivel() {
        var restauranteId = 1L;
        var reservaDTO = criarReservaDto();
        reservaDTO.setQtdPessoas(200);
        reservaDTO.setDataHoraInicio(LocalDateTime.now());
        reservaDTO.setDataHoraFim(LocalDateTime.now().plusHours(2));

            assertThatThrownBy(() -> reservaService.reservarMesa(restauranteId, reservaDTO))
                    .isInstanceOf(NaoHaMesasDisponiveisException.class)
                    .hasMessage("Não há lugares disponíveis nesse horário para o Restaurante: Restaurante Teste");




    }

    @Test
    @Order(2)
    void deveRetornarTodasAsReservasComSucesso() {
        var restauranteId = 1L;
        var reserva = criarReservaDto();

        var reservaRealizada = reservaService.reservarMesa(restauranteId, reserva);

        var result = reservaService.findAll(restauranteId);

        assertThat(result).isNotEmpty();
    }

    @Test
    void deveLancarExceptionAoTentarReservarMesaEmRestauranteQueNaoExiste() {
        var restauranteId = 0L;

        assertThatThrownBy(() -> reservaService.findAll(restauranteId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurante não existe");

    }

}