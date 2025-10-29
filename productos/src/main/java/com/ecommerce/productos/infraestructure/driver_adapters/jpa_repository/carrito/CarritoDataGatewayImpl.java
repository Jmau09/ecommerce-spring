package com.ecommerce.productos.infraestructure.driver_adapters.jpa_repository.carrito;


import com.ecommerce.productos.domain.model.Carrito;
import com.ecommerce.productos.domain.model.gateway.CarritoGateway;
import com.ecommerce.productos.infraestructure.mapper.MapperCarrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor


public class CarritoDataGatewayImpl  implements CarritoGateway {
    private final CarritoDataJpaRepository repository;
    private final MapperCarrito mapperCarrito;

    @Override
    public Carrito guardar(Carrito carrito) {
        var entity = mapperCarrito.toData(carrito);
        return mapperCarrito.toDomain(repository.save(entity));
    }

    @Override
    public Carrito buscarPorUsuarioId(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .map(mapperCarrito::toDomain)
                .orElse(null);
    }
    @Override
    public List<Carrito> listar() {
        return repository.findAll()
                .stream()
                .map(mapperCarrito::toDomain)
                .toList();
    }


    @Override
    public void eliminarCarrito(Long carritoId) {
        repository.deleteById(carritoId);
    }
}
