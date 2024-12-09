package org.example;

import org.example.agencia.Agencia;
import org.example.banco.Banco;
import org.example.cliente.Cliente;

import java.util.List;

public class AcoesGerente {

    public static void listaClienteComSaldoNegativo(List<Banco> bancos) {

        /* Obtem todas as contas de todas as agencias de todos os bancos e filtra as
        contas com saldo negativo, usa flatMap para obter uma stream dos clientes
        dessas contas e imprime os nomes e cpfs dos clientes, evitando repeticoes com distint,
        ja um cliente pode ter mais de uma conta negativa */

        bancos.stream().flatMap(a -> a.getAgencias().stream())
                .flatMap(a -> a.getContas().stream())
                .filter(a -> a.getSaldo() < 0)
                .flatMap(a -> a.getClientes().stream())
                .distinct()
                .forEach(a -> System.out.println(a.getNome() + "          " + a.getCpf()));
    }

    public static void listaEmailDosClientes(List<Cliente> clientes) {

        clientes.stream().map(Cliente::getEmail).forEach(System.out::println);
    }

    public static void listaEnderecosDasAgencias(List<Banco> bancos) {

        bancos.stream().flatMap(a -> a.getAgencias().stream())
                .map(Agencia::getEndereco).forEach(System.out::println);
    }

    public static void listaClientesComMultiplasContas(List<Cliente> clientes) {

        clientes.stream().filter(a -> a.getContas().size() > 1).forEach(a -> System.out.println(a.getNome() + "          " + a.getCpf()));
    }

    public static void listaNomesBancos(List<Banco> bancos) {

        bancos.stream().map(Banco::getNome).forEach(System.out::println);

    }

}
