package com.example.pasteleria.repository;

import com.example.pasteleria.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByEmail(String email);

    List<Usuarios> findAllByRol(String rol);

    boolean existsByEmail(String email);
}
