package dao;

import exceptions.DatabaseException;
import model.DisponibilidadeMedico;
import model.Medico;
import util.DbConection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MedicoDaoImpl implements MedicoDao {
    Connection conexaoDB = DbConection.getConnection();

    public MedicoDaoImpl() {
        criarTabela();
    }

    public void criarTabela() {
        String sql = """
                BEGIN
                    EXECUTE IMMEDIATE '
                        CREATE TABLE MEDICO (
                            ID NUMBER GENERATED ALWAYS AS IDENTITY,
                            NAME VARCHAR2(100) NOT NULL,
                            EMAIL VARCHAR2(100) NOT NULL UNIQUE,
                            PHONE_NUMBER VARCHAR2(20),
                            DATE_BIRTHDAY VARCHAR2(20),
                            CRM VARCHAR2(20) NOT NULL UNIQUE,
                            ESPECIALIDADE VARCHAR2(100),
                            DISPONIBILIDADE_MEDICO VARCHAR2(20),
                            PRIMARY KEY (ID)
                        )
                    ';
                EXCEPTION
                    WHEN OTHERS THEN
                        IF SQLCODE != -955 THEN
                            RAISE;
                        END IF;
                END;
                """;
        try (Connection conn = DbConection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabela 'Medico' verificada/criada com sucesso!");

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao criar tabela (DAO): " + e.getMessage());
        }
    }

    @Override
    public void criar(Medico medico) {
        String sql = "INSERT INTO MEDICO (NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY, CRM, ESPECIALIDADE, DISPONIBILIDADE_MEDICO) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, medico.getName());
            statement.setString(2, medico.getEmail());
            statement.setString(3, medico.getNumber());
            statement.setString(4, medico.getDateBirthday());
            statement.setString(5, medico.getCrm());
            statement.setString(6, medico.getEspecialidade());
            statement.setString(7, medico.getDisponibilidadeMedico() != null ? medico.getDisponibilidadeMedico().name() : null);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao criar medico: " + e.getMessage());
        }
    }

    @Override
    public Medico buscarPorId(Long id) {
        String sql = "SELECT ID, NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY, CRM, ESPECIALIDADE, DISPONIBILIDADE_MEDICO FROM MEDICO WHERE ID = ?";

        try (Connection conn = DbConection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMedico(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar medico por id: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Medico buscarPorEmail(String email) {
        String sql = "SELECT ID, NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY, CRM, ESPECIALIDADE, DISPONIBILIDADE_MEDICO FROM MEDICO WHERE EMAIL = ?";

        try (Connection conn = DbConection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMedico(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar medico por email: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Medico> buscarPorNome(String nome) {
        String sql = "SELECT ID, NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY, CRM, ESPECIALIDADE, DISPONIBILIDADE_MEDICO FROM MEDICO WHERE UPPER(NAME) LIKE UPPER(?)";
        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = DbConection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, "%" + nome + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    medicos.add(mapMedico(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar medico por nome: " + e.getMessage());
        }

        return medicos;
    }

    @Override
    public List<Medico> listarTodos() {
        String sql = "SELECT ID, NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY, CRM, ESPECIALIDADE, DISPONIBILIDADE_MEDICO FROM MEDICO ORDER BY NAME";
        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = DbConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                medicos.add(mapMedico(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar medicos: " + e.getMessage());
        }

        return medicos;
    }

    @Override
    public void atualizar(Medico medico) {
        String sql = "UPDATE MEDICO SET NAME = ?, EMAIL = ?, PHONE_NUMBER = ?, DATE_BIRTHDAY = ?, CRM = ?, ESPECIALIDADE = ?, DISPONIBILIDADE_MEDICO = ? WHERE ID = ?";

        try (Connection conn = DbConection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, medico.getName());
            statement.setString(2, medico.getEmail());
            statement.setString(3, medico.getNumber());
            statement.setString(4, medico.getDateBirthday());
            statement.setString(5, medico.getCrm());
            statement.setString(6, medico.getEspecialidade());
            statement.setString(7, medico.getDisponibilidadeMedico() != null ? medico.getDisponibilidadeMedico().name() : null);
            statement.setLong(8, medico.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar medico: " + e.getMessage());
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM MEDICO WHERE ID = ?";

        try (Connection conn = DbConection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar medico: " + e.getMessage());
        }
    }

    private Medico mapMedico(ResultSet resultSet) throws SQLException {
        Medico medico = new Medico();
        medico.setId(resultSet.getLong("ID"));
        medico.setName(resultSet.getString("NAME"));
        medico.setEmail(resultSet.getString("EMAIL"));
        medico.setNumber(resultSet.getString("PHONE_NUMBER"));
        medico.setDateBirthday(resultSet.getString("DATE_BIRTHDAY"));
        medico.setCrm(resultSet.getString("CRM"));
        medico.setEspecialidade(resultSet.getString("ESPECIALIDADE"));

        String disponibilidade = resultSet.getString("DISPONIBILIDADE_MEDICO");
        if (disponibilidade != null) {
            medico.setDisponibilidadeMedico(DisponibilidadeMedico.valueOf(disponibilidade));
        }

        return medico;
    }
}
