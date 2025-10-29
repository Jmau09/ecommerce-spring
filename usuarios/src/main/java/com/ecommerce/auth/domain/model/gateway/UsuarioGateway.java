package com.ecommerce.auth.domain.model.gateway;
//los metodos (guardar, registrar usuario, etc)

//puerta de enlace de dominio a infraestructura

import com.ecommerce.auth.domain.model.Usuario;

//Funcion:nombre del funcion, lo que se va retornar, patrones de entrada y logica de la funcion
public interface UsuarioGateway {

    //Guardar,eliminar, buscar van a ser el caso de uso y sera la logica de negocio
    Usuario guardarUsuario(Usuario usuario);

    void eliminarUsuarioPorID (Long id);

    Usuario buscarUsuarioPorID(Long id);

    Usuario actualizarUsuario(Usuario usuario);

    //busca al usuario por email
Usuario buscarPorEmail (String email);


}

