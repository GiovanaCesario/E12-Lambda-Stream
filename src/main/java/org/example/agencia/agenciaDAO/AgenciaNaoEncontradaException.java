package org.example.agencia.agenciaDAO;

public class AgenciaNaoEncontradaException extends Exception {

    public AgenciaNaoEncontradaException(int numero) {

        super("Agencia de numero " + numero + " nao encontrada.");
    }
}
