package com.example.bancaria.repository;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bancaria.entity.ContaBancaria;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ContaRepository repository;

    public DataInitializer(ContaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Cria a conta ID 1 com R$ 1000,00 de saldo inicial
        repository.save(new ContaBancaria(new BigDecimal("1000.00")));
    }
}