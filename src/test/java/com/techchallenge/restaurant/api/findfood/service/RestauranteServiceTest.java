package com.techchallenge.restaurant.api.findfood.service;

import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import com.techchallenge.restaurant.api.findfood.domain.service.RestauranteService;
import com.techchallenge.restaurant.api.findfood.dados.RestauranteDados;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RestauranteServiceTest extends RestauranteDados {

    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private RestauranteService restauranteService;

    AutoCloseable mock;
    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }
    @Nested
    @DisplayName("Testes de Registro de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class registrarRestaurante{
        @Test
        @Order(1)
        void devePermitirRegistrarRestaurante(){

            // Arrange
            var restauranteDto = criarRestauranteDtoValido();
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(criarRestauranteValido());

            //Act
            restauranteService.registrarRestaurante(restauranteDto);

            // Assert
            verify(restauranteRepository, times(1)).save(any());
            verifyNoMoreInteractions(restauranteRepository);
        }
        @Test
        @Order(2)
        void deveLancarExcecaoAoSalvarRestauranteComNomeVazio() {

            // Arrange
            var restauranteDto = criarRestauranteDtoValido();
            restauranteDto.setNome("");
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(criarRestauranteValido());

            //Act & Assert
            assertThatThrownBy(() -> restauranteService.registrarRestaurante(restauranteDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Inconsistencia nos campos informados.");

        }
    }
    @Nested
    @DisplayName("Testes de Atualização de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class atualizarRestaurante{
        @Test
        @Order(1)
        void devePermitirAtualizarRestaurantes(){

            // Arrange
            var restauranteId = 1L;
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(criarRestauranteValido());

            // Act
            restauranteService.registrarRestaurante(restauranteDto);
            restauranteDto.setNome("Restaurante Teste 2");
            restauranteService.atualizarRestaurante(restauranteId, restauranteDto);

            // Assert
            verify(restauranteRepository, times(2)).save(any());

        }
        @Test
        @Order(2)
        void deveLancarExcecaoAoAtualizarRestauranteInexistente() {

            // Arrange
            var restauranteId = 1L;
            var restauranteDto = criarRestauranteDtoValido();

            //Act & Assert
            assertThatThrownBy(() -> restauranteService.atualizarRestaurante(restauranteId, restauranteDto))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Restaurante não foi encontrado");

        }
    }
    @Nested
    @DisplayName("Testes de Exclusão de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class deletarRestaurante {
        @Test
        @Order(1)
        void devePermitirDeletarRestaurantes() {

            // Arrange
            var restauranteId = 1L;
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(restaurante);

            // Act
            restauranteService.registrarRestaurante(restauranteDto);
            restauranteService.deletarRestaurante(restauranteId);

            // Assert
            verify(restauranteRepository, times(1)).deleteById(restauranteId);
        }
        @Test
        @Order(2)
        void deveLancarExcecaoAoDeletarRestauranteInexistente() {

            // Arrange
            Long restauranteId = 100L; // ID inexistente

            //Act & Assert
            assertThatThrownBy(() -> restauranteService.deletarRestaurante(restauranteId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Restaurante com ID '100' não foi encontrado para exclusão.");

        }
    }
    @Nested
    @DisplayName("Testes de Busca de Restaurante")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class buscarRestaurante{
        @Test
        @Order(1)
        void devePermitirBuscarRestaurantePorNome() {

            // Arrange
            var restauranteId = 1L;
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(restaurante);
            when(restauranteRepository.findByNomeLocalizacaoTipoCozinha(eq(restauranteDto.getNome()), any(), any())).thenReturn(Collections.singletonList(restaurante));

            // Act
            restauranteService.registrarRestaurante(restauranteDto);
            List<Restaurante> actualRestaurants = restauranteService.buscarRestaurantePor(restauranteDto.getNome(), null, null);

            // Assert
            verify(restauranteRepository, times(1)).save(any());
            verify(restauranteRepository, times(1)).findByNomeLocalizacaoTipoCozinha(any(), any(), any());
        }
        @Test
        @Order(2)
        void devePermitirBuscarRestaurantePorLocalizacao(){

            // Arrange
            var restauranteId = 1L;
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(restaurante);
            when(restauranteRepository.findByNomeLocalizacaoTipoCozinha(any(), eq(restauranteDto.getLocalizacao()), any())).thenReturn(Collections.singletonList(restaurante));

            // Act
            restauranteService.registrarRestaurante(restauranteDto);
            List<Restaurante> actualRestaurants = restauranteService.buscarRestaurantePor(null, restauranteDto.getLocalizacao(), null);

            // Assert
            verify(restauranteRepository, times(1)).save(any());
            verify(restauranteRepository, times(1)).findByNomeLocalizacaoTipoCozinha(any(), any(), any());
        }
        @Test
        @Order(3)
        void devePermitirBuscarRestaurantePorTipoCozinha(){

            // Arrange
            var restauranteId = 1L;
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(restaurante);
            when(restauranteRepository.findByNomeLocalizacaoTipoCozinha(any(), any(), eq(restauranteDto.getTipoCozinha()))).thenReturn(Collections.singletonList(restaurante));

            // Act
            restauranteService.registrarRestaurante(restauranteDto);
            List<Restaurante> actualRestaurants = restauranteService.buscarRestaurantePor(null, null , restauranteDto.getTipoCozinha());

            // Assert
            verify(restauranteRepository, times(1)).save(any());
            verify(restauranteRepository, times(1)).findByNomeLocalizacaoTipoCozinha(any(), any(), any());
        }
        @Test
        @Order(4)
        void devePermitirBuscarRestaurantesPorId(){

            // Arrange
            var restauranteId = 1L;
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(restaurante);

            // Act
            restauranteService.registrarRestaurante(restauranteDto);
            Optional<Restaurante> actualRestaurants = restauranteService.buscarRestaurantePorID(restaurante.getId());

            // Assert
            verify(restauranteRepository, times(1)).save(any());
            verify(restauranteRepository, times(2)).findById(any());

        }
        @Test
        @Order(5)
        void devePermitirBuscarTodosRestaurantes(){

            // Arrange
            var restauranteId = 1L;
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findAll()).thenReturn(Collections.singletonList(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(restaurante);

            // Act
            restauranteService.registrarRestaurante(restauranteDto);
            restauranteService.buscarTodosRestaurantes();

            // Assert
            verify(restauranteRepository, times(1)).findAll();
        }
        @Test
        @Order(6)
        void deveLancarExcecaoAoBuscarNomeRestauranteInexistente() {

            // Arrange
            var restauranteNome = "Ifood";
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(restaurante);
            when(restauranteRepository.findByNomeLocalizacaoTipoCozinha(eq(restauranteDto.getNome()), any(), any())).thenReturn(Collections.singletonList(restaurante));

            //Act & Assert
            assertThatThrownBy(() -> restauranteService.buscarRestaurantePor(restauranteNome, StringUtils.EMPTY, StringUtils.EMPTY))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Restaurante com nome '"+restauranteNome+"' não foi encontrado.");
        }
        @Test
        @Order(7)
        void deveLancarExcecaoAoBuscarLocalizacaoRestauranteInexistente() {

            // Arrange
            var restauranteLocalizacao = "Rio de Janeiro";
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(restaurante);
            when(restauranteRepository.findByNomeLocalizacaoTipoCozinha(any(), eq(restauranteDto.getLocalizacao()), any())).thenReturn(Collections.singletonList(restaurante));

            //Act & Assert
            assertThatThrownBy(() -> restauranteService.buscarRestaurantePor(StringUtils.EMPTY, restauranteLocalizacao, StringUtils.EMPTY))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Restaurante com localização '"+restauranteLocalizacao+"' não foi encontrado.");
        }
        @Test
        @Order(8)
        void deveLancarExcecaoAoBuscarTipoCozinhaRestauranteInexistente() {

            // Arrange
            var restauranteTipoCozinha = "Francesa";
            var restauranteDto = criarRestauranteDtoValido();
            var restaurante = criarRestauranteValido();

            when(restauranteRepository.findById(restaurante.getId())).thenReturn(Optional.of(restaurante));
            when(modelMapper.map(restauranteDto, Restaurante.class)).thenReturn(restaurante);
            when(restauranteRepository.findByNomeLocalizacaoTipoCozinha(any(), any(), eq(restauranteDto.getTipoCozinha()))).thenReturn(Collections.singletonList(restaurante));

            //Act & Assert
            assertThatThrownBy(() -> restauranteService.buscarRestaurantePor(StringUtils.EMPTY, StringUtils.EMPTY, restauranteTipoCozinha))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Restaurante com tipo de cozinha '"+restauranteTipoCozinha+"' não foi encontrado.");
        }
        @Test
        @Order(9)
        void deveLancarExcecaoAoBuscarTodosRestaurantes() {

            assertThatThrownBy(() -> restauranteService.buscarTodosRestaurantes())
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Nenhum Restaurante Cadastrado");

        }
    }

}
