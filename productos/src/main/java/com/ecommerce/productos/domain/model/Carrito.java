package com.ecommerce.productos.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Carrito {
    private Long CarritoId;
    private Long usuarioId;
    private List<ItemCarrito> items;
    private Double precioTotal;
}
