package br.com.vr.mini.autorizador.domain.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_cartao")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroCartao;
    private String senha;
    private BigDecimal saldo;
    private LocalDateTime createdAt;

    @Version
    private Long version;

    public Cartao(Long id, String numeroCartao,
                  String senha, BigDecimal saldo, LocalDateTime createdAt) {
        this.id = id;
        this.numeroCartao = numeroCartao;
        this.senha = senha;
        this.saldo = saldo;
        this.createdAt = createdAt;
    }
}
