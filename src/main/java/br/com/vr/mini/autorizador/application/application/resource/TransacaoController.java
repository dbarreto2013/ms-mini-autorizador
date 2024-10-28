package br.com.vr.mini.autorizador.application.application.resource;

import br.com.vr.mini.autorizador.application.application.request.TransacaoRequest;
import br.com.vr.mini.autorizador.application.application.response.TransacaoResponseEnum;
import br.com.vr.mini.autorizador.domain.domain.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService service;

    @Operation(
            security = @SecurityRequirement(name = "basic"),
            summary = "Realiza uma transação",
            description = "Este endpoint realiza uma transação com os dados fornecidos. " +
                    "O número do cartão deve ter exatamente 16 dígitos, a senha deve ter exatamente 4 dígitos, " +
                    "e o valor deve ser um número positivo.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( // Anotação para o corpo da requisição
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransacaoRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Transação realizada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransacaoResponseEnum.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Cartão não encontrado",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "409", description = "Conflito ao realizar a transação (ex.: saldo insuficiente)",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<TransacaoResponseEnum> realizarTransacao(@Valid @RequestBody TransacaoRequest request) {
        this.service.realizarTransacao(request);
        return new ResponseEntity<>(TransacaoResponseEnum.OK, HttpStatus.CREATED);
    }
}
