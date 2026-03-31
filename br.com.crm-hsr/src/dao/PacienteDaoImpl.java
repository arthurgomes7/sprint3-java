package dao;

import exceptions.DatabaseException;
import model.Paciente;
import util.DbConection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PacienteDaoImpl implements PacienteDao{
    private final Connection connection;

    public PacienteDaoImpl() {
        this.connection = DbConection.getConnection();
        criarTabela();
    }

    public void criarTabela() {
        String sql = """
                BEGIN
                    EXECUTE IMMEDIATE '
                        CREATE TABLE PACIENTE (
                            ID NUMBER GENERATED ALWAYS AS IDENTITY,
                            NAME VARCHAR2(100) NOT NULL,
                            EMAIL VARCHAR2(100) NOT NULL UNIQUE,
                            PHONE_NUMBER VARCHAR2(20),
                            DATE_BIRTHDAY VARCHAR2(20),
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

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao criar tabela PACIENTE: " + e.getMessage());
        }
    }

    @Override
    public void criar(Paciente paciente) {
        String sql = "INSERT INTO PACIENTE (NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, paciente.getName());
            statement.setString(2, paciente.getEmail());
            statement.setString(3, paciente.getNumber());
            statement.setString(4, paciente.getDateBirthday());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao criar paciente: " + e.getMessage());
        }
    }

    @Override
    public Paciente buscarPorId(Long id) {
        String sql = "SELECT ID, NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY FROM PACIENTE WHERE ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapPaciente(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar paciente por id: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Paciente buscarPorEmail(String email) {
        String sql = "SELECT ID, NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY FROM PACIENTE WHERE EMAIL = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapPaciente(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar paciente por email: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Paciente> buscarPorNome(String nome) {
        String sql = "SELECT ID, NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY FROM PACIENTE WHERE UPPER(NAME) LIKE UPPER(?)";
        List<Paciente> pacientes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + nome + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pacientes.add(mapPaciente(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar paciente por nome: " + e.getMessage());
        }

        return pacientes;
    }

    @Override
    public List<Paciente> listarTodos() {
        String sql = "SELECT ID, NAME, EMAIL, PHONE_NUMBER, DATE_BIRTHDAY FROM PACIENTE ORDER BY NAME";
        List<Paciente> pacientes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                pacientes.add(mapPaciente(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar pacientes: " + e.getMessage());
        }

        return pacientes;
    }

    @Override
    public void atualizar(Paciente paciente) {
        String sql = "UPDATE PACIENTE SET NAME = ?, EMAIL = ?, PHONE_NUMBER = ?, DATE_BIRTHDAY = ? WHERE ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, paciente.getName());
            statement.setString(2, paciente.getEmail());
            statement.setString(3, paciente.getNumber());
            statement.setString(4, paciente.getDateBirthday());
            statement.setLong(5, paciente.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM PACIENTE WHERE ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar paciente: " + e.getMessage());
        }
    }

    private Paciente mapPaciente(ResultSet resultSet) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(resultSet.getLong("ID"));
        paciente.setName(resultSet.getString("NAME"));
        paciente.setEmail(resultSet.getString("EMAIL"));
        paciente.setNumber(resultSet.getString("PHONE_NUMBER"));
        paciente.setDateBirthday(resultSet.getString("DATE_BIRTHDAY"));
        return paciente;
    }
}
