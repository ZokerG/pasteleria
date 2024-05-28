package com.example.pasteleria.repository;

import com.example.pasteleria.model.CarByCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarByClienteRepositorio extends JpaRepository<CarByCliente, Long>{
    List<CarByCliente> findAllByClienteId(long clienteId);
}
