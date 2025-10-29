package com.ecommerce.productos.domain.model.gateway;

import com.ecommerce.productos.domain.model.Carrito;

import java.util.List;

public interface CarritoGateway {
    Carrito guardar(Carrito carrito);
    Carrito buscarPorUsuarioId(Long id);
    List<Carrito> listar();
    void eliminarCarrito(Long carritoId);

}
