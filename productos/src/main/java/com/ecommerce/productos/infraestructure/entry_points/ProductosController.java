package com.ecommerce.productos.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.productos.domain.model.Productos;
import com.ecommerce.productos.domain.usecase.ProductosUseCase;
import com.ecommerce.productos.infraestructure.driver_adapters.jpa_repository.productos.ProductosData;
import com.ecommerce.productos.infraestructure.mapper.MapperProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //indica que esta clase es un controlador, y se van a crear APIs que se van a exponer
@RequestMapping("/api/ecommerce/productos") //parametrizar URLs
@RequiredArgsConstructor //crea constructores

public class ProductosController {

    private final ProductosUseCase productosUseCase;
    private final MapperProducto mapperProducto;

    @PostMapping("/save")
    public ResponseEntity<String> saveProductos(@RequestBody ProductosData productosData) {
        Productos productos = mapperProducto.toProductos(productosData);
        String resultado = productosUseCase.guardarProducto(productos);

        if (resultado.startsWith("Producto guardado!")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Productos> findByIdUsuario(@PathVariable Long id) {
        try {
            Productos productoEncontrado = productosUseCase.buscarProductoPorId(id);

            if (productoEncontrado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(productoEncontrado);

        } catch (Exception e) {
            // Esto previene que cualquier error interno rompa el servidor
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }





    @PutMapping("/update")
    public ResponseEntity<Productos> actualizarProducto(@RequestBody ProductosData productoData) {
        try {
            Productos producto = mapperProducto.toProductos(productoData);
            Productos productoValidadoActualizado = productosUseCase.actualizarProducto(producto);
            return new ResponseEntity<>(productoValidadoActualizado, HttpStatus.OK);

        } catch (Exception error) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductos(@PathVariable Long id){
        try {
            Productos producto = productosUseCase.buscarProductoPorId(id);
           if (producto == null) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND)
                       .body("El producto con el ID:"+id+" no exite en la BD");
           }
           productosUseCase.eliminarProducto(id);
           return ResponseEntity.ok().body("Producto eliminado");
        } catch (Exception error) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Productos>> obtenerUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<Productos> productos = productosUseCase.obtenerProductos(page, size);
        return ResponseEntity.ok(productos);
    }



}