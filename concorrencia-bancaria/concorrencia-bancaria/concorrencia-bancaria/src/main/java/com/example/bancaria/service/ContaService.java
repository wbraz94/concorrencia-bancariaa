package com.example.bancaria.service;

import java.math.BigDecimal;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bancaria.entity.ContaBancaria;
import com.example.bancaria.repository.ContaRepository;

@Service
public class ContaService {

    private final ContaRepository repository;

    public ContaService(ContaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void depositar(@NonNull Long id, BigDecimal valor) {
        ContaBancaria conta = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        
        conta.setSaldo(conta.getSaldo().add(valor));
        repository.save(conta);
    }

    @Transactional
    public void sacar(@NonNull Long id, BigDecimal valor) {
        ContaBancaria conta = repository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        repository.save(conta);
    }
}