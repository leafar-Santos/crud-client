package com.santos.cliente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santos.cliente.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
