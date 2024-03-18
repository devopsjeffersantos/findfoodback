package com.techchallenge.restaurant.api.findfood.service;

import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import com.techchallenge.restaurant.api.findfood.domain.service.RestauranteServiceImpl;
import com.techchallenge.restaurant.api.findfood.dados.RestauranteDados;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RestauranteServiceTest {
    private final ModelMapper modelMapper = new ModelMapper();
    @Mock
    private RestauranteRepository restauranteRepository;
    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    AutoCloseable mock;
    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        restauranteService = new RestauranteServiceImpl(restauranteRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void devePermitirRegistrarRestaurante(){

        // Arrange
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();
        Restaurante restaurante = modelMapper.map(restauranteDto, Restaurante.class);  // Use the initialized modelMapper
        when(restauranteRepository.save(restaurante)).thenReturn(restaurante);

        //Act
        var restauranteRegistrado = restauranteService.save(restauranteDto);

        // Assert
        verify(restauranteRepository).save(eq(restaurante));
        verify(restauranteRepository, times(1)).save(restaurante);
        assertThat(restauranteRegistrado.getId()).isEqualTo(RestauranteDados.criarRestauranteDtoValido().getId());
        assertThat(restauranteRegistrado.getNome()).isEqualTo(RestauranteDados.criarRestauranteDtoValido().getNome());
        assertThat(restauranteRegistrado.getTipoCozinha()).isEqualTo(RestauranteDados.criarRestauranteDtoValido().getTipoCozinha());
        assertThat(restauranteRegistrado.getHorarioFuncionamento()).isEqualTo(RestauranteDados.criarRestauranteDtoValido().getHorarioFuncionamento());
        assertThat(restauranteRegistrado.getLocalizacao()).isEqualTo(RestauranteDados.criarRestauranteDtoValido().getLocalizacao());
        assertThat(restauranteRegistrado.getQuantidadeTotalDeMesas()).isEqualTo(RestauranteDados.criarRestauranteDtoValido().getQuantidadeTotalDeMesas());

    }
    @Test
    void devePermitirBuscarRestaurantePorNome() {

        // Arrange
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();
        Restaurante restauranteSalvo = modelMapper.map(restauranteDto, Restaurante.class);


        // Simular a inserção do restaurante no banco de dados
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteSalvo);

        // Simular a busca pelo nome do restaurante
        when(restauranteRepository.findByNomeIgnoreCaseLikeOrLocalizacaoIgnoreCaseLikeOrTipoCozinhaIgnoreCaseLike(eq(restauranteSalvo.getNome()), any(), any()))
                .thenReturn(Collections.singletonList(restauranteSalvo));

        // Act
        List<Restaurante> actualRestaurants = restauranteService.findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(restauranteSalvo.getNome(), null, null);

        // Assert
        assertThat(actualRestaurants).isNotEmpty();
        assertThat(actualRestaurants).hasSize(1);
        assertThat(actualRestaurants.get(0).getNome()).isEqualTo(restauranteSalvo.getNome());
    }

    @Test
    void devePermitirBuscarRestaurantePorLocalizacao(){

        // Arrange
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();
        Restaurante restauranteSalvo = modelMapper.map(restauranteDto, Restaurante.class);

        // Simular a inserção do restaurante no banco de dados
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteSalvo);


        // Simular a busca pelo nome do restaurante
        when(restauranteRepository.findByNomeIgnoreCaseLikeOrLocalizacaoIgnoreCaseLikeOrTipoCozinhaIgnoreCaseLike(any(), eq(restauranteSalvo.getLocalizacao()), any()))
                .thenReturn(Collections.singletonList(restauranteSalvo));

        // Act
        List<Restaurante> actualRestaurants = restauranteService.findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(null, restauranteSalvo.getLocalizacao(), null);

        // Assert
        assertThat(actualRestaurants).isNotEmpty();
        assertThat(actualRestaurants).hasSize(1);
        assertThat(actualRestaurants.get(0).getLocalizacao()).isEqualTo(restauranteSalvo.getLocalizacao());
    }

    @Test
    void devePermitirBuscarRestaurantePorTipoCozinha(){

        // Arrange
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();
        Restaurante restauranteSalvo = modelMapper.map(restauranteDto, Restaurante.class);

        // Simular a inserção do restaurante no banco de dados
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteSalvo);

        // Simular a busca pelo nome do restaurante
        when(restauranteRepository.findByNomeIgnoreCaseLikeOrLocalizacaoIgnoreCaseLikeOrTipoCozinhaIgnoreCaseLike(any(), any(), eq(restauranteSalvo.getTipoCozinha())))
                .thenReturn(Collections.singletonList(restauranteSalvo));

        // Act
        List<Restaurante> actualRestaurants = restauranteService.findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(null, null, restauranteSalvo.getTipoCozinha());

        // Assert
        assertThat(actualRestaurants).isNotEmpty();
        assertThat(actualRestaurants).hasSize(1);
        assertThat(actualRestaurants.get(0).getTipoCozinha()).isEqualTo(restauranteSalvo.getTipoCozinha());

    }

    @Test
    void devePermitirBuscarRestaurantesPorId(){

        // Arrange
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();
        Restaurante restauranteSalvo = modelMapper.map(restauranteDto, Restaurante.class);

        // Simular a inserção do restaurante no banco de dados
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteSalvo);

        // Simular a busca pelo nome do restaurante
        when(restauranteRepository.findById(restauranteSalvo.getId())).thenReturn(Optional.of(restauranteSalvo));

        // Act
        Optional<RestauranteDTO> actualRestaurants = restauranteService.findById(restauranteSalvo.getId());

        // Assert
        assertThat(actualRestaurants).isNotEmpty();
        assertThat(actualRestaurants).isNotNull();
    }
    @Test
    void devePermitirBuscarTodosRestaurantes(){

        // Arrange
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();
        Restaurante restauranteSalvo = modelMapper.map(restauranteDto, Restaurante.class);

        // Simular a inserção do restaurante no banco de dados
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteSalvo);

        // Simular a busca pelo nome do restaurante
        when(restauranteRepository.findAll()).thenReturn(Collections.singletonList(restauranteSalvo));

        // Act
        List<RestauranteDTO> actualRestaurants = (List<RestauranteDTO>) restauranteService.findAll();

        // Assert
        assertThat(actualRestaurants).isNotEmpty();
        assertThat(actualRestaurants).hasSize(1);
    }
    @Test
    void devePermitirAtualizarRestaurantes(){

        // Arrange
        Long restauranteId = 1L;
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();
        Restaurante restauranteSalvo = modelMapper.map(restauranteDto, Restaurante.class);

        // Simular a inserção do restaurante no banco de dados
        when(restauranteRepository.save(restauranteSalvo)).thenReturn(restauranteSalvo);

        // Simular a alteração pelo nome do restaurante
        when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restauranteSalvo));

        // Act
        RestauranteDTO restauranteAtualizado = restauranteService.update(restauranteId, restauranteDto);

        // Assert
        assertThat(restauranteAtualizado).isNotNull();
        assertThat(restauranteAtualizado.getId()).isEqualTo(restauranteId);
        assertThat(restauranteAtualizado.getNome()).isEqualTo(restauranteDto.getNome());
    }
    @Test
    void devePermitirDeletarRestaurantes() {

        // Arrange
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();
        Restaurante restauranteSalvo = modelMapper.map(restauranteDto, Restaurante.class);

        // Simular a inserção do restaurante no banco de dados
        when(restauranteRepository.save(restauranteSalvo)).thenReturn(restauranteSalvo);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restauranteSalvo));

        // Act
        RestauranteDTO restauranteSalvoDTO = restauranteService.save(restauranteDto);
        doNothing().when(restauranteRepository).deleteById(restauranteSalvo.getId());

        restauranteService.delete(restauranteSalvoDTO.getId());

        // Assert
        verify(restauranteRepository, times(1)).deleteById(restauranteSalvo.getId());
    }

    @Test
    void deveLancarExcecaoAoSalvarRestauranteComNomeVazio() {

        // Arrange
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();
        Restaurante restaurante = modelMapper.map(restauranteDto, Restaurante.class);
        restaurante.setNome("");
        when(restauranteRepository.save(restaurante)).thenThrow(DataIntegrityViolationException.class);

        // Act
        try {
            restauranteService.save(restauranteDto);
            fail("Deveria ter lançado uma exceção");
        } catch (Exception e) {
            // Assert
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e.getMessage()).contains("source cannot be null");
        }
    }

    @Test
    void deveLancarExcecaoAoBuscarNomeRestauranteInexistente() {

        // Arrange
        RestauranteDTO restauranteSalvo = RestauranteDados.criarRestauranteDtoValido();
        when(restauranteRepository.findByNomeIgnoreCaseLikeOrLocalizacaoIgnoreCaseLikeOrTipoCozinhaIgnoreCaseLike(eq(restauranteSalvo.getNome()), any(), any())).thenReturn(Collections.emptyList());

        // Act
        try {
            List<Restaurante> actualRestaurants = restauranteService.findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(restauranteSalvo.getNome(), null, null);
            fail("Deveria ter lançado uma exceção");
        } catch (EntityNotFoundException e) {
            // Assert
            assertThat(e.getMessage()).contains("Restaurante com nome 'Restaurante Teste' não foi encontrado.");
        }
    }
    @Test
    void deveLancarExcecaoAoBuscarLocalizacaoRestauranteInexistente() {

        // Arrange
        RestauranteDTO restauranteSalvo = RestauranteDados.criarRestauranteDtoValido();
        when(restauranteRepository.findByNomeIgnoreCaseLikeOrLocalizacaoIgnoreCaseLikeOrTipoCozinhaIgnoreCaseLike(any(), eq(restauranteSalvo.getLocalizacao()), any())).thenReturn(Collections.emptyList());

        // Act
        try {
            List<Restaurante> actualRestaurants = restauranteService.findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(null, restauranteSalvo.getLocalizacao(), null);
            fail("Deveria ter lançado uma exceção");
        } catch (EntityNotFoundException e) {
            // Assert
            assertThat(e.getMessage()).contains("Restaurante com localização 'São Paulo, SP' não foi encontrado.");
        }
    }
    @Test
    void deveLancarExcecaoAoBuscarTipoCozinhaRestauranteInexistente() {

        // Arrange
        RestauranteDTO restauranteSalvo = RestauranteDados.criarRestauranteDtoValido();
        when(restauranteRepository.findByNomeIgnoreCaseLikeOrLocalizacaoIgnoreCaseLikeOrTipoCozinhaIgnoreCaseLike(any(), any(), eq(restauranteSalvo.getTipoCozinha()))).thenReturn(Collections.emptyList());

        // Act
        try {
            List<Restaurante> actualRestaurants = restauranteService.findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(null, null, restauranteSalvo.getTipoCozinha());
            fail("Deveria ter lançado uma exceção");
        } catch (EntityNotFoundException e) {
            // Assert
            assertThat(e.getMessage()).contains("Restaurante com tipo de cozinha 'Italiana' não foi encontrado.");
        }
    }

    @Test
    void deveLancarExcecaoAoBuscarNenhumParametroRestauranteInexistente() {

        // Arrange
        RestauranteDTO restauranteSalvo = RestauranteDados.criarRestauranteDtoValido();
        when(restauranteRepository.findByNomeIgnoreCaseLikeOrLocalizacaoIgnoreCaseLikeOrTipoCozinhaIgnoreCaseLike(any(), any(), any())).thenReturn(Collections.emptyList());

        // Act
        try {
            List<Restaurante> actualRestaurants = restauranteService.findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(null, null, null);
            fail("Deveria ter lançado uma exceção");
        } catch (IllegalArgumentException e) {
            // Assert
            assertThat(e.getMessage()).contains("Pelo menos um critério de busca (nome, localização ou tipo de cozinha) deve ser informado.");
        }
    }
    @Test
    void deveLancarExcecaoAoBuscarTodosRestaurantes() {

        // Arrange
        when(restauranteRepository.findAll()).thenThrow(DataIntegrityViolationException.class); // Simula um erro no repositório

        // Act
        try {
            restauranteService.findAll();
            fail("Deveria ter lançado uma exceção");
        } catch (Exception e) {
            // Assert
            assertThat(e).isInstanceOf(DataIntegrityViolationException.class); // Ajuste a exceção esperada se necessário
        }
    }
    @Test
    void deveLancarExcecaoAoAtualizarRestauranteInexistente() {

        // Arrange
        Long restauranteId = 100L; // ID inexistente
        RestauranteDTO restauranteDto = RestauranteDados.criarRestauranteDtoValido();

        // Act
        try {
            restauranteService.update(restauranteId, restauranteDto);
            fail("Deveria ter lançado uma exceção");
        } catch (Exception e) {
            // Assert
            assertThat(e).isInstanceOf(EntityNotFoundException.class);
            assertThat(e.getMessage()).contains("Restaurante não foi encontrado");
        }
    }

    @Test
    void deveLancarExcecaoAoDeletarRestauranteInexistente() {

        // Arrange
        Long restauranteId = 100L; // ID inexistente

        // Act
        try {
            restauranteService.delete(restauranteId);
            fail("Deveria ter lançado uma exceção"); // This will fail if no exception is thrown
        } catch (RuntimeException e) { // Adjust the exception type if necessary
            // Assert
            assertThat(e).isInstanceOf(RuntimeException.class);
        }
    }
}
