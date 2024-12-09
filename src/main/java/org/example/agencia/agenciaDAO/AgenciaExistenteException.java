package org.example.agencia.agenciaDAO;

public class AgenciaExistenteException extends Exception  {

    public AgenciaExistenteException(int numero) {

        super("Agencia com numero " + numero + " ja existente no banco de dados.");
    }
}
