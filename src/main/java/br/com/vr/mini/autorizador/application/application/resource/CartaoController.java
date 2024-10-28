package br.com.vr.mini.autorizador.application.application.resource;

import br.com.vr.mini.autorizador.application.application.request.CartaoRequest;
import br.com.vr.mini.autorizador.application.application.response.CartaoResponse;
import br.com.vr.mini.autorizador.domain.domain.service.CartaoService;
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
import java.math.BigDecimal;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    private final CartaoService service;

    @Operation(
            security = @SecurityRequirement(name = "basic"),
            summary = "Cria um novo cartão",
            description = "Este endpoint cria um novo cartão com os dados fornecidos. " +
                    "O número do cartão deve ter exatamente 16 dígitos e a senha deve ter exatamente 4 dígitos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CartaoRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cartão criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CartaoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "409", description = "Conflito ao criar cartão (ex.: número de cartão já existe)",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<CartaoResponse> criarCartao(@Valid @RequestBody CartaoRequest request) {
        return new ResponseEntity<>(this.service.criarCartao(request), HttpStatus.CREATED);
    }

    @Operation(security = @SecurityRequirement(name = "basic"),
            summary = "Obtém o saldo do cartão",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saldo obtido com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = "number", format = "float", example = "150.75"))),
                    @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
            })
    @GetMapping("/{numeroCartao}")
    public BigDecimal obterSaldo(@PathVariable final String numeroCartao) {
        return this.service.obterSaldo(numeroCartao);
    }
}
