package com.techchallenge.restaurant.api.findfood.domain.repository;

import com.techchallenge.restaurant.api.findfood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    @Query("SELECT r FROM Restaurante r " +
            "WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
            "AND LOWER(r.localizacao) LIKE LOWER(CONCAT('%', :localizacao, '%')) " +
            "AND LOWER(r.tipoCozinha) LIKE LOWER(CONCAT('%', :tipoCozinha, '%'))")
    List<Restaurante> findByNomeIgnoreCaseLikeOrLocalizacaoIgnoreCaseLikeOrTipoCozinhaIgnoreCaseLike(String nome, String localizacao, String tipoCozinha);

}
