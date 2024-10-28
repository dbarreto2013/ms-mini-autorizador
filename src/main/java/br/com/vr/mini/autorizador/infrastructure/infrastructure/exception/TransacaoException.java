package br.com.vr.mini.autorizador.infrastructure.infrastructure.exception;

import br.com.vr.mini.autorizador.application.application.response.TransacaoResponseEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class TransacaoException extends RuntimeException {

    private HttpStatus httpStatus;
    private TransacaoResponseEnum transacaoResponseEnum;

    public TransacaoException(final String numeroCartao,
                              final TransacaoResponseEnum transacaoResponseEnum,
                              final HttpStatus httpStatus) {
        super(numeroCartao);
        this.setTransacaoResponseEnum(transacaoResponseEnum);
        this.setHttpStatus(httpStatus);
    }
}