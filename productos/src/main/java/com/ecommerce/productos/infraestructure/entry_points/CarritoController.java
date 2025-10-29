package com.ecommerce.productos.infraestructure.entry_points;

import com.ecommerce.productos.domain.exception.*;
import com.ecommerce.productos.domain.model.Carrito;
import com.ecommerce.productos.domain.model.ItemCarrito;
import com.ecommerce.productos.domain.usecase.CarritoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/carrito")
@RequiredArgsConstructor

public class CarritoController {

    private final CarritoUseCase carritoUseCase;

    @PostMapping("/add")
    public ResponseEntity<String> agregar(@RequestParam Long usuarioId,
                                           @RequestBody ItemCarrito itemCarrito) {
        try {
            carritoUseCase.guardar(usuarioId, itemCarrito);
            return ResponseEntity.ok("Producto agregado al carrito del usuario con el ID:"+usuarioId);
        } catch (UsuarioNoEncontradoException exception) {
            return ResponseEntity.status(HttpStatus.OK).body("El usuario con ID " + usuarioId + " no existe en la base de datos.");
        }catch (ProductoNoEncontradoException exception) {
            return ResponseEntity.status(HttpStatus.OK).body("El producto con ID:" + itemCarrito.getProductoId() + " no existe en la base de datos.");
        }catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del servicio");
        }
    }

    @GetMapping("/ver")
    public ResponseEntity<List<ItemCarrito>> verCarrito(@RequestParam Long usuarioId) {
        List<ItemCarrito> items = carritoUseCase.obtenerItemsDelCarrito(usuarioId);
        return ResponseEntity.ok(items);
    }


    @DeleteMapping("/vaciar/{usuarioId}")
    public ResponseEntity<String> vaciar(@PathVariable Long usuarioId) {
        try {
            carritoUseCase.vaciarCarrito(usuarioId);
            return ResponseEntity.ok("Carrito vaciado correctamente");
        } catch (CarritoNoFoundException exception) {
            return ResponseEntity.status(HttpStatus.OK).body(exception.getMessage());
        }
    }


    @DeleteMapping("/eliminar/{productoId}")
    public ResponseEntity<String> eliminarItemDelCarrito(
            @RequestParam Long usuarioId,
            @PathVariable Long productoId) {
        try {
            Carrito carritoActualizado = carritoUseCase.eliminarItemDelCarrito(usuarioId, productoId);

            if (carritoActualizado.getItems().isEmpty()) {
                return ResponseEntity.ok("El carrito quedó vacío después de eliminar el producto con ID: " + productoId);
            }

            return ResponseEntity.ok("Producto con ID " + productoId +
                    " eliminado correctamente del carrito del usuario con ID: " + usuarioId);

        } catch (ProductoNoEncontradoCarritoExcepcion e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (CarritoNoFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Carrito no encontrado para el usuario con ID: " + usuarioId);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar producto del carrito: " + e.getMessage());
        }
    }


    @PostMapping("/comprar/{usuarioId}")
    public ResponseEntity<String> venderCarrito(@PathVariable Long usuarioId) {
        try {
            carritoUseCase.comprar(usuarioId);
            return ResponseEntity.ok("Compra realizada correctamente. Carrito eliminado.");
        } catch (UsuarioNoEncontradoException | CarritoNoFoundException | StockNoFoundExepction exception) {
            return ResponseEntity.status(HttpStatus.OK).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Error al procesar la compra: " + exception.getMessage());
        }
    }

}
