package br.com.vr.mini.autorizador.domain.domain.service.validator;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.application.application.response.TransacaoResponseEnum;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.TransacaoException;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class SaldoSuficienteValidator implements TransacaoValidator {

    @Override
    public void validar(final Cartao cartao, final TransacaoRequest request) {
        if (cartao.getSaldo().compareTo(request.valor()) < 0) {
            throw new TransacaoException(
                    cartao.getNumeroCartao(),
                    TransacaoResponseEnum.SALDO_INSUFICIENTE,
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }
}
