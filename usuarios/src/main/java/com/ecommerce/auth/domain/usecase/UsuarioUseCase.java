package com.ecommerce.auth.domain.usecase;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.model.gateway.EncrypterGateway;
import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;


//logica de negocio
//no agregar dependencias ni etiquetas
@RequiredArgsConstructor


public class UsuarioUseCase {
//Guardar,eliminar, buscar (van a ser el caso de uso y sera la logica de negocio)

    //no se puede contectar la BD en la infraestructura
    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;

public String guardarUsuario(Usuario usuario){
    if (usuario.getNombre()==null || usuario.getNombre().trim().isEmpty()){
        return "El campo nombre es obligatorio";
    }
    if (usuario.getEmail()==null || usuario.getEmail().trim().isEmpty()){
        return "El campo email es obligatorio";
    }
    if (usuario.getPassword()==null || usuario.getPassword().trim().isEmpty()){
        return "El campo password es obligatorio";
    }
    if (usuario.getEdad()==null || usuario.getEdad() <= 0){
        return "El campo edad es obligatorio y debe ser mayor que 0";
    }
    if (usuario.getRole()==null || usuario.getRole().trim().isEmpty()){
        return "El campo role es obligatorio";
    }
    usuario.setPassword(encrypterGateway.encrypt(usuario.getPassword()));
    usuarioGateway.guardarUsuario(usuario);

    return "Usuario guardado correctamente";
}

    //public Usuario guardarUsuario(Usuario usuario) {

        //si da error, en los logs aparece ese mensaje
      //  if (usuario.getEmail() == null && usuario.getPassword() == null) {
            //Arrojar excepciones
        //    throw new NullPointerException("Ojo con eso manito - guardarUsuario");
        //}
        //usuario.setPassword(encrypterGateway.encrypt(usuario.getPassword()));
        //return usuarioGateway.guardar(usuario);
//        Forma 2
//        String cpasswordEncrypt = encrypterGateway.encrypt(usuario.getPassword());
//        usuario.setPassword(cpasswordEncrypt);

    public Usuario buscarPorIdUsuario(Long id) {
        try {
            Usuario usuario = usuarioGateway.buscarUsuarioPorID(id);
            return usuario; // puede venir nulo si no existe
        } catch (Exception e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
            return null;
        }
    }


    public void eliminarPorIdUsuario(Long id){
        try{
            usuarioGateway.eliminarUsuarioPorID(id);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    public Usuario actualizarUsuario(Usuario usuario) {
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("El id es obligatorio para actualizar");
        }

        // Verificar si el usuario existe en la base de datos
        Usuario usuarioExistente = usuarioGateway.buscarUsuarioPorID(usuario.getId());
        if (usuarioExistente == null) {
            // Retorna null si no existe
            return null;
        }

        // Encriptar la contraseña solo si no está vacía
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            String passwordEncrypt = encrypterGateway.encrypt(usuario.getPassword());
            usuario.setPassword(passwordEncrypt);
        }

        return usuarioGateway.actualizarUsuario(usuario);
    }


    public String loginUsuario(String email, String password) {
        Usuario usuarioLogueado = usuarioGateway.buscarPorEmail(email);
        if (usuarioLogueado.getEmail() == null || usuarioLogueado.getPassword() == null) {
            return "Usuario no encontrado";
        }

        if(encrypterGateway.checkPass(password, usuarioLogueado.getPassword())) {
            return "Credenciales correctos";
        } else  {
            return "Credenciales incorrectos";
        }
    }
}

