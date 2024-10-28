package br.com.vr.mini.autorizador.domain.domain.service;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import br.com.vr.mini.autorizador.domain.domain.service.validator.TransacaoValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@AllArgsConstructor
@Component
public class TransacaoChain {

    private final List<TransacaoValidator> validators;

    public void validar(final Cartao cartao, final TransacaoRequest request) {
        for (TransacaoValidator validator : validators) {
            validator.validar(cartao, request);
        }
    }
}
