package com.ecommerce.productos.domain.usecase;

import com.ecommerce.productos.domain.exception.*;
import com.ecommerce.productos.domain.model.gateway.UsuarioGateway;
import com.ecommerce.productos.domain.model.Carrito;
import com.ecommerce.productos.domain.model.ItemCarrito;
import com.ecommerce.productos.domain.model.Productos;
import com.ecommerce.productos.domain.model.gateway.CarritoGateway;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CarritoUseCase {

    private final CarritoGateway carritoGateway;
    private final ProductosUseCase productosUseCase;
    private final UsuarioGateway usuarioGateway;

    public Carrito guardar(Long usuarioId, ItemCarrito itemCarrito) {

        if(!usuarioGateway.usuarioExiste(usuarioId)){
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID" +  usuarioId);
        }
        Productos producto = productosUseCase.buscarProductoPorId(itemCarrito.getProductoId());
        if (producto == null) {
            throw new ProductoNoEncontradoException("Producto no encontrado con ID: " + itemCarrito.getProductoId());
        }
        itemCarrito.setNombreProducto(producto.getNombre());
        itemCarrito.setPrecioUnitario(producto.getPrecio());
        itemCarrito.setSubtotal(producto.getPrecio()*itemCarrito.getCantidad());
        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);

        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuarioId(usuarioId);
            carrito.setItems(new java.util.ArrayList<>());
        }

        Optional<ItemCarrito> existente = carrito.getItems()
                .stream()
                .filter(item -> item.getProductoId().equals(itemCarrito.getProductoId()))
                .findFirst();

        if (existente.isPresent()) {
            ItemCarrito itemExistente = existente.get();
            itemExistente.setCantidad(itemExistente.getCantidad() + itemCarrito.getCantidad());
            itemExistente.setSubtotal(itemExistente.getPrecioUnitario() * itemExistente.getCantidad());
        } else {
            carrito.getItems().add(itemCarrito);
        }

        double total = carrito.getItems().stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();

        carrito.setPrecioTotal(total);
        return carritoGateway.guardar(carrito);
    }

    public List<ItemCarrito> obtenerItemsDelCarrito(Long usuarioId) {
        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);

        if (carrito == null || carrito.getItems() == null) {
            return Collections.emptyList();
        }

        return carrito.getItems();
    }



    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);

        if (carrito == null) {
            throw new CarritoNoFoundException("No se encontró carrito para el usuario con ID: " + usuarioId);
        }

        carritoGateway.eliminarCarrito(carrito.getCarritoId());
    }

    public Carrito eliminarItemDelCarrito(Long usuarioId, Long productoId) {
        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);

        if (carrito == null) {
            throw new CarritoNoFoundException("Carrito no encontrado para el usuario ID: " + usuarioId);
        }

        boolean productoExisteCarrito = carrito .getItems().stream()
                .anyMatch(item -> item.getProductoId().equals(productoId));

        if (!productoExisteCarrito) {
            throw new ProductoNoEncontradoCarritoExcepcion
                    ("El producto con ID " + productoId + " no se encuentra en el carrito del usuario con ID: " + usuarioId);
        }
        List<ItemCarrito> itemsFiltrados = carrito.getItems()
                .stream()
                .filter(item -> !item.getProductoId().equals(productoId))
                .collect(Collectors.toList());

        carrito.setItems(itemsFiltrados);

        double total = itemsFiltrados.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();

        carrito.setPrecioTotal(total);

        return carritoGateway.guardar(carrito);
    }

    public void comprar(Long usuarioId) {

        // Verificar que el usuario exista
        if (!usuarioGateway.usuarioExiste(usuarioId)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + usuarioId);
        }

        // Buscar el carrito del usuario
        Carrito carrito = carritoGateway.buscarPorUsuarioId(usuarioId);
        if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new CarritoNoFoundException("Carrito no encontrado o vacío para el usuario: " + usuarioId);
        }

        // Verificar stock disponible
        boolean stockInsuficiente = carrito.getItems().stream()
                .anyMatch(itemCarrito -> {
                    Productos producto = productosUseCase.buscarProductoPorId(itemCarrito.getProductoId());
                    return producto == null || producto.getStock() < itemCarrito.getCantidad();
                });

        if (stockInsuficiente) {
            throw new StockNoFoundExepction("No hay suficiente stock para uno o más productos.");
        }

        // Actualizar stock de cada producto
        carrito.getItems().forEach(itemCarrito -> {
            Productos producto = productosUseCase.buscarProductoPorId(itemCarrito.getProductoId());
            producto.setStock(producto.getStock() - itemCarrito.getCantidad());
            productosUseCase.guardarProducto(producto);
        });

        // Eliminar el carrito e ítems
        carritoGateway.eliminarCarrito(carrito.getCarritoId());

    }
}


