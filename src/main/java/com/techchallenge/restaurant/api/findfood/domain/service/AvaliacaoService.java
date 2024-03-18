package com.techchallenge.restaurant.api.findfood.domain.service;

import com.techchallenge.restaurant.api.findfood.api.model.AvaliacaoDTO;
import com.techchallenge.restaurant.api.findfood.domain.model.Avaliacao;
import com.techchallenge.restaurant.api.findfood.domain.repository.AvaliacaoRepository;
import com.techchallenge.restaurant.api.findfood.domain.repository.RestauranteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    private final RestauranteRepository restauranteRepository;

    private final ModelMapper modelMapper;

    public void registrarAvaliacao(Long restauranteId, AvaliacaoDTO avaliacaoDTO) {
        var optionalRestaurante = restauranteRepository.findById(restauranteId);
        var avaliacao = modelMapper.map(avaliacaoDTO, Avaliacao.class);

        if(optionalRestaurante.isEmpty()){
            throw new EntityNotFoundException("Restaurante não foi encontrada");
        }

        if(avaliacao.getPontuacao() < 0 || avaliacao.getPontuacao() > 5){
            throw new IllegalArgumentException("Pontuação para a reserva precisa ser um valor de 0 a 5");
        }

        avaliacao.setRestaurante(optionalRestaurante.get());
        avaliacaoRepository.save(avaliacao);
    }


    public List<AvaliacaoDTO> findAll(Long restauranteId) {
        var optionalRestaurante = restauranteRepository.findById(restauranteId);

        if(optionalRestaurante.isEmpty()){
            throw new EntityNotFoundException("Restaurante não foi encontrada");
        }

        var avaliacaoList = avaliacaoRepository.findAllByRestaurante(optionalRestaurante.get());
        return avaliacaoList.stream()
                .map(avaliacao -> modelMapper.map(avaliacao, AvaliacaoDTO.class))
                .toList();
    }

}
