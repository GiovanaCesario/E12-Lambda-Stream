package org.example.agencia;

import org.example.banco.Banco;
import org.example.conta.Conta;

import java.util.ArrayList;
import java.util.List;

public class Agencia {

    //Atributos
    final private int numero;
    private String endereco;
    private String email;

    private Banco banco;
    private List<Conta> contas;

    //Construtor
    public Agencia(int numero, String endereco, String email, Banco banco) {

        this.numero = numero;
        this.endereco = endereco;
        this.email = email;
        this.contas = new ArrayList<>();
        this.banco = banco;
    }

    //Associacao
    public void associaConta(Conta conta) {

        this.contas.add(conta);
        conta.setAgencia(this);
    }

    //Desassociacao
    public void desassociaConta(Conta conta) {

        this.contas.remove(conta);
        conta.setAgencia(null);
    }

    @Override
    public String toString() {
        return "Agencia{" +
                "numero=" + numero +
                ", endereco='" + endereco + '\'' +
                ", email='" + email + '\'' +
                ", banco=" + banco +
                ", contas=" + contas +
                '}';
    }

    //Getters e Setters
    public int getNumero() { return numero; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Banco getBanco() { return banco; }
    public void setBanco(Banco banco) { this.banco = banco;}

    public List<Conta> getContas() { return contas; }
    public void setContas(List<Conta> contas) { this.contas = contas; }
}
