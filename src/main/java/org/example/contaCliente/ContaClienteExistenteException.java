package org.example.contaCliente;

public class ContaClienteExistenteException extends Exception{

    public ContaClienteExistenteException(int numeroConta, String cpf) {

        super("Relacionamento já existe entre a conta " +numeroConta+  " e o cliente de cpf " +cpf);
    }
}
