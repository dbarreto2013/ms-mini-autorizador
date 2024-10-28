package br.com.vr.mini.autorizador.infrastructure.infrastructure.exception;

import br.com.vr.mini.autorizador.application.application.request.CartaoRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class CartaoException extends RuntimeException {

    private HttpStatus httpStatus;
    private CartaoRequest request;

    public CartaoException(final String numeroCartao, final CartaoRequest request, final HttpStatus httpStatus) {
        super(numeroCartao);
        this.setRequest(request);
        this.setHttpStatus(httpStatus);
    }
}