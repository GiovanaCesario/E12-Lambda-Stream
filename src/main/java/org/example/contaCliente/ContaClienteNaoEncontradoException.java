package org.example.contaCliente;

public class ContaClienteNaoEncontradoException extends Exception{

    public ContaClienteNaoEncontradoException(int numeroConta, String cpf) {

        super("Relacionamento entre a conta " +numeroConta+  " e o cliente de cpf " +cpf+ " nao foi encontrado");
    }
}
