package org.example.cliente;

import org.example.conta.Conta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cliente {

    //Atributos
    final private String cpf;
    private String nome;
    private String email;

    private List<Conta> contas;

    //Construtor
    public Cliente(String cpf, String nome, String email) {

        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.contas = new ArrayList<>();
    }

    //Associa
    public void associaConta(Conta conta) {

        this.contas.add(conta);
    }

    //Desassocia
    public void desassociaConta(Conta conta) {

        this.contas.remove(conta);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "cpf=" + cpf +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", contas=" + contas +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente cliente)) return false;
        return Objects.equals(cpf, cliente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cpf);
    }

    //Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Conta> getContas() { return contas; }
    public void setContas(List<Conta> contas) { this.contas = contas; }
}
