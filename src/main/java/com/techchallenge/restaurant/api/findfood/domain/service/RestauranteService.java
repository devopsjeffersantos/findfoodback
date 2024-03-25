package com.techchallenge.restaurant.api.findfood.domain.service;


import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final ModelMapper modelMapper;

    public void registrarRestaurante(RestauranteDTO restauranteDTO) {
        if (isRestauranteDTOValid(restauranteDTO)) {
            var restaurante = modelMapper.map(restauranteDTO, Restaurante.class);
            restauranteRepository.save(restaurante);
        }else{
            throw new IllegalArgumentException("Inconsistencia nos campos informados.");
        }
    }

    public List<Restaurante> buscarRestaurantePor(String nome, String localizacao, String tipoDeCozinha) {

        List<Restaurante> restaurantesEncontrados = restauranteRepository.findByNomeLocalizacaoTipoCozinha(nome, localizacao, tipoDeCozinha);

        if (restaurantesEncontrados.isEmpty()) {
            if (nome != null && !nome.isEmpty()) {
                throw new EntityNotFoundException(String.format("Restaurante com nome '%s' não foi encontrado.", nome));
            } else if (localizacao != null && !localizacao.isEmpty()) {
                throw new EntityNotFoundException(String.format("Restaurante com localização '%s' não foi encontrado.", localizacao));
            } else {
                throw new EntityNotFoundException(String.format("Restaurante com tipo de cozinha '%s' não foi encontrado.", tipoDeCozinha));
            }
        }

        return restaurantesEncontrados;
    }
    public Optional<Restaurante> buscarRestaurantePorID(Long id) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(id);
        if (restauranteOptional.isPresent()) {
            restauranteRepository.findById(id);
            return restauranteOptional;
        }else {
            throw new EntityNotFoundException("Nenhum Restaurante Encontrado");
        }
    }
    public List<Restaurante> buscarTodosRestaurantes() {
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        if(restaurantes.isEmpty()) {
            throw new EntityNotFoundException("Nenhum Restaurante Cadastrado");
        }else{
            return restaurantes;
        }
    }
    public RestauranteDTO atualizarRestaurante(Long restauranteId, RestauranteDTO restauranteDTO) {

        Optional<Restaurante> optionalRestaurante = restauranteRepository.findById(restauranteId);

        if(optionalRestaurante.isPresent()){
            Restaurante restaurante = optionalRestaurante.get();
            modelMapper.map(restauranteDTO, restaurante);

            restaurante = restauranteRepository.save(restaurante);

            return modelMapper.map(restaurante, RestauranteDTO.class);
        } else {
            throw new EntityNotFoundException("Restaurante não foi encontrado");
        }
    }
    public void deletarRestaurante(Long id) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(id);
        if (restauranteOptional.isPresent()) {
            restauranteRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(String.format("Restaurante com ID '%s' não foi encontrado para exclusão.", id));
        }
    }

    private boolean isRestauranteDTOValid(RestauranteDTO dto) {
        return dto.getNome() != null && !dto.getNome().isEmpty() &&
                dto.getLocalizacao() != null && !dto.getLocalizacao().isEmpty() &&
                dto.getTipoCozinha() != null && !dto.getTipoCozinha().isEmpty() &&
                dto.getHorarioFuncionamento() != null && !dto.getHorarioFuncionamento().isEmpty() &&
                dto.getQuantidadeTotalDeMesas() != null && dto.getQuantidadeTotalDeMesas() > 0;
    }

}
