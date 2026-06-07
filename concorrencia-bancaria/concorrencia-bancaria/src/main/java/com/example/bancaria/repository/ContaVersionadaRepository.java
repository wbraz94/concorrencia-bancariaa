package com.example.bancaria.repository;

import com.example.bancaria.entity.ContaBancariaVersionada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaVersionadaRepository extends JpaRepository<ContaBancariaVersionada, Long> {
}