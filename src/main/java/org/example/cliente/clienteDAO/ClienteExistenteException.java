package org.example.cliente.clienteDAO;

public class ClienteExistenteException extends Exception{

    public ClienteExistenteException(String cpf) {

        super("Cliente com cpf " + cpf + " ja existente no banco de dados.");
    }
}
