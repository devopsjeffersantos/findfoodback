package com.techchallenge.restaurant.api.findfood.domain.exception;

public class NaoHaMesasDisponiveisException extends RuntimeException {
    public NaoHaMesasDisponiveisException(String mensagem) {
        super(mensagem);
    }
}