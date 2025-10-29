package com.ecommerce.productos.infraestructure.mapper;

import com.ecommerce.productos.domain.model.Productos;
import com.ecommerce.productos.infraestructure.driver_adapters.jpa_repository.productos.ProductosData;
import org.springframework.stereotype.Component;

@Component
public class MapperProducto {

    public Productos toProductos(ProductosData productosData) {
        return  new Productos(
                productosData.getId(),
                productosData.getNombre(),
                productosData.getDescripcion(),
                productosData.getPrecio(),
                productosData.getStock(),
                productosData.getImagenUrl()
        );
    }

    public ProductosData toProductosData(Productos productos) {
        return new ProductosData(
                productos.getId(),
                productos.getNombre(),
                productos.getDescripcion(),
                productos.getPrecio(),
                productos.getStock(),
                productos.getImagenUrl()
        );
    }
}