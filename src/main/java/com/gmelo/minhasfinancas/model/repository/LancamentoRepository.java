package com.gmelo.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmelo.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
