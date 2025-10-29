package com.ecommerce.productos.infraestructure.driver_adapters.jpa_repository.itemcarrito;


import com.ecommerce.productos.infraestructure.driver_adapters.jpa_repository.carrito.CarritoData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_carrito")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarritoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemCarritoId;

    private Long productoId;
    private String nombreProducto;
    private Double precioUnitario;
    private Integer cantidad;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private CarritoData carrito;
}
