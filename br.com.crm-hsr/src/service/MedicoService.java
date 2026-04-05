package service;

import dao.MedicoDao;
import dao.MedicoDaoImpl;
import dao.PacienteDao;
import dao.PacienteDaoImpl;
import exceptions.EntityException;
import model.DisponibilidadeMedico;
import model.Medico;
import model.Paciente;

import java.util.HashMap;
import java.util.Map;

public class MedicoService {
    private final MedicoDao medicoDao;
    private final PacienteDao pacienteDao;
    private final Map<Long, Medico> atendimentosAtivos = new HashMap<>();

    public MedicoService() {
        this.medicoDao = new MedicoDaoImpl();
        this.pacienteDao = new PacienteDaoImpl();
    }

    public Medico iniciarAtendimento(Long medicoId, Long pacienteId) {
        if (medicoId == null) {
            throw new EntityException("Id do medico e obrigatorio.");
        }

        if (pacienteId == null) {
            throw new EntityException("Id do paciente e obrigatorio.");
        }

        Medico medico = medicoDao.buscarPorId(medicoId);
        if (medico == null) {
            throw new EntityException("Medico nao encontrado.");
        }

        Paciente paciente = pacienteDao.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new EntityException("Paciente nao encontrado.");
        }

        if (medico.getDisponibilidadeMedico() != DisponibilidadeMedico.DISPONIVEL) {
            throw new EntityException("Medico nao esta disponivel para iniciar atendimento.");
        }

        medico.setDisponibilidadeMedico(DisponibilidadeMedico.OCUPADO);
        medico.setPacienteEmAtendimento(paciente);
        medicoDao.atualizar(medico);
        atendimentosAtivos.put(medico.getId(), medico);

        System.out.println("Atendimento iniciado. Medico '" + medico.getName() + "' agora atende o paciente '" + paciente.getName() + "'.");
        return medico;
    }

    public Paciente informacoesDoPacienteDaConsulta(Long medicoId) {
        if (medicoId == null) {
            throw new EntityException("Id do medico e obrigatorio.");
        }

        Medico medico = atendimentosAtivos.get(medicoId);
        if (medico == null) {
            throw new EntityException("Nao existe atendimento ativo para o medico informado.");
        }

        Paciente paciente = medico.getPacienteEmAtendimento();
        if (paciente == null) {
            throw new EntityException("O medico nao possui paciente em atendimento.");
        }

        System.out.println("Paciente em atendimento:");
        System.out.println("Nome: " + paciente.getName());
        System.out.println("Email: " + paciente.getEmail());
        System.out.println("Telefone: " + paciente.getNumber());
        System.out.println("Data de nascimento: " + paciente.getDateBirthday());
        System.out.println("Sintomas: " + paciente.getSintomas());
        System.out.println("Prioridade da triagem: " + paciente.getPrioridadeTriagem());

        return paciente;
    }

    public Medico finalizarAtendimento(Long medicoId) {
        if (medicoId == null) {
            throw new EntityException("Id do medico e obrigatorio.");
        }

        Medico medico = atendimentosAtivos.get(medicoId);
        if (medico == null) {
            throw new EntityException("Nao existe atendimento ativo para o medico informado.");
        }

        if (medico.getPacienteEmAtendimento() == null) {
            throw new EntityException("O medico nao possui paciente em atendimento.");
        }

        String nomePaciente = medico.getPacienteEmAtendimento().getName();

        medico.setPacienteEmAtendimento(null);
        medico.setDisponibilidadeMedico(DisponibilidadeMedico.DISPONIVEL);
        medicoDao.atualizar(medico);
        atendimentosAtivos.remove(medicoId);

        System.out.println("Atendimento finalizado. Medico '" + medico.getName() + "' liberado e paciente '" + nomePaciente + "' removido do atendimento.");
        return medico;
    }
}
