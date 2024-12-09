package org.example.cliente.clienteDAO;

public class ClienteNaoEncontradoException extends Exception{

    public ClienteNaoEncontradoException(String cpf) {

        super("Cliente de cpf " + cpf + " nao encontrado.");
    }
}
