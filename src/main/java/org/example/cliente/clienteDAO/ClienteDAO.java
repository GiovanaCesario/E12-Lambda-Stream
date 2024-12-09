package org.example.cliente.clienteDAO;

import org.example.cliente.Cliente;
import org.example.conexao.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    //Constantes
    private static final int CPF_POSICAO_TABELA = 1;
    private static final int NOME_POSICAO_TABELA = 2;
    private static final int EMAIL_POSICAO_TABELA = 3;

    //Códigos de erro
    static final int DUPLICATE_KEY_ERROR_CODE = 1062;

    //Cria a tabela de clientes caso ela não exista.
    public static void criaTabelaCliente() throws FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            Statement stmt = conexao.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS clientes (cpf VARCHAR(255) PRIMARY KEY," +
                    "nome VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL)");
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Obtem um cliente a partir do seu cpf.
    public static Cliente obtemClientePorCpf(String cpf) throws ClienteNaoEncontradoException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM clientes WHERE cpf = ?");
            stmt.setString(1, cpf);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                // Obtenho os dados
                return new Cliente(resultado.getString(CPF_POSICAO_TABELA),
                        resultado.getString(NOME_POSICAO_TABELA),
                        resultado.getString(EMAIL_POSICAO_TABELA)
                );
            }

            // Se chegou aqui é porque não tem cliente com esse cpf
            throw new ClienteNaoEncontradoException(cpf);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Insere um novo cliente no banco de dados.
    public static void insere(Cliente cliente) throws ClienteExistenteException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO clientes VALUES(?,?,?);");
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getEmail());
            stmt.execute();

        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) throw new ClienteExistenteException(cliente.getCpf());
            throw new Error(e.getMessage());
        }
    }

    //Atualiza os dados de um cliente no banco de dados.
    public static void atualiza(Cliente cliente) throws ClienteNaoEncontradoException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("UPDATE clientes SET nome = ?, email = ? WHERE cpf = ?;");
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCpf());

            // Verifico a quantidade de registros alterados
            int nLinhasAlteradas = stmt.executeUpdate();

            // Se não alterou nenhuma linha é porque não tem cliente com esse cpf
            if (nLinhasAlteradas == 0) throw new ClienteNaoEncontradoException(cliente.getCpf());

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Remove om cliente do banco de dados.
    public static void remove(Cliente cliente) throws ClienteNaoEncontradoException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM clientes WHERE cpf = ?;");
            stmt.setString(1, cliente.getCpf());

            // Verifico a quantidade de registros alterados
            int nLinhasAlteradas = stmt.executeUpdate();

            // Se não alterou nenhuma linha é porque não tem cliente com esse cpf
            if (nLinhasAlteradas == 0) throw new ClienteNaoEncontradoException(cliente.getCpf());

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Obtem a lista de todos os Clientes cpfdos.
    public static List<Cliente> obtemListaClientes() throws FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery("SELECT * from clientes ORDER BY nome;");

            // Crio a lista de clientes.
            List<Cliente> listaClientes = new ArrayList<>();

            while(resultado.next()) {
                // Obtenho os dados
                Cliente ClienteTmp = new Cliente(resultado.getString(CPF_POSICAO_TABELA),
                        resultado.getString(NOME_POSICAO_TABELA),
                        resultado.getString(EMAIL_POSICAO_TABELA));
                // Adiciono à lista de clientes
                listaClientes.add(ClienteTmp);
            }

            // Retorna a lista de clientes preenchida
            return listaClientes;

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }
}
