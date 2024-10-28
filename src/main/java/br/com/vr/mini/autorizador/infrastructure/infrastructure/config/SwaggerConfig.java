package br.com.vr.mini.autorizador.infrastructure.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        String securitySchemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API Mini autorizador")
                        .version("1.0.0")
                        .description("Esta é a API para criação de cartões, obtenção de saldo do cartão e autorização de transações.")
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .schemaRequirement(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic"));
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("cartoes")
                .pathsToMatch("/cartoes/**", "/transacoes/**")
                .build();
    }
}
