package com.example.bancaria.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bancaria.service.ContaService;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService service;

    public ContaController(ContaService service) {
        this.service = service;
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<Void> depositar(@PathVariable @NonNull Long id, @RequestBody Map<String, BigDecimal> payload) {
        BigDecimal valor = payload.get("valor");
        service.depositar(id, valor);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<Void> sacar(@PathVariable @NonNull Long id, @RequestBody Map<String, BigDecimal> payload) {
        BigDecimal valor = payload.get("valor");
        service.sacar(id, valor);
        return ResponseEntity.ok().build();
    }
}