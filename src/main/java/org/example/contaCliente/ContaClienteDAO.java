package org.example.contaCliente;

import org.example.agencia.agenciaDAO.AgenciaNaoEncontradaException;
import org.example.banco.bancoDAO.BancoNaoEncontradoException;
import org.example.cliente.Cliente;
import org.example.cliente.clienteDAO.ClienteDAO;
import org.example.cliente.clienteDAO.ClienteNaoEncontradoException;
import org.example.conexao.*;
import org.example.conta.Conta;
import org.example.conta.contaDAO.ContaDAO;
import org.example.conta.contaDAO.ContaNaoEncontradaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaClienteDAO {

    //Constantes
    private static final int NUMEROCONTA_POSICAO_TABELA = 1;
    private static final int CPFCLIENTE_POSICAO_TABELA = 2;

    //Códigos de erro
    static final int DUPLICATE_KEY_ERROR_CODE = 1062;

    // Cria a tabela intermediária ContaCliente caso ela não exista.
    public static void criaTabelaContaCliente() throws FalhaConexaoException {
        try {
            Connection conexao = Conexao.obtemConexao();

            Statement stmt = conexao.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS ContaCliente (" +
                    "numeroConta INT NOT NULL, " +
                    "cpfCliente VARCHAR(255) NOT NULL, " +
                    "PRIMARY KEY (numeroConta, cpfCliente), " +
                    "FOREIGN KEY (numeroConta) REFERENCES contas(numero) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY (cpfCliente) REFERENCES clientes(cpf) ON DELETE CASCADE ON UPDATE CASCADE)");

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }


    // Adiciona um relacionamento entre conta e cliente 
    public static void insereContaCliente(int numeroConta, String cpfCliente) throws FalhaConexaoException,
            ContaClienteExistenteException {

        try {
            Connection conexao = Conexao.obtemConexao();

            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO ContaCliente (numeroConta, cpfCliente) VALUES (?, ?)");
            stmt.setInt(1, numeroConta);
            stmt.setString(2, cpfCliente);
            stmt.executeUpdate();

        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) throw new ContaClienteExistenteException(numeroConta, cpfCliente);
            throw new Error(e.getMessage());
        }
    }


    // Remove um relacionamento entre conta e cliente.
    public static void removeContaCliente(int numeroConta, String cpfCliente) throws FalhaConexaoException,
            ContaClienteNaoEncontradoException {

        try {
            Connection conexao = Conexao.obtemConexao();

            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM ContaCliente WHERE numeroConta = ? AND cpfCliente = ?");
            stmt.setInt(1, numeroConta);
            stmt.setString(2, cpfCliente);

            int nLinhasAlteradas = stmt.executeUpdate();

            if (nLinhasAlteradas == 0) throw new ContaClienteNaoEncontradoException(numeroConta, cpfCliente);

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    // Obtem todos os clientes associados a uma conta.
    public static List<Cliente> obtemClientesDeConta(int numeroConta) throws FalhaConexaoException,
            ClienteNaoEncontradoException {

        try {
            Connection conexao = Conexao.obtemConexao();

            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM ContaCliente WHERE numeroConta = ?");
            stmt.setInt(1, numeroConta);
            ResultSet resultado = stmt.executeQuery();

            List<Cliente> clientes = new ArrayList<>();

            while (resultado.next()) {
                Cliente cliente = ClienteDAO.obtemClientePorCpf(resultado.getString(CPFCLIENTE_POSICAO_TABELA));
                clientes.add(cliente);
            }

            return clientes;

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    // Obtém todas as contas associadas a um cliente.
    public static List<Conta> obtemContasDeCliente(String cpfCliente) throws FalhaConexaoException,
            AgenciaNaoEncontradaException, BancoNaoEncontradoException, ContaNaoEncontradaException {

        try {
            Connection conexao = Conexao.obtemConexao();

            PreparedStatement stmt = conexao.prepareStatement( "SELECT numeroConta FROM ContaCliente WHERE cpfCliente = ?");
            stmt.setString(1, cpfCliente);
            ResultSet resultado = stmt.executeQuery();

            List<Conta> contas = new ArrayList<>();

            while (resultado.next()) {
                Conta conta = ContaDAO.obtemContaPorNumero(resultado.getInt(NUMEROCONTA_POSICAO_TABELA));
                contas.add(conta);
            }

            return contas;

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }
}
