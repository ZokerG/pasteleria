package com.example.pasteleria.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.TimeZone;

@Configuration
public class LocaleConfig {

    @PostConstruct
    public void init() {
        Locale.setDefault(new Locale("es", "CO"));
        TimeZone.setDefault(TimeZone.getTimeZone("America/Bogota"));
    }
}
