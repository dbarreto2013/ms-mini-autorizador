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

class SaldoSuficienteValidatorTest {

    private SaldoSuficienteValidator saldoSuficienteValidator;

    @BeforeEach
    void setUp() {
        saldoSuficienteValidator = new SaldoSuficienteValidator();
    }

    @Test
    void testValidar_SaldoInsuficiente_DeveLancarTransacaoException() {
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(150.00)
        );
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(100.00),
                null
        );

        var exception = assertThrows(TransacaoException.class, () -> {
            saldoSuficienteValidator.validar(cartao, request);
        });

        assertEquals(TransacaoResponseEnum.SALDO_INSUFICIENTE, exception.getTransacaoResponseEnum());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertEquals("1234567890123456", exception.getMessage());
    }

    @Test
    void testValidar_SaldoSuficiente_NaoDeveLancarException() {
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(200.00),
                null
        );
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(150.00)
        );

        assertDoesNotThrow(() -> {
            saldoSuficienteValidator.validar(cartao, request);
        });
    }

}
