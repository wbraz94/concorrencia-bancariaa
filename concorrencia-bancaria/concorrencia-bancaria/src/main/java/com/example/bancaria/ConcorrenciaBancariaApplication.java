package com.example.bancaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConcorrenciaBancariaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConcorrenciaBancariaApplication.class, args);
        System.out.println("✅ Aplicação rodando em: http://localhost:8080");
        System.out.println("📊 H2 Console: http://localhost:8080/h2-console");
    }
}