package org.example.banco;

import org.example.agencia.Agencia;

import java.util.ArrayList;
import java.util.List;

public class Banco {

    //Atributos
    final private String cnpj;
    private String nome;

    private List<Agencia> agencias;

    //Construtor
    public Banco(String cnpj, String nome) {

        this.cnpj = cnpj;
        this.nome = nome;
        this.agencias = new ArrayList<>();
    }

    //Associacao
    public void associaAgencia(Agencia agencia) {

        this.agencias.add(agencia);
        agencia.setBanco(this);
    }

    //Desassociacao
    public void dessasociaAgencia(Agencia agencia) {

        this.agencias.remove(agencia);
        agencia.setBanco(null);
    }

    @Override
    public String toString() {
        return "Banco{" +
                "cnpj=" + cnpj +
                ", nome='" + nome + '\'' +
                ", agencias=" + agencias +
                '}';
    }

    //Getters e Setters
    public String  getCnpj() { return cnpj; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Agencia> getAgencias() { return agencias; }
    public void setAgencias(List<Agencia> agencias) { this.agencias = agencias; }
}
