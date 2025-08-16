package com.plataformamarcenaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // Import necessário para a anotação

@SpringBootApplication
@ComponentScan(basePackages = "com.plataformamarcenaria") // Adicionando a anotação que vai forçar o scan
public class PlataformaMercenariaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlataformaMercenariaApplication.class, args);
    }
}