package com.ecommerce.auth.infraestructure.driver_adapter.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioDataJpaRepository extends JpaRepository <UsuarioData, Long> {

    //Consulta email a la base de datos
    Optional<UsuarioData> findByEmail(String email);

}
