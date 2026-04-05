package service;

import dao.MedicoDao;
import dao.MedicoDaoImpl;
import dao.PacienteDao;
import dao.PacienteDaoImpl;
import exceptions.EntityException;
import model.Consulta;
import model.DisponibilidadeMedico;
import model.Medico;
import model.Paciente;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PacienteService {
    private final PacienteDao pacienteDao;
    private final MedicoDao medicoDao;
    private final List<Consulta> consultas = new ArrayList<>();

    public PacienteService() {
        this.pacienteDao = new PacienteDaoImpl();
        this.medicoDao = new MedicoDaoImpl();
    }

    public void agendarConsulta(Long pacienteId, Long medicoId) {
        if (pacienteId == null) {
            throw new EntityException("Id do paciente e obrigatorio.");
        }

        if (medicoId == null) {
            throw new EntityException("Id do medico e obrigatorio.");
        }

        Paciente paciente = pacienteDao.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new EntityException("Paciente nao encontrado.");
        }

        Medico medico = medicoDao.buscarPorId(medicoId);
        if (medico == null) {
            throw new EntityException("Medico nao encontrado.");
        }

        if (medico.getDisponibilidadeMedico() != DisponibilidadeMedico.DISPONIVEL) {
            throw new EntityException("Medico nao disponivel para consulta.");
        }

        Consulta consulta = new Consulta(paciente, medico);
        consultas.add(consulta);
    }

    public List<Consulta> listarConsultasDoPaciente(Long pacienteId) {
        if (pacienteId == null) {
            throw new EntityException("Id do paciente e obrigatorio.");
        }

        Paciente paciente = pacienteDao.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new EntityException("Paciente nao encontrado.");
        }

        return consultas.stream()
                .filter(consulta -> consulta.getPaciente() != null)
                .filter(consulta -> pacienteId.equals(consulta.getPaciente().getId()))
                .collect(Collectors.toList());
    }

    public Paciente realizarTriagem(Long pacienteId, String sintomas, Integer frequenciaCardiaca, Double temperatura) {
        if (pacienteId == null) {
            throw new EntityException("Id do paciente e obrigatorio.");
        }

        Paciente paciente = pacienteDao.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new EntityException("Paciente nao encontrado.");
        }

        paciente.setSintomas(sintomas);
        paciente.setFrequenciaCardiaca(frequenciaCardiaca);
        paciente.setTemperatura(temperatura);

        String sintomasNormalizados = sintomas == null ? "" : sintomas.toLowerCase(Locale.ROOT);

        if (sintomasNormalizados.contains("dor no peito")
                || sintomasNormalizados.contains("falta de ar")
                || (frequenciaCardiaca != null && frequenciaCardiaca >= 120)
                || (temperatura != null && temperatura >= 39.0)) {
            paciente.setPrioridadeTriagem("ALTA");
            paciente.setTempoMaximoEsperaMinutos(10);
        } else if ((temperatura != null && temperatura >= 37.5)
                || sintomasNormalizados.contains("febre")) {
            paciente.setPrioridadeTriagem("MEDIA");
            paciente.setTempoMaximoEsperaMinutos(30);
        } else {
            paciente.setPrioridadeTriagem("BAIXA");
            paciente.setTempoMaximoEsperaMinutos(120);
        }

        paciente.setTriagemRealizada(true);
        System.out.println("Triagem concluida para o paciente " + paciente.getName() + " com prioridade " + paciente.getPrioridadeTriagem() + ".");
        return paciente;
    }
}
