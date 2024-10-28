package br.com.vr.mini.autorizador.infrastructure.infrastructure.exception;

public class ConcurrencyException extends RuntimeException {
    public ConcurrencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
