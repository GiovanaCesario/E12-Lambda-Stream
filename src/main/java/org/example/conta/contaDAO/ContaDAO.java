package org.example.conta.contaDAO;

import org.example.agencia.agenciaDAO.AgenciaDAO;
import org.example.agencia.agenciaDAO.AgenciaNaoEncontradaException;
import org.example.banco.bancoDAO.BancoNaoEncontradoException;
import org.example.conexao.*;
import org.example.conta.Conta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {

    //Constantes
    private static final int NUMERO_POSICAO_TABELA = 1;
    private static final int SALDO_POSICAO_TABELA = 2;
    private static final int NUMEROAGENCIA_POSICAO_TABELA = 3;

    //Códigos de erro
    static final int DUPLICATE_KEY_ERROR_CODE = 1062;

    //Cria a tabela de Contas caso ela não exista.
    public static void criaTabelaConta() throws FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            Statement stmt = conexao.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS contas (numero INT PRIMARY KEY," +
                    "saldo DECIMAL NOT NULL, " +
                    "numeroAgencia INT NOT NULL, " +
                    "FOREIGN KEY (numeroAgencia) REFERENCES agencias(numero) ON DELETE CASCADE ON UPDATE CASCADE)");

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    // Obtem uma conta a partir do seu número.
    public static Conta obtemContaPorNumero(int numero) throws ContaNaoEncontradaException, FalhaConexaoException,
            AgenciaNaoEncontradaException, BancoNaoEncontradoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM contas WHERE numero = ?");
            stmt.setInt(1, numero);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                // Obtenho os dados
                return new Conta(resultado.getInt(NUMERO_POSICAO_TABELA),
                        resultado.getFloat(SALDO_POSICAO_TABELA),
                        AgenciaDAO.obtemAgenciaPorNumero(resultado.getInt(NUMEROAGENCIA_POSICAO_TABELA))
                );
            }

            // Se chegou aqui é porque não tem conta com esse nnumero
            throw new ContaNaoEncontradaException(numero);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Insere uma nova conta no banco de dados.
    public static void insere(Conta conta) throws ContaExistenteException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO contas VALUES(?,?,?);");
            stmt.setInt(1, conta.getNumero());
            stmt.setFloat(2, conta.getSaldo());
            stmt.setInt(3, conta.getAgencia().getNumero());
            stmt.execute();

        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) throw new ContaExistenteException(conta.getNumero());
            throw new Error(e.getMessage());
        }
    }

    //Atualiza os dados de uma conta no banco de dados.
    public static void atualiza(Conta conta) throws ContaNaoEncontradaException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("UPDATE contas SET saldo = ?, numeroAgencia = ? WHERE numero = ?;");
            stmt.setFloat(1, conta.getSaldo());
            stmt.setInt(2, conta.getAgencia().getNumero());
            stmt.setInt(3, conta.getNumero());

            // Verifico a quantidade de registros alterados
            int nLinhasAlteradas = stmt.executeUpdate();

            // Se não alterou nenhuma linha é porque não tem conta com esse numero
            if (nLinhasAlteradas == 0) throw new ContaNaoEncontradaException(conta.getNumero());

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Remove um conta do banco de dados.
    public static void remove(Conta conta) throws ContaNaoEncontradaException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM contas WHERE numero = ?;");
            stmt.setInt(1, conta.getNumero());

            // Verifico a quantidade de registros alterados
            int nLinhasAlteradas = stmt.executeUpdate();

            // Se não alterou nenhuma linha é porque não tem conta com esse numero
            if (nLinhasAlteradas == 0) throw new ContaNaoEncontradaException(conta.getNumero());

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Obtem a lista de todos os Contas Numerodos.
    public static List<Conta> obtemListaContas() throws FalhaConexaoException, AgenciaNaoEncontradaException, BancoNaoEncontradoException{

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery("SELECT * from contas ORDER BY numero;");

            // Crio a lista de contas.
            List<Conta> listaContas = new ArrayList<>();

            while(resultado.next()) {
                // Obtenho os dados
                Conta ContaTmp = new Conta(resultado.getInt(NUMERO_POSICAO_TABELA),
                        resultado.getFloat(SALDO_POSICAO_TABELA),
                        AgenciaDAO.obtemAgenciaPorNumero(resultado.getInt(NUMEROAGENCIA_POSICAO_TABELA)));

                // Adiciono à lista de contas
                listaContas.add(ContaTmp);
            }

            // Retorna a lista de contas preenchida
            return listaContas;

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Obtem a lista de todos as contas de determinada agencia.
    public static List<Conta> obtemListaContasDeUmaAgencia(int numero) throws FalhaConexaoException, AgenciaNaoEncontradaException, BancoNaoEncontradoException{

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM contas WHERE numeroAgencia = ? ORDER BY numero");
            stmt.setInt(1, numero);
            ResultSet resultado = stmt.executeQuery();

            // Crio a lista de contas.
            List<Conta> listaContas = new ArrayList<>();

            while(resultado.next()) {
                // Obtenho os dados
                Conta ContaTmp = new Conta(resultado.getInt(NUMERO_POSICAO_TABELA),
                        resultado.getFloat(SALDO_POSICAO_TABELA),
                        AgenciaDAO.obtemAgenciaPorNumero(resultado.getInt(NUMEROAGENCIA_POSICAO_TABELA)));

                // Adiciono à lista de contas
                listaContas.add(ContaTmp);
            }

            // Retorna a lista de contas preenchida
            return listaContas;

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }
}