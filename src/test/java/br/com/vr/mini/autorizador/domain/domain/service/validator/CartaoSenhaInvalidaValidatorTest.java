package br.com.vr.mini.autorizador.domain.domain.service.validator;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.application.application.response.TransacaoResponseEnum;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.TransacaoException;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CartaoSenhaInvalidaValidatorTest {

    private CartaoSenhaInvalidaValidator cartaoSenhaInvalidaValidator;

    @BeforeEach
    void setUp() {
        cartaoSenhaInvalidaValidator = new CartaoSenhaInvalidaValidator();
    }

    @Test
    void testValidar_SenhaInvalida_DeveLancarTransacaoException() {
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                null,
                null
        );
        var request = new TransacaoRequest(
                "1234567890123456",
                "4321",
                BigDecimal.valueOf(100.00)
        );

        var exception = assertThrows(TransacaoException.class, () -> {
            cartaoSenhaInvalidaValidator.validar(cartao, request);
        });

        assertEquals(TransacaoResponseEnum.SENHA_INVALIDA, exception.getTransacaoResponseEnum());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertEquals("1234567890123456", exception.getMessage());
    }

    @Test
    void testValidar_SenhaValida_NaoDeveLancarException() {
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                null,
                null
        );
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(100.00)
        );

        assertDoesNotThrow(() -> {
            cartaoSenhaInvalidaValidator.validar(cartao, request);
        });
    }

    @Test
    void testValidar_CartaoNulo_NaoDeveLancarException() {
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(100.00)
        );

        assertDoesNotThrow(() -> {
            cartaoSenhaInvalidaValidator.validar(null, request);
        });
    }
}
