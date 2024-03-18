package com.techchallenge.restaurant.api.findfood.domain.service;


import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;

    @Autowired
    private final ModelMapper modelMapper = new ModelMapper();

    public RestauranteServiceImpl(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public RestauranteDTO save(RestauranteDTO restauranteDTO) {
        Restaurante restaurante = restauranteRepository.save(modelMapper.map(restauranteDTO, Restaurante.class));
        return modelMapper.map(restaurante, RestauranteDTO.class);
    }

    public List<Restaurante> findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(String nome, String localizacao, String tipoDeCozinha) {

        if (nome == null || nome.isEmpty()) {
            if (localizacao == null || localizacao.isEmpty()) {
                if (tipoDeCozinha == null || tipoDeCozinha.isEmpty()) {
                    throw new IllegalArgumentException("Pelo menos um critério de busca (nome, localização ou tipo de cozinha) deve ser informado.");
                }
            }
        }

        // Execute search based on provided criteria
        List<Restaurante> restaurantesEncontrados = restauranteRepository.findByNomeIgnoreCaseLikeOrLocalizacaoIgnoreCaseLikeOrTipoCozinhaIgnoreCaseLike(nome, localizacao, tipoDeCozinha);

        // Handle empty result based on search criteria
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

    @Override
    public Optional<RestauranteDTO> findById(Long id) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(id);
        return restauranteOptional.map(restaurante -> modelMapper.map(restaurante, RestauranteDTO.class));
    }

    public Collection<RestauranteDTO> findAll() {
        return restauranteRepository.findAll().stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteDTO.class))
                .toList();
    }


    public RestauranteDTO update(Long restauranteId, RestauranteDTO restauranteDTO) {
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

    public void delete(Long id) {
        Optional<Restaurante> restauranteOptional = restauranteRepository.findById(id);
        if (restauranteOptional.isPresent()) {
            restauranteRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(String.format("Restaurante com ID '%s' não foi encontrado para exclusão.", id));
        }
    }

}
