package org.example.banco.bancoDAO;

public class BancoExistenteException extends Exception{

    public BancoExistenteException(String cnpj) {

        super("Banco com cnpj " + cnpj + " ja existente no banco de dados.");
    }
}
