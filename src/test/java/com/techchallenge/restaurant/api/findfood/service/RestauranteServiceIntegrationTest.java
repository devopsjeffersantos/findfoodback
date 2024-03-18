package com.techchallenge.restaurant.api.findfood.service;

import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import com.techchallenge.restaurant.api.findfood.domain.service.RestauranteServiceImpl;
import com.techchallenge.restaurant.api.findfood.dados.RestauranteDados;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class RestauranteServiceIntegrationTest {

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
        assertThat(restauranteRegistrado)
            .isInstanceOf(RestauranteDTO.class)
            .isNotNull();
    }

}
