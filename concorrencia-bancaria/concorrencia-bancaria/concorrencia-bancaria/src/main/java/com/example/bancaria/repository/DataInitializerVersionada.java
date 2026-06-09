package com.example.bancaria.repository;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bancaria.entity.ContaBancariaVersionada;

@Component
public class DataInitializerVersionada implements CommandLineRunner {

    private final ContaRepository repository;

    public DataInitializerVersionada(ContaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Cria a conta ID 1 com R$ 1000,00 de saldo inicial
        repository.save(new ContaBancariaVersionada(new BigDecimal("1000.00")));
    }
}