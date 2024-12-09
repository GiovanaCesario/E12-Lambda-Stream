package org.example.conta.contaDAO;

public class ContaNaoEncontradaException extends Exception{

    public ContaNaoEncontradaException(int numero) {

        super("Conta de numero " + numero+ " nao encontrada.");
    }
}
