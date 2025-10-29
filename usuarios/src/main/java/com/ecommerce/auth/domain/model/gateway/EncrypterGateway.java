package com.ecommerce.auth.domain.model.gateway;

public interface EncrypterGateway {
    //en los puertos se implementan los metodos para realizar la conexion

    String encrypt (String password);

    boolean checkPass (String passUser, String passBD);


}
