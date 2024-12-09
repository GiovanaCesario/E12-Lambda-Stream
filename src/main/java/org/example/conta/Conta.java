package org.example.conta;

import org.example.agencia.Agencia;
import org.example.cliente.Cliente;

import java.util.ArrayList;
import java.util.List;

public class Conta {

    //Atributos
    final private int numero;
    private float saldo;

    private Agencia agencia;
    private List<Cliente> clientes;

    //Construtor
    public Conta(int numero, float saldo , Agencia agencia) {

        this.numero = numero;
        this.saldo = saldo;
        this.clientes = new ArrayList<>();
        this.agencia = agencia;
    }

    //Associacao
    public void associaCliente(Cliente cliente) {

        this.clientes.add(cliente);
        cliente.associaConta(this);
    }

    //Desasociacao
    public void desassociaCliente(Cliente cliente) {

        this.clientes.remove(cliente);
        cliente.desassociaConta(this);
    }

    @Override
    public String toString() {
        return "Conta{" +
                "numero=" + numero +
                ", saldo=" + saldo +
                ", agencia=" + agencia +
                ", clientes=" + clientes +
                '}';
    }

    //Getters e Setters
    public int getNumero() { return numero; }

    public float getSaldo() { return saldo; }
    public void setSaldo(float saldo) { this.saldo = saldo; }

    public Agencia getAgencia() { return agencia; }
    public void setAgencia(Agencia agencia) { this.agencia = agencia;}

    public List<Cliente> getClientes() { return clientes; }
    public void setClientes(List<Cliente> clientes) { this.clientes = clientes;}
}
