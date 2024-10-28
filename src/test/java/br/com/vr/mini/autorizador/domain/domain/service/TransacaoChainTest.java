package br.com.vr.mini.autorizador.domain.domain.service;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import br.com.vr.mini.autorizador.domain.domain.service.validator.TransacaoValidator;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.TransacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransacaoChainTest {

    private TransacaoChain transacaoChain;
    private TransacaoValidator validator1;
    private TransacaoValidator validator2;

    @BeforeEach
    void setUp() {
        validator1 = Mockito.mock(TransacaoValidator.class);
        validator2 = Mockito.mock(TransacaoValidator.class);
        List<TransacaoValidator> validators = Arrays.asList(validator1, validator2);
        transacaoChain = new TransacaoChain(validators);
    }

    @Test
    void testValidar_ValidadorExecutadoComSucesso() {
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(500.00),
                null
        );
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(100.00)
        );

        transacaoChain.validar(cartao, request);

        verify(validator1, times(1)).validar(cartao, request);
        verify(validator2, times(1)).validar(cartao, request);
    }

    @Test
    void testValidar_ValidadorLancaExcecao() {
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(500.00),
                null
        );
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(100.00)
        );

        doThrow(new TransacaoException("Erro", null, HttpStatus.UNPROCESSABLE_ENTITY))
                .when(validator1).validar(cartao, request);

        TransacaoException exception = assertThrows(TransacaoException.class, () -> {
            transacaoChain.validar(cartao, request);
        });

        assertEquals("Erro", exception.getMessage());
        verify(validator1, times(1)).validar(cartao, request);
        verify(validator2, never()).validar(cartao, request);
    }
}
