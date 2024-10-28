package br.com.vr.mini.autorizador.domain.domain.service;

import br.com.vr.mini.autorizador.application.application.request.CartaoRequest;
import br.com.vr.mini.autorizador.application.application.response.CartaoResponse;
import br.com.vr.mini.autorizador.domain.domain.converter.CartaoConverter;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import br.com.vr.mini.autorizador.domain.domain.repository.CartaoRepository;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.CartaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartaoServiceTest {

    private CartaoService cartaoService;
    private CartaoRepository cartaoRepository;
    private CartaoConverter cartaoConverter;

    @BeforeEach
    void setUp() {
        cartaoRepository = Mockito.mock(CartaoRepository.class);
        cartaoConverter = Mockito.mock(CartaoConverter.class);
        cartaoService = new CartaoService(cartaoRepository, cartaoConverter);
    }

    @Test
    void testCriarCartao_Sucesso() {
        var request = new CartaoRequest(
                "1234567890123456",
                "1234"
        );
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(500.00),
                null
        );
        var response = new CartaoResponse(
                "1234",
                "1234567890123456"
        );

        when(cartaoRepository.findByNumeroCartao(request.numeroCartao())).thenReturn(Optional.empty());
        when(cartaoConverter.requestToEntity(request)).thenReturn(cartao);
        when(cartaoRepository.save(cartao)).thenReturn(cartao);
        when(cartaoConverter.apply(cartao)).thenReturn(response);

        CartaoResponse result = cartaoService.criarCartao(request);

        assertNotNull(result);
        assertEquals("1234567890123456", result.numeroCartao());
        assertEquals("1234", result.senha());
        verify(cartaoRepository, times(1)).save(cartao);
    }

    @Test
    void testCriarCartao_CartaoExistente_DeveLancarCartaoException() {
        var request = new CartaoRequest(
                "1234567890123456",
                "1234"
        );
        var existingCartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(500.00),
                null
        );

        when(cartaoRepository.findByNumeroCartao(request.numeroCartao())).thenReturn(Optional.of(existingCartao));

        CartaoException exception = assertThrows(CartaoException.class, () -> {
            cartaoService.criarCartao(request);
        });

        assertEquals("1234567890123456", exception.getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        verify(cartaoRepository, never()).save(any());
    }

    @Test
    void testObterSaldo_CartaoExistente() {
        var cartao = new Cartao(
                null,
                "1234567890123456",
                "1234",
                BigDecimal.valueOf(500.00),
                null
        );
        when(cartaoRepository.findByNumeroCartao("1234567890123456")).thenReturn(Optional.of(cartao));

        var saldo = cartaoService.obterSaldo("1234567890123456");

        assertEquals(BigDecimal.valueOf(500.00), saldo);
    }

    @Test
    void testObterSaldo_CartaoNaoExistente_DeveLancarCartaoException() {
        when(cartaoRepository.findByNumeroCartao("1234567890123456")).thenReturn(Optional.empty());

        CartaoException exception = assertThrows(CartaoException.class, () -> {
            cartaoService.obterSaldo("1234567890123456");
        });

        assertEquals("1234567890123456", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}
