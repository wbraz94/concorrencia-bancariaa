package com.example.bancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bancaria.entity.ContaBancaria;
import com.example.bancaria.entity.ContaBancariaVersionada;

@Repository
public interface ContaRepository extends JpaRepository<ContaBancaria, Long> {

    public void save(ContaBancariaVersionada contaBancariaVersionada);
}