package br.com.vr.mini.autorizador.domain.domain.service;

import br.com.vr.mini.autorizador.domain.domain.converter.CartaoConverter;
import br.com.vr.mini.autorizador.application.application.request.CartaoRequest;
import br.com.vr.mini.autorizador.application.application.response.CartaoResponse;
import br.com.vr.mini.autorizador.domain.domain.repository.CartaoRepository;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.CartaoException;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CartaoService {

    private final CartaoRepository repository;
    private final CartaoConverter converter;

    public CartaoResponse criarCartao(final CartaoRequest request) {
        this.validaCriacaoCartao(request);
        var cartao = this.criarCartao(this.converter.requestToEntity(request));
        return this.respostaCartao(cartao);
    }

    public BigDecimal obterSaldo(final String numeroCartao) {
        return repository.findByNumeroCartao(numeroCartao).map(Cartao::getSaldo)
                .orElseThrow(() -> new CartaoException(numeroCartao, null, HttpStatus.NOT_FOUND));
    }

    private void validaCriacaoCartao(final CartaoRequest request) {
        this.repository.findByNumeroCartao(request.numeroCartao()).ifPresent(cartao -> {
            throw new CartaoException(request.numeroCartao(), request, HttpStatus.UNPROCESSABLE_ENTITY);
        });
    }

    private Cartao criarCartao(final Cartao cartao) {
        return this.repository.save(cartao);
    }

    private CartaoResponse respostaCartao(final Cartao cartao) {
        return this.converter.apply(cartao);
    }
}
