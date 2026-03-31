package dao;

import model.Paciente;

import java.util.List;

public interface PacienteDao {
    void criar(Paciente paciente);
    Paciente buscarPorId(Long id);
    Paciente buscarPorEmail(String email);
    List<Paciente> buscarPorNome(String nome);
    List<Paciente> listarTodos();
    void atualizar(Paciente paciente);
    void deletar(Long id);
}
