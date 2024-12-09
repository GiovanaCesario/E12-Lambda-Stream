package org.example.conta.contaDAO;

public class ContaExistenteException extends Exception{

    public ContaExistenteException(int numero) {

        super("Conta com numero " + numero + " ja existente no banco de dados.");
    }
}
