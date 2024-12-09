package org.example.conexao;

public class FalhaConexaoException extends Exception {

    public FalhaConexaoException(String mensagem) {
        super(mensagem);
    }
}