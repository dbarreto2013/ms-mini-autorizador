package br.com.vr.mini.autorizador.domain.domain.service;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.application.application.response.TransacaoResponseEnum;
import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import br.com.vr.mini.autorizador.domain.domain.model.TransacaoMongoDB;
import br.com.vr.mini.autorizador.domain.domain.repository.CartaoRepository;
import br.com.vr.mini.autorizador.domain.domain.repository.TransacaoMongoRepository;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.ConcurrencyException;
import br.com.vr.mini.autorizador.infrastructure.infrastructure.exception.TransacaoException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class TransacaoService {

    private final CartaoRepository cartaoRepository;
    private final TransacaoMongoRepository transacaoMongoRepository;
    private final TransacaoChain validatorChain;

    @Transactional
    public void realizarTransacao(final TransacaoRequest request) {
        var cartao = this.cartaoRepository.findByNumeroCartao(request.numeroCartao())
                .orElseThrow(() ->
                        new TransacaoException(request.numeroCartao(),
                                TransacaoResponseEnum.CARTAO_INEXISTENTE,
                                HttpStatus.UNPROCESSABLE_ENTITY
                        )
                );


        this.validatorChain.validar(cartao, request);

        salvarTransacao(request, cartao);

        this.atualizarSaldo(request.numeroCartao(), request.valor());

    }

    private void salvarTransacao(final TransacaoRequest request, final Cartao cartao) {
        this.transacaoMongoRepository.save(new TransacaoMongoDB(
                UUID.randomUUID().toString(),
                request.numeroCartao(),
                cartao.getSaldo(),
                (cartao.getSaldo().subtract(request.valor())),
                request.valor(),
                LocalDateTime.now()
        ));
    }

    @Transactional
    private void atualizarSaldo(String numeroCartao, BigDecimal valor) {
        try {
            this.cartaoRepository.debitarSaldo(numeroCartao, valor);
        } catch (OptimisticLockingFailureException | OptimisticLockException e) {
            throw new ConcurrencyException("Falha ao tentar realizar a transação devido a um conflito de concorrência", e);
        }
    }
}
