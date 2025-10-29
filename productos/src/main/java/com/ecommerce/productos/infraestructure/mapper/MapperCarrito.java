package com.ecommerce.productos.infraestructure.mapper;

import com.ecommerce.productos.domain.model.Carrito;
import com.ecommerce.productos.domain.model.ItemCarrito;
import com.ecommerce.productos.infraestructure.driver_adapters.jpa_repository.carrito.CarritoData;
import com.ecommerce.productos.infraestructure.driver_adapters.jpa_repository.itemcarrito.ItemCarritoData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperCarrito {

    public CarritoData toData(Carrito carrito) {
        CarritoData carritoData = new CarritoData();
        carritoData.setCarritoId(carrito.getCarritoId());
        carritoData.setUsuarioId(carrito.getUsuarioId());
        carritoData.setTotal(carrito.getPrecioTotal());

        List<ItemCarritoData> items = carrito.getItems().stream()
                .map(item -> {
                    ItemCarritoData itemData = new ItemCarritoData();
                    itemData.setItemCarritoId(item.getItemCarritoId());
                    itemData.setProductoId(item.getProductoId());
                    itemData.setNombreProducto(item.getNombreProducto());
                    itemData.setPrecioUnitario(item.getPrecioUnitario());
                    itemData.setCantidad(item.getCantidad());
                    itemData.setSubtotal(item.getSubtotal());
                    itemData.setCarrito(carritoData); // importante establecer relación bidireccional
                    return itemData;
                }).collect(Collectors.toList());

        carritoData.setItems(items);
        return carritoData;
    }

    public Carrito toDomain(CarritoData carritoData) {
        Carrito carrito = new Carrito();
        carrito.setCarritoId(carritoData.getCarritoId());
        carrito.setUsuarioId(carritoData.getUsuarioId());
        carrito.setPrecioTotal(carritoData.getTotal());

        List<ItemCarrito> items = carritoData.getItems().stream()
                .map(itemData -> {
                    ItemCarrito item = new ItemCarrito();
                    item.setItemCarritoId(itemData.getItemCarritoId());
                    item.setProductoId(itemData.getProductoId());
                    item.setNombreProducto(itemData.getNombreProducto());
                    item.setPrecioUnitario(itemData.getPrecioUnitario());
                    item.setCantidad(itemData.getCantidad());
                    item.setSubtotal(itemData.getSubtotal());
                    item.setCarritoId(carritoData.getCarritoId());
                    return item;
                }).collect(Collectors.toList());

        carrito.setItems(items);
        return carrito;
    }
}



