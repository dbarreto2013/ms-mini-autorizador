package br.com.vr.mini.autorizador.domain.domain.service.validator;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;

public interface TransacaoValidator {
    void validar(final Cartao cartao, final TransacaoRequest request);
}
