package com.ecommerce.productos.domain.model.gateway;

import com.ecommerce.productos.domain.model.Productos;

import java.util.List;

public interface ProductosGateway {
    Productos guardarProducto(Productos producto);
    Productos buscarProductoPorId(Long id);
    Productos actualizarProducto(Productos producto);
    void eliminarProducto(Long id);
    List<Productos> obtenerProductos(int page, int size);
}