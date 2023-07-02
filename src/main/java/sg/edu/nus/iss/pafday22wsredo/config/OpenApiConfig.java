package sg.edu.nus.iss.pafday22wsredo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("My PAD Day 24 lecture")
                .description("BankAccount API transaction example")
                .version("version 1.0"));
    }
}

