package com.ecommerce.productos.application.config;
import com.ecommerce.productos.domain.model.gateway.UsuarioGateway;
import com.ecommerce.productos.domain.model.gateway.CarritoGateway;
import com.ecommerce.productos.domain.model.gateway.ProductosGateway;
import com.ecommerce.productos.domain.usecase.CarritoUseCase;
import com.ecommerce.productos.domain.usecase.ProductosUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductosUseCaseConfig {

    @Bean
    public ProductosUseCase productoUseCase(ProductosGateway productosGateway) {
        return new ProductosUseCase(productosGateway);
    }

    @Bean
    public CarritoUseCase carritoUseCase(CarritoGateway carritoGateway,
                                         ProductosUseCase productoUseCase,
                                         UsuarioGateway usuarioGateway) {
        return new CarritoUseCase(carritoGateway, productoUseCase, usuarioGateway);
    }
}

