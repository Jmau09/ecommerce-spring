package com.ecommerce.productos.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarrito {
    private Long ItemCarritoId;
    private Long carritoId;
    private Long productoId;
    private  String nombreProducto;
    private  Double precioUnitario;
    private Integer cantidad;
    private Double subtotal;
}
