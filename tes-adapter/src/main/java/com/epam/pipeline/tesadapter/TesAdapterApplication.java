package com.epam.pipeline.tesadapter;

import com.epam.pipeline.tesadapter.configuration.TesSwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan("com.epam.pipeline.tesadapter.service")
@Import({TesSwaggerConfig.class})
public class TesAdapterApplication {
    public static void main(String[] args) {
        SpringApplication.run(TesAdapterApplication.class, args);
    }
}
