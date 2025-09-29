package com.example.notesappbackend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration and UI theming using Ocean Professional color scheme.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public io.swagger.v3.oas.models.OpenAPI baseOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info()
                        .title("Notes API - Ocean Professional")
                        .version("1.0.0")
                        .description("Modern, minimalist Notes API"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Docs")
                        .url("https://example.com/docs"));
    }

    @Bean
    public SwaggerIndexTransformer swaggerIndexTransformer(SwaggerUiConfigProperties config) {
        // Inject custom styles for Ocean Professional theme.
        String style = """
                <style>
                  :root {
                    --ocean-primary: #2563EB;
                    --ocean-secondary: #F59E0B;
                    --ocean-error: #EF4444;
                    --ocean-bg: #f9fafb;
                    --ocean-surface: #ffffff;
                    --ocean-text: #111827;
                  }
                  body, .swagger-ui, .swagger-ui .topbar {
                    background: var(--ocean-bg) !important;
                    color: var(--ocean-text) !important;
                  }
                  .swagger-ui .topbar, .swagger-ui .opblock {
                    border-radius: 10px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.04);
                  }
                  .swagger-ui .topbar { background: var(--ocean-surface) !important; }
                  .swagger-ui .topbar .download-url-wrapper, .swagger-ui .topbar .download-url-wrapper input {
                    border-radius: 8px !important;
                  }
                  .swagger-ui .btn.execute-op {
                    background: var(--ocean-primary) !important;
                    border-radius: 8px !important;
                  }
                  .swagger-ui .btn.authorize {
                    border-color: var(--ocean-primary) !important;
                    color: var(--ocean-primary) !important;
                    border-radius: 8px !important;
                  }
                  .swagger-ui .opblock-summary-path, .swagger-ui .opblock-summary-method, .swagger-ui .markdown p, .swagger-ui .info .title {
                    color: var(--ocean-text) !important;
                  }
                  .swagger-ui .errors-wrapper {
                    border-left: 4px solid var(--ocean-error);
                    background: #ffecec;
                    border-radius: 8px;
                  }
                </style>
                """;
        return (request, html) -> html.replace("</head>", style + "</head>");
    }
}
