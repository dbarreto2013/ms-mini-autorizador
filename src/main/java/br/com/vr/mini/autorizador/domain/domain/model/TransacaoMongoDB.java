package br.com.vr.mini.autorizador.domain.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Document(collection = "transacoes")
public class TransacaoMongoDB {

    @Id
    private String id;
    private String numeroCartao;

    private BigDecimal valorAntigo;
    private BigDecimal valorAtual;
    private BigDecimal valorTransacao;
    private LocalDateTime createdAt;

}
