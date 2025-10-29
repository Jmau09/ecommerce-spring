package com.ecommerce.auth.infraestructure.driver_adapter.jpa_repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "usuario")
@Data //solo se utiliza para el tema de la base de datos

public class UsuarioData {

    //las anotaciones para los atributos van encima del atributo, significa que afectan al atributo que estè debajo
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //crea el id automaticamente
    private Long id;

    private String nombre;

    @Column(length = 30, nullable = false)
    private String email;

    private String password;
    private String role;
    private Integer edad;

}
