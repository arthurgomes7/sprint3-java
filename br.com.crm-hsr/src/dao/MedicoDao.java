package dao;

import model.Medico;

import java.util.List;

public interface MedicoDao {
    void criar(Medico medico);
    Medico buscarPorId(Long id);
    Medico buscarPorEmail(String email);
    List<Medico> buscarPorNome(String nome);
    List<Medico> listarTodos();
    void atualizar(Medico medico);
    void deletar(Long id);
}
