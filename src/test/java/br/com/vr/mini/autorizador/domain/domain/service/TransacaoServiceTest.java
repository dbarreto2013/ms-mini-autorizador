package br.com.vr.mini.autorizador.domain.domain.service;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.application.application.response.TransacaoResponseEnum;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import br.com.vr.mini.autorizador.domain.domain.model.TransacaoMongoDB;
import br.com.vr.mini.autorizador.domain.domain.repository.CartaoRepository;
import br.com.vr.mini.autorizador.domain.domain.repository.TransacaoMongoRepository;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.ConcurrencyException;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.TransacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransacaoServiceTest {

    private TransacaoService transacaoService;
    private CartaoRepository cartaoRepository;
    private TransacaoMongoRepository transacaoMongoRepository;
    private TransacaoChain validatorChain;

    @BeforeEach
    void setUp() {
        cartaoRepository = mock(CartaoRepository.class);
        transacaoMongoRepository = mock(TransacaoMongoRepository.class);
        validatorChain = mock(TransacaoChain.class);
        transacaoService = new TransacaoService(cartaoRepository, transacaoMongoRepository, validatorChain);
    }

    @Test
    void testRealizarTransacao_Sucesso() {
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234", BigDecimal.valueOf(100.00)
        );
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(500.00),
                null
        );

        when(cartaoRepository.findByNumeroCartao(request.numeroCartao())).thenReturn(Optional.of(cartao));

        transacaoService.realizarTransacao(request);

        verify(validatorChain, times(1)).validar(cartao, request);

        var transacaoCaptor = ArgumentCaptor.forClass(TransacaoMongoDB.class);
        verify(transacaoMongoRepository, times(1)).save(transacaoCaptor.capture());

        var transacao = transacaoCaptor.getValue();
        assertEquals(request.numeroCartao(), transacao.getNumeroCartao());
        assertEquals(cartao.getSaldo(), transacao.getValorAntigo());
        assertEquals(cartao.getSaldo().subtract(request.valor()), transacao.getValorAtual());
        assertEquals(request.valor(), transacao.getValorTransacao());
        assertNotNull(transacao.getCreatedAt());
    }

    @Test
    void testRealizarTransacao_CartaoInexistente() {
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(100.00)
        );

        when(cartaoRepository.findByNumeroCartao(request.numeroCartao())).thenReturn(Optional.empty());

        TransacaoException exception = assertThrows(TransacaoException.class, () -> {
            transacaoService.realizarTransacao(request);
        });

        assertEquals(TransacaoResponseEnum.CARTAO_INEXISTENTE.name(), exception.getTransacaoResponseEnum().name());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
    }

    @Test
    void testRealizarTransacao_ConcurrencyException() {
        var request = new TransacaoRequest(
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(100.00)
        );
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(500.00),
                null
        );

        when(cartaoRepository.findByNumeroCartao(request.numeroCartao())).thenReturn(Optional.of(cartao));
        doThrow(new OptimisticLockingFailureException("Conflito")).when(cartaoRepository)
                .debitarSaldo(cartao.getNumeroCartao(), request.valor());

        ConcurrencyException exception = assertThrows(ConcurrencyException.class, () -> {
            transacaoService.realizarTransacao(request);
        });

        assertEquals("Falha ao tentar realizar a transação devido a um conflito de concorrência",
                exception.getMessage());
    }
}
