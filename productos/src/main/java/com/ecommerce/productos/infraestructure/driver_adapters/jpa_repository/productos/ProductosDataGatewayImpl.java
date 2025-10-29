package com.ecommerce.productos.infraestructure.driver_adapters.jpa_repository.productos;

import com.ecommerce.productos.domain.model.Productos;
import com.ecommerce.productos.domain.model.gateway.ProductosGateway;
import com.ecommerce.productos.infraestructure.mapper.MapperProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //indica que la clase almacena, guarda, elimina en la base de datos, tiene sql
//esa clase hace las consultas a la BD

@RequiredArgsConstructor
public class ProductosDataGatewayImpl implements ProductosGateway {
    private final MapperProducto mapperProducto;
    private final ProductosDataJpaRepository repository;

    @Override
    public Productos guardarProducto(Productos producto) {
        ProductosData productosData = mapperProducto.toProductosData(producto);
        return  mapperProducto.toProductos(repository.save(productosData));
    }

    @Override
    public Productos buscarProductoPorId(Long id) {
        return repository.findById(id)
                .map(mapperProducto::toProductos)
                .orElse(null);
    }




    @Override
    public Productos actualizarProducto(Productos producto) {

        ProductosData  productoDataActualizar = mapperProducto.toProductosData(producto);

        if(!repository.existsById(productoDataActualizar.getId())){
            throw new RuntimeException("Usuario con id" + productoDataActualizar.getId() + "no existe");
        }
        return mapperProducto.toProductos(repository.save(productoDataActualizar));
    }

    @Override
    public void eliminarProducto(Long id) {
        try{
            repository.deleteById(id);
        }catch(Exception e){
            throw new RuntimeException("Error al eliminar el producto"+e.getMessage());
        }
    }

    @Override
    public List<Productos> obtenerProductos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductosData> productoDataPage = repository.findAll(pageable);

        return productoDataPage.getContent()
                .stream()
                .map(mapperProducto::toProductos)
                .toList();
    }




}