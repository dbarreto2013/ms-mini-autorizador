package br.com.vr.mini.autorizador.domain.domain.repository;

import br.com.vr.mini.autorizador.domain.domain.model.Cartao;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    Optional<Cartao> findByNumeroCartao(final String numeroCartao);

    @Transactional
    @Modifying
    @Query("UPDATE Cartao c SET c.saldo = c.saldo - :valor WHERE c.numeroCartao = :numeroCartao")
    void debitarSaldo(String numeroCartao, BigDecimal valor);

}
