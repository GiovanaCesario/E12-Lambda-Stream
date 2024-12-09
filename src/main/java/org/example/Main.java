package org.example;

import org.example.agencia.Agencia;
import org.example.agencia.agenciaDAO.*;
import org.example.banco.Banco;
import org.example.banco.bancoDAO.*;
import org.example.cliente.Cliente;
import org.example.cliente.clienteDAO.*;
import org.example.conexao.FalhaConexaoException;
import org.example.conta.Conta;
import org.example.conta.contaDAO.*;
import org.example.contaCliente.ContaClienteDAO;
import org.example.contaCliente.ContaClienteExistenteException;
import org.example.contaCliente.ContaClienteNaoEncontradoException;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        //OBS: O BD contem os dados da atividade anterior

        List<Banco> bancos = null;
        List<Cliente> clientes = null;

        //Descarrega dados
        try {
            bancos = BancoDAO.obtemListaBancos();

            for (Banco banco: bancos) {
                banco.setAgencias(AgenciaDAO.obtemListaAgenciasDeUmBanco(banco.getCnpj()));

                for(Agencia agencia : banco.getAgencias()) {
                    agencia.setContas(ContaDAO.obtemListaContasDeUmaAgencia(agencia.getNumero()));

                    for(Conta conta : agencia.getContas()) {
                        conta.setClientes(ContaClienteDAO.obtemClientesDeConta(conta.getNumero()));
                    }
                }
            }

            clientes = ClienteDAO.obtemListaClientes();
            for (Cliente cliente : clientes) {
                cliente.setContas(ContaClienteDAO.obtemContasDeCliente(cliente.getCpf()));
            }

        } catch (FalhaConexaoException | BancoNaoEncontradoException | AgenciaNaoEncontradaException |
                 ClienteNaoEncontradoException | ContaNaoEncontradaException e) {

        }

        //Executa as acoes do gerente
        System.out.println("=== Clientes com saldo negativo ===");
        AcoesGerente.listaClienteComSaldoNegativo(bancos);
        System.out.println("===================================");

        System.out.println("=== Emails dos clientes ===");
        AcoesGerente.listaEmailDosClientes(clientes);
        System.out.println("==========================");

        System.out.println("=== Enderecos das agencias ===");
        AcoesGerente.listaEnderecosDasAgencias(bancos);
        System.out.println("==============================");

        System.out.println("=== Clientes com multiplas contas ===");
        AcoesGerente.listaClientesComMultiplasContas(clientes);
        System.out.println("=====================================");

        System.out.println("=== Nomes de todos os bancos do sistema ===");
        AcoesGerente.listaNomesBancos(bancos);
        System.out.println("===========================================");
    }
}

