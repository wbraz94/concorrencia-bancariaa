package com.example.bancaria.controller;

import com.example.bancaria.service.ContaVersionadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/contas")
public class ContaVersionadaController {

    @Autowired
    private ContaVersionadaService service;

    @PostMapping
    public ResponseEntity<?> criarConta(@RequestBody Map<String, BigDecimal> request) {
        BigDecimal saldoInicial = request.getOrDefault("saldoInicial", BigDecimal.ZERO);
        var conta = service.criarConta(saldoInicial);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "id", conta.getId(),
            "saldo", conta.getSaldo(),
            "versao", conta.getVersion()
        ));
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<?> depositar(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        try {
            BigDecimal valor = request.get("valor");
            service.depositar(id, valor);
            BigDecimal saldo = service.consultarSaldo(id);
            return ResponseEntity.ok(Map.of(
                "status", "sucesso",
                "operacao", "depósito",
                "valor", valor,
                "saldo_atual", saldo
            ));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("CONFLITO")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "erro", "Conflito de concorrência",
                    "mensagem", "A conta foi modificada por outra operação. Tente novamente."
                ));
            }
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<?> sacar(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        try {
            BigDecimal valor = request.get("valor");
            service.sacar(id, valor);
            BigDecimal saldo = service.consultarSaldo(id);
            return ResponseEntity.ok(Map.of(
                "status", "sucesso",
                "operacao", "saque",
                "valor", valor,
                "saldo_atual", saldo
            ));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("CONFLITO")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "erro", "Conflito de concorrência",
                    "mensagem", "A conta foi modificada por outra operação. Tente novamente."
                ));
            }
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<?> saldo(@PathVariable Long id) {
        BigDecimal saldo = service.consultarSaldo(id);
        return ResponseEntity.ok(Map.of("saldo", saldo));
    }
}