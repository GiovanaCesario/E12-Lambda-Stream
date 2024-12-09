package org.example.agencia.agenciaDAO;

import org.example.agencia.Agencia;
import org.example.banco.bancoDAO.BancoDAO;
import org.example.banco.bancoDAO.BancoNaoEncontradoException;
import org.example.conexao.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgenciaDAO {

    //Constantes
    private static final int NUMERO_POSICAO_TABELA = 1;
    private static final int ENDERECO_POSICAO_TABELA = 2;
    private static final int EMAIL_POSICAO_TABELA = 3;
    private static final int CNPJBANCO_POSICAO_TABELA = 4;

    //Códigos de erro
    static final int DUPLICATE_KEY_ERROR_CODE = 1062;

    //Cria a tabela de agencias caso ela não exista.
    public static void criaTabelaAgencia() throws FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            Statement stmt = conexao.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS agencias (" +
                    "numero INT PRIMARY KEY, " +
                    "endereco VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) NOT NULL, " +
                    "cnpjBanco VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY (cnpjBanco) REFERENCES bancos(cnpj) ON DELETE CASCADE ON UPDATE CASCADE)");

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }


    //Obtem uma agencia a partir do seu número.
    public static Agencia obtemAgenciaPorNumero(int numero) throws AgenciaNaoEncontradaException, FalhaConexaoException, BancoNaoEncontradoException {

        ResultSet resultado = null;
        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM agencias WHERE numero = ?");
            stmt.setInt(1, numero);
            resultado = stmt.executeQuery();

            if (resultado.next()) {
                // Obtenho os dados
                return new Agencia(resultado.getInt(NUMERO_POSICAO_TABELA),
                        resultado.getString(ENDERECO_POSICAO_TABELA),
                        resultado.getString(EMAIL_POSICAO_TABELA),
                        BancoDAO.obtemBancoPorCnpj(resultado.getString(CNPJBANCO_POSICAO_TABELA))
                );
            }

            // Se chegou aqui é porque não tem agencia com esse numero
            throw new AgenciaNaoEncontradaException(numero);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }


    //Insere um novo agencia no banco de dados.
    public static void insere(Agencia agencia) throws AgenciaExistenteException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO agencias VALUES(?,?,?,?);");
            stmt.setInt(1, agencia.getNumero());
            stmt.setString(2, agencia.getEndereco());
            stmt.setString(3, agencia.getEmail());
            stmt.setString(4, agencia.getBanco().getCnpj());
            stmt.execute();

        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) throw new AgenciaExistenteException(agencia.getNumero());
            throw new Error(e.getMessage());
        }
    }


    //Atualiza os dados de um agencia no banco de dados.
    public static void atualiza(Agencia agencia) throws AgenciaNaoEncontradaException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("UPDATE agencias SET endereco = ?, email = ?, cnpjBanco = ? WHERE numero = ?;");
            stmt.setString(1, agencia.getEndereco());
            stmt.setString(2, agencia.getEmail());
            stmt.setString(3, agencia.getBanco().getCnpj());
            stmt.setInt(4, agencia.getNumero());

            // Verifico a quantidade de registros alterados
            int nLinhasAlteradas = stmt.executeUpdate();

            // Se não alterou nenhuma linha é porque não tem agencia com esse numero
            if (nLinhasAlteradas == 0) throw new AgenciaNaoEncontradaException(agencia.getNumero());

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }


    //Remove o agencia do banco de dados.
    public static void remove(Agencia agencia) throws AgenciaNaoEncontradaException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM agencias WHERE numero = ?;");
            stmt.setInt(1, agencia.getNumero());

            // Verifico a quantidade de registros alterados
            int nLinhasAlteradas = stmt.executeUpdate();

            // Se não alterou nenhuma linha é porque não tem agencia com esse numero
            if (nLinhasAlteradas == 0) throw new AgenciaNaoEncontradaException(agencia.getNumero());

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }


    //Obtem a lista de todos os agencias numerodos.
    public static List<Agencia> obtemListaAgencias() throws FalhaConexaoException, BancoNaoEncontradoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery("SELECT * from agencias ORDER BY numero;");

            // Crio a lista de agencias.
            List<Agencia> listaAgencias = new ArrayList<>();

            while(resultado.next()) {
                // Obtenho os dados
                Agencia agenciaTmp = new Agencia(resultado.getInt(NUMERO_POSICAO_TABELA),
                        resultado.getString(ENDERECO_POSICAO_TABELA),
                        resultado.getString(EMAIL_POSICAO_TABELA),
                        BancoDAO.obtemBancoPorCnpj(resultado.getString(CNPJBANCO_POSICAO_TABELA)));
                // Adiciono à lista de agencias
                listaAgencias.add(agenciaTmp);
            }

            // Retorna a lista de agencias preenchida
            return listaAgencias;

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }


    //Obtem a lista de todos os agencias de determinado banco.
    public static List<Agencia> obtemListaAgenciasDeUmBanco(String cnpj) throws FalhaConexaoException, BancoNaoEncontradoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM agencias WHERE CnpjBanco = ? ORDER BY numero");
            stmt.setString(1, cnpj);
            ResultSet resultado = stmt.executeQuery();

            // Crio a lista de agencias.
            List<Agencia> listaAgencias = new ArrayList<>();

            while(resultado.next()) {
                // Obtenho os dados
                Agencia agenciaTmp = new Agencia(resultado.getInt(NUMERO_POSICAO_TABELA),
                        resultado.getString(ENDERECO_POSICAO_TABELA),
                        resultado.getString(EMAIL_POSICAO_TABELA),
                        BancoDAO.obtemBancoPorCnpj(resultado.getString(CNPJBANCO_POSICAO_TABELA)));
                // Adiciono à lista de agencias
                listaAgencias.add(agenciaTmp);
            }

            // Retorna a lista de agencias preenchida
            return listaAgencias;

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }


}
