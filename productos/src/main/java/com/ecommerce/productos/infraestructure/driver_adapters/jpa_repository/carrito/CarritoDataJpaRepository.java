package com.ecommerce.productos.infraestructure.driver_adapters.jpa_repository.carrito;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoDataJpaRepository extends JpaRepository<CarritoData, Long> {
    Optional<CarritoData> findByUsuarioId(Long usuarioId);
}
