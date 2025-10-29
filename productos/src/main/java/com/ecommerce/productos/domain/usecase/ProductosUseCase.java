package com.ecommerce.productos.domain.usecase;

import com.ecommerce.productos.domain.model.Productos;
import com.ecommerce.productos.domain.model.gateway.ProductosGateway;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class ProductosUseCase {
    private final ProductosGateway productosGateway;

    public String guardarProducto(Productos producto) {
        if (producto.getNombre() == null ||producto.getNombre().trim().isEmpty()) {
            return "El campo nombre es obligatorio";
        }
        if (producto.getPrecio() == null || producto.getPrecio().doubleValue() < 0.0){
            return "El precio es obligatorio";
        }
        if (producto.getStock() == null || producto.getStock() < 0){
            return "El stock es obligatorio";
        }
        if (producto.getImagenUrl() == null || producto.getImagenUrl().trim().isEmpty()) {
            return "Por favor ingrese la URL de la imagen";
        }
        productosGateway.guardarProducto(producto);
        return "Producto guardado!";
    }

    public Productos buscarProductoPorId(Long id) {
        try {
            return productosGateway.buscarProductoPorId(id);
        } catch (Exception error) {
            System.out.println("Error: " + error.getMessage());
            return null;
        }
    }




    public Productos actualizarProducto(Productos producto) {

        if (producto.getId() == null) {
            throw new IllegalArgumentException("El id es obligatorio");
        }
        return productosGateway.actualizarProducto(producto);
    }

    public void eliminarProducto(Long id) {
        try {
            productosGateway.eliminarProducto(id);
        } catch (Exception error) {
            System.out.println("Error al eliminar producto: " + error.getMessage());
        }
    }


    public List<Productos> obtenerProductos(int page, int size)
    {
        return productosGateway.obtenerProductos(page,size);
    }




}

