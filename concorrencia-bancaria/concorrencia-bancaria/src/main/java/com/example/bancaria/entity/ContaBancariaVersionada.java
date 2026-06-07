package com.example.bancaria.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conta_versionada")
public class ContaBancariaVersionada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Version
    private Integer version;

    private LocalDateTime ultimaAtualizacao;

    public ContaBancariaVersionada() {}

    public ContaBancariaVersionada(BigDecimal saldoInicial) {
        this.saldo = saldoInicial;
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public LocalDateTime getUltimaAtualizacao() { return ultimaAtualizacao; }
    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) { 
        this.ultimaAtualizacao = ultimaAtualizacao; 
    }

    // Métodos de negócio
    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo");
        }
        this.saldo = this.saldo.add(valor);
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public void sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo");
        }
        if (this.saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        this.saldo = this.saldo.subtract(valor);
        this.ultimaAtualizacao = LocalDateTime.now();
    }
}