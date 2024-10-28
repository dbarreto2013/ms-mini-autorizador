package br.com.vr.mini.autorizador.domain.domain.service.validator;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.application.application.response.TransacaoResponseEnum;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.TransacaoException;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CartaoExistenteValidatorTest {

    private CartaoExistenteValidator cartaoExistenteValidator;

    @BeforeEach
    void setUp() {
        cartaoExistenteValidator = new CartaoExistenteValidator();
    }

    @Test
    void testValidar_CartaoNulo_DeveLancarTransacaoException() {
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(100.00));

        var exception = assertThrows(TransacaoException.class, () -> {
            cartaoExistenteValidator.validar(null, request);
        });

        assertEquals(TransacaoResponseEnum.CARTAO_INEXISTENTE, exception.getTransacaoResponseEnum());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertEquals("1234567890123456", exception.getMessage());
    }

    @Test
    void testValidar_CartaoExistente_NaoDeveLancarException() {
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
            cartaoExistenteValidator.validar(cartao, request);
        });
    }
}
