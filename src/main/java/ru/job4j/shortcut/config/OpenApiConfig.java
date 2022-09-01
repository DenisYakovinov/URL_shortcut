package ru.job4j.shortcut.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${swagger-server}") String swaggerApiServer) {
        Server defaultServer = new Server();
        defaultServer.setUrl(swaggerApiServer);
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("URL shortcut Application API").version("1.0.0"))
                .servers(List.of(defaultServer))
                .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomiser loginEndpointCustomizer() {
        Schema requestModel = new Schema()
                .addProperty("login", new StringSchema())
                .addProperty("password", new StringSchema());
        Schema responseModel = new Schema()
                .addProperty("Authorization", new StringSchema());
        RequestBody requestBody = new RequestBody().content(new Content().addMediaType("*/*",
                        new MediaType().schema(requestModel))).required(true);
        ApiResponses apiResponses = new ApiResponses().addApiResponse("200", new ApiResponse().description("OK")
                .content(new Content().addMediaType("*/*", new MediaType().schema(responseModel))));
        PathItem pathItem = new PathItem().post(new Operation().tags(List.of("login-endpoint"))
                                                               .operationId("login")
                                                               .requestBody(requestBody)
                                                               .responses(apiResponses)
                                                               .description("returns a JWT token"));
        return openApi -> openApi.path("/login", pathItem);
    }
}