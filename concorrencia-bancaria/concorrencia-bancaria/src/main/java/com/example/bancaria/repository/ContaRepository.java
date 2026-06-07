package com.example.bancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.bancaria.entity.ContaBancaria;

@Repository
public interface ContaRepository extends JpaRepository<ContaBancaria, Long> {
}