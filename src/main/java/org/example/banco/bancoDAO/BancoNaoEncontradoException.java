package org.example.banco.bancoDAO;

public class BancoNaoEncontradoException extends Exception{

    public BancoNaoEncontradoException(String cnpj) {

        super("Banco de cnpj " + cnpj + " nao encontrado.");
    }
}
