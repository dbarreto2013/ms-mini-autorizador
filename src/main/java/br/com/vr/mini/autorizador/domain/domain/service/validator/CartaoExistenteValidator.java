package br.com.vr.mini.autorizador.domain.domain.service.validator;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.application.application.response.TransacaoResponseEnum;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.TransacaoException;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CartaoExistenteValidator implements TransacaoValidator {

    @Override
    public void validar(final Cartao cartao, final TransacaoRequest request) {
        if (cartao == null) {
            throw new TransacaoException(
                    request.numeroCartao(),
                    TransacaoResponseEnum.CARTAO_INEXISTENTE,
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }
    }
}
