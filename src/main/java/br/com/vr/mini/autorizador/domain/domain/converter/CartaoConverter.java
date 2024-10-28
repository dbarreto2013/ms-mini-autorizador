package br.com.vr.mini.autorizador.domain.domain.converter;

import br.com.vr.mini.autorizador.application.application.request.CartaoRequest;
import br.com.vr.mini.autorizador.application.application.response.CartaoResponse;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Function;

@Component
public class CartaoConverter implements Function<Cartao, CartaoResponse> {

    private static final BigDecimal SALDO_INICIAL = BigDecimal.valueOf(500.00);

    @Override
    public CartaoResponse apply(final Cartao cartao) {
        return new CartaoResponse(
                cartao.getSenha(),
                cartao.getNumeroCartao()
        );
    }

    public Cartao requestToEntity(final CartaoRequest request) {
        return new Cartao(
                null,
                request.numeroCartao(),
                request.senha(),
                SALDO_INICIAL,
                LocalDateTime.now()
        );
    }
}
