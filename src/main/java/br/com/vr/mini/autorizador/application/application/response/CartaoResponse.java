package br.com.vr.mini.autorizador.application.application.response;

public record CartaoResponse(
        String senha,
        String numeroCartao
) {}