package br.com.vr.mini.autorizador.infrastructure.infrastructure.exception;

import br.com.vr.mini.autorizador.application.application.request.CartaoRequest;
import br.com.vr.mini.autorizador.application.application.response.TransacaoResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartaoException.class)
    public ResponseEntity<CartaoRequest> handleBusinessException(CartaoException ex) {

        if(HttpStatus.NOT_FOUND.equals(ex.getHttpStatus())) {
            return new ResponseEntity<>(ex.getHttpStatus());
        } else {
            return new ResponseEntity<CartaoRequest>(ex.getRequest(), ex.getHttpStatus());
        }
    }

    @ExceptionHandler(TransacaoException.class)
    public ResponseEntity<TransacaoResponseEnum> handleBusinessException(TransacaoException ex) {
        log.error("Cartão {}", ex.getTransacaoResponseEnum().name());
        return new ResponseEntity<TransacaoResponseEnum>(ex.getTransacaoResponseEnum(), ex.getHttpStatus());
    }

    @ExceptionHandler(ConcurrencyException.class)
    public ResponseEntity<Map<String, String>> handleBusinessException(ConcurrencyException ex) {
        log.error("Conflito de concorrência {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Formato de entrada inválido. Verifique os dados enviados.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
