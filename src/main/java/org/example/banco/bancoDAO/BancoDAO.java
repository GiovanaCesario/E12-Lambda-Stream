package org.example.banco.bancoDAO;

import org.example.banco.Banco;
import org.example.conexao.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BancoDAO {

    //Constantes
    private static final int CNPJ_POSICAO_TABELA = 1;
    private static final int NOME_POSICAO_TABELA = 2;

    //Códigos de erro
    static final int DUPLICATE_KEY_ERROR_CODE = 1062;

    //Cria a tabela de bancos caso ela não exista.
    public static void criaTabelaBanco() throws FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            Statement stmt = conexao.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS bancos (cnpj VARCHAR(255) PRIMARY KEY," +
                    "nome VARCHAR(255) NOT NULL)");
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Obtem um banco a partir do seu cnpj.
    public static Banco obtemBancoPorCnpj(String cnpj) throws BancoNaoEncontradoException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM bancos WHERE cnpj = ?");
            stmt.setString(1, cnpj);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                // Obtenho os dados
                return new Banco(resultado.getString(CNPJ_POSICAO_TABELA),
                        resultado.getString(NOME_POSICAO_TABELA)
                );
            }

            // Se chegou aqui é porque não tem banco com esse cnpj
            throw new BancoNaoEncontradoException(cnpj);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    //Insere um novo banco no banco de dados.
    public static void insere(Banco banco) throws BancoExistenteException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO bancos VALUES(?,?);");
            stmt.setString(1, banco.getCnpj());
            stmt.setString(2, banco.getNome());
            stmt.execute();

        } catch (SQLException e) {
            if (e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) throw new BancoExistenteException(banco.getCnpj());
            throw new Error(e.getMessage());
        }
    }

    //Atualiza os dados de um banco no banco de dados.
    public static void atualiza(Banco banco) throws BancoExistenteException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("UPDATE bancos SET nome = ? WHERE cnpj = ?;");
            stmt.setString(1, banco.getNome());
            stmt.setString(2, banco.getCnpj());

            // Verifico a quantidade de registros alterados
            int nLinhasAlteradas = stmt.executeUpdate();

            // Se não alterou nenhuma linha é porque não tem banco com esse cnpj
            if (nLinhasAlteradas == 0) throw new BancoExistenteException(banco.getCnpj());

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    // Remove ou banco do banco de dados.
    public static void remove(Banco banco) throws BancoNaoEncontradoException, FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM bancos WHERE cnpj = ?;");
            stmt.setString(1, banco.getCnpj());

            // Verifico a quantidade de registros alterados
            int nLinhasAlteradas = stmt.executeUpdate();

            // Se não alterou nenhuma linha é porque não tem banco com esse cnpj
            if (nLinhasAlteradas == 0) throw new BancoNaoEncontradoException(banco.getCnpj());

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    // Obtem a lista de todos os bancos.
    public static List<Banco> obtemListaBancos() throws FalhaConexaoException {

        try {
            // Estabelecer conexao
            Connection conexao = Conexao.obtemConexao();

            // Faço a consulta
            Statement stmt = conexao.createStatement();
            ResultSet resultado = stmt.executeQuery("SELECT * from bancos ORDER BY nome;");

            // Crio a lista de bancos.
            List<Banco> listaBancos = new ArrayList<>();

            while(resultado.next()) {
                // Obtenho os dados
                Banco bancoTmp = new Banco(resultado.getString(CNPJ_POSICAO_TABELA),
                        resultado.getString(NOME_POSICAO_TABELA));
                // Adiciono à lista de bancos
                listaBancos.add(bancoTmp);
            }

            // Retorna a lista de bancos preenchida
            return listaBancos;

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }
}