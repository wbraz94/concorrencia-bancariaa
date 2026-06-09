package com.example.bancaria.service;

import com.example.bancaria.entity.ContaBancariaVersionada;
import com.example.bancaria.repository.ContaVersionadaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ContaVersionadaService {

    @Autowired
    private ContaVersionadaRepository repository;

    @Transactional
    public ContaBancariaVersionada criarConta(BigDecimal saldoInicial) {
        ContaBancariaVersionada conta = new ContaBancariaVersionada(saldoInicial);
        return repository.save(conta);
    }

    @Transactional
    public void depositar(Long id, BigDecimal valor) {
        try {
            ContaBancariaVersionada conta = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
            
            System.out.println("💰 Depósito: R$" + valor + " | Versão atual: " + conta.getVersion());
            conta.depositar(valor);
            repository.save(conta);
            System.out.println("✅ Depósito OK | Nova versão: " + conta.getVersion());
            
        } catch (ObjectOptimisticLockingFailureException e) {
            System.err.println("⚠️ CONFLITO! Outra thread modificou a conta " + id);
            throw new RuntimeException("CONFLITO: Tente novamente", e);
        }
    }

    @Transactional
    public void sacar(Long id, BigDecimal valor) {
        try {
            ContaBancariaVersionada conta = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
            
            System.out.println("💰 Saque: R$" + valor + " | Versão atual: " + conta.getVersion());
            conta.sacar(valor);
            repository.save(conta);
            System.out.println("✅ Saque OK | Nova versão: " + conta.getVersion());
            
        } catch (ObjectOptimisticLockingFailureException e) {
            System.err.println("⚠️ CONFLITO! Outra thread modificou a conta " + id);
            throw new RuntimeException("CONFLITO: Tente novamente", e);
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal consultarSaldo(Long id) {
        ContaBancariaVersionada conta = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        return conta.getSaldo();
    }
}