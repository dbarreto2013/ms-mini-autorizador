package br.com.vr.mini.autorizador.domain.domain.converter;

import br.com.vr.mini.autorizador.application.application.request.CartaoRequest;
import br.com.vr.mini.autorizador.application.application.response.CartaoResponse;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CartaoConverterTest {

    private static final BigDecimal SALDO_INICIAL = BigDecimal.valueOf(500.00);

    private CartaoConverter cartaoConverter;

    @BeforeEach
    void setUp() {
        cartaoConverter = new CartaoConverter();
    }

    @Test
    void testApply() {
        Cartao cartao = new Cartao(null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(500.00),
                LocalDateTime.now()
        );

        CartaoResponse response = cartaoConverter.apply(cartao);

        assertEquals("1234", response.senha());
        assertEquals("1234567890123456", response.numeroCartao());
    }

    @Test
    void testRequestToEntity() {
        CartaoRequest request = new CartaoRequest("1234567890123456", "1234");

        Cartao cartao = cartaoConverter.requestToEntity(request);

        assertEquals("1234567890123456", cartao.getNumeroCartao());
        assertEquals("1234", cartao.getSenha());
        assertEquals(CartaoConverterTest.SALDO_INICIAL, cartao.getSaldo());
        assertEquals(LocalDateTime.now().getMinute(), cartao.getCreatedAt().getMinute());
    }
}
