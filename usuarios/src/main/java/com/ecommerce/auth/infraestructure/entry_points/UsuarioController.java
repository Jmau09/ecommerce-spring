package com.ecommerce.auth.infraestructure.entry_points;

import com.ecommerce.auth.domain.model.Usuario;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import com.ecommerce.auth.infraestructure.driver_adapter.jpa_repository.UsuarioData;
import com.ecommerce.auth.infraestructure.mapper.MapperUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //indica que esta clase es un controlador, y se van a crear APIs que se van a exponer
@RequestMapping("/api/ecommerce/usuario") //parametrizar URLs
@RequiredArgsConstructor //crea constructores
public class UsuarioController {

    //disparador
    //las APIs las consume el front
    //El front va conectado a un botòn
    //el boton consume la api

    private final UsuarioUseCase usuarioUseCase;
    private final MapperUsuario mapperUsuario;

    @PostMapping("/save")
    public ResponseEntity<String> saveUsuario(@RequestBody UsuarioData usuarioData) {
        //responseEntity es un tipo de obj que devuelve un statuscode
        //engloba el usuario y devuelve usuario y metadata
        //da el HttpStatusCode (son codigos que se deben devolver al consumir una API)
        //400 es error del usuario - 500 es error del programador
        //requestbody hace el mapeo. convierte el contenido de la peticion http
        //se debe pasar primero por los casos de uso
        Usuario usuario=mapperUsuario.toUsuario(usuarioData);
        String resultado  = usuarioUseCase.guardarUsuario(usuario);

        if (resultado.startsWith("Usuario guardado")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else  {
            return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    //que pase el obj por la URL, y no por un body
    public ResponseEntity<?> findByIdUsuario(@PathVariable Long id){
        Usuario usuarioEncontrado = usuarioUseCase.buscarPorIdUsuario(id);
        if (usuarioEncontrado == null){
            String mensaje = "No existe el usuario con el ID " + id;
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuarioEncontrado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    //que pase el obj por la URL, y no por un body
    public ResponseEntity<String> deleteByIdUsuario(@PathVariable Long id){
        try {
            Usuario usuario = usuarioUseCase.buscarPorIdUsuario(id);
            if(usuario==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El usuario con el ID:"+id+" no exite en la BD");
            }
            usuarioUseCase.eliminarPorIdUsuario(id);
            //siempre se debe retornar un HTTP status
            return ResponseEntity.ok().body("Usuario eliminado exitosamente");
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUsuario(@RequestBody UsuarioData usuarioData) {
        try {
            Usuario usuario = mapperUsuario.toUsuario(usuarioData);
            Usuario usuarioActualizado = usuarioUseCase.actualizarUsuario(usuario);

            if (usuarioActualizado == null) {
                return new ResponseEntity<>("El ID no existe en la base de datos", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>("Usuario actualizado correctamente", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/login")
    //recibe el email y password, llama al caso de uso, y retorna el usuario autenticado.
public ResponseEntity<Object> loginUsuario(@RequestBody UsuarioData usuarioData){
        try{
            String mensajeRTA = usuarioUseCase.loginUsuario(usuarioData.getEmail(), usuarioData.getPassword());
        return new ResponseEntity<>(mensajeRTA, HttpStatus.OK);

        } catch (Exception error) {
            return  new ResponseEntity<>("Falló el logueo", HttpStatus.CONFLICT);
        }
    }


}
