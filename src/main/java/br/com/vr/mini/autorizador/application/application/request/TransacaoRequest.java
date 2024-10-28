package br.com.vr.mini.autorizador.application.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record TransacaoRequest(

        @NotNull(message = "O número do cartão é obrigatório.")
        @NotBlank(message = "O número do cartão não pode ser em branco")
        @Size(min = 16, max = 16, message = "O número do cartão deve ter exatamente 16 dígitos.")
        @Schema(example = "1234567890123456")
        String numeroCartao,

        @NotNull(message = "A senha é obrigatória")
        @NotBlank(message = "A senha não pode ser em branco")
        @Size(min = 4, max = 4, message = "A senha deve ter exatamente 4 dígitos.")
        @Schema(example = "1234")
        String senha,

        @NotNull(message = "O valor é obrigatório")
        @Schema(example = "175.25")
        BigDecimal valor
) {}
