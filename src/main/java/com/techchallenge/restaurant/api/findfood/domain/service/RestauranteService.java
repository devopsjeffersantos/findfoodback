package com.techchallenge.restaurant.api.findfood.domain.service;

import com.techchallenge.restaurant.api.findfood.api.model.RestauranteDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RestauranteService {

    RestauranteDTO save(RestauranteDTO restauranteDTO);

    List<Restaurante> findRestaurantePorNomeOuLocalizacaoOuTipoDeCozinha(String nome, String localizacao, String tipoDeCozinha);

    Collection<RestauranteDTO> findAll();

    Optional<RestauranteDTO> findById(Long id);

    RestauranteDTO update(Long restauranteId, RestauranteDTO restauranteDTO);

    void delete(Long id);



}
