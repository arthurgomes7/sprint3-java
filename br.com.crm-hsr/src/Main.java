import dao.MedicoDao;
import dao.MedicoDaoImpl;
import dao.PacienteDao;
import dao.PacienteDaoImpl;
import exceptions.EntityException;
import model.Consulta;
import model.DisponibilidadeMedico;
import model.Medico;
import model.Paciente;
import service.MedicoService;
import service.PacienteService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PacienteDao pacienteDao = new PacienteDaoImpl();
        MedicoDao medicoDao = new MedicoDaoImpl();
        PacienteService pacienteService = new PacienteService();
        MedicoService medicoService = new MedicoService();

        String emailPacienteTeste = "paciente.teste@hsr.com";
        String emailMedicoTeste = "medico.teste@hsr.com";

        limparPacienteTeste(pacienteDao, emailPacienteTeste);
        limparMedicoTeste(medicoDao, emailMedicoTeste);

        Paciente paciente = new Paciente();
        paciente.setName("Paciente Teste");
        paciente.setEmail(emailPacienteTeste);
        paciente.setNumber("11999999999");
        paciente.setDateBirthday("2000-01-01");
        pacienteDao.criar(paciente);

        Paciente paciente1 = new Paciente();
        paciente1.setName("Arthur");
        paciente1.setEmail("arthur@hospital.com");
        paciente1.setNumber("11988599493");
        paciente1.setDateBirthday("2005-03-23");
        pacienteDao.criar(paciente1);

        Medico medico = new Medico();
        medico.setName("Medico Teste");
        medico.setEmail(emailMedicoTeste);
        medico.setNumber("11888888888");
        medico.setDateBirthday("1985-06-10");
        medico.setCrm("CRM-TESTE-001");
        medico.setEspecialidade("Clinico Geral");
        medico.setDisponibilidadeMedico(DisponibilidadeMedico.DISPONIVEL);
        medicoDao.criar(medico);

        Paciente pacienteSalvo = pacienteDao.buscarPorEmail(emailPacienteTeste);
        Medico medicoSalvo = medicoDao.buscarPorEmail(emailMedicoTeste);

        try {
            System.out.println("==== TESTE 1: TRIAGEM ====");
            Paciente pacienteTriado = pacienteService.realizarTriagem(
                    pacienteSalvo.getId(),
                    "dor no peito e falta de ar",
                    125,
                    39.1
            );
            System.out.println("Sintomas registrados: " + pacienteTriado.getSintomas());
            System.out.println("Prioridade calculada: " + pacienteTriado.getPrioridadeTriagem());
            System.out.println("Triagem realizada: " + pacienteTriado.isTriagemRealizada());
            System.out.println("Tempo maximo de espera: " + pacienteTriado.getTempoMaximoEsperaMinutos() + " minutos");
            if (!"ALTA".equals(pacienteTriado.getPrioridadeTriagem())
                    || !pacienteTriado.isTriagemRealizada()
                    || pacienteTriado.getTempoMaximoEsperaMinutos() == null
                    || pacienteTriado.getTempoMaximoEsperaMinutos() != 10) {
                throw new RuntimeException("Teste da triagem falhou.");
            }
            System.out.println("Teste da triagem finalizado com sucesso.");

            System.out.println("==== TESTE 2: AGENDAMENTO ====");
            pacienteService.agendarConsulta(pacienteTriado.getId(), medicoSalvo.getId());
            List<Consulta> consultas = pacienteService.listarConsultasDoPaciente(pacienteTriado.getId());
            System.out.println("Consultas encontradas para o paciente: " + consultas.size());
            if (consultas.isEmpty()) {
                throw new RuntimeException("Teste do agendamento falhou.");
            }
            for (Consulta consulta : consultas) {
                System.out.println(consulta);
            }
            System.out.println("Teste do agendamento finalizado com sucesso.");

            System.out.println("==== TESTE 3: INICIAR ATENDIMENTO ====");
            Medico medicoEmAtendimento = medicoService.iniciarAtendimento(medicoSalvo.getId(), pacienteTriado.getId());
            System.out.println("Disponibilidade do medico: " + medicoEmAtendimento.getDisponibilidadeMedico());
            System.out.println("Paciente em atendimento: " + medicoEmAtendimento.getPacienteEmAtendimento().getName());
            if (medicoEmAtendimento.getDisponibilidadeMedico() != DisponibilidadeMedico.OCUPADO
                    || medicoEmAtendimento.getPacienteEmAtendimento() == null) {
                throw new RuntimeException("Teste de iniciar atendimento falhou.");
            }
            System.out.println("Teste de iniciar atendimento finalizado com sucesso.");

            System.out.println("==== TESTE 4: INFORMACOES DO PACIENTE EM ATENDIMENTO ====");
            Paciente pacienteEmAtendimento = medicoService.informacoesDoPacienteDaConsulta(medicoSalvo.getId());
            if (pacienteTriado == null
                    || !pacienteTriado.getId().equals(pacienteTriado.getId())) {
                throw new RuntimeException("Teste da consulta de informacoes do paciente falhou.");
            }
            System.out.println("Teste da consulta de informacoes finalizado com sucesso.");

            System.out.println("==== TESTE 5: FINALIZAR ATENDIMENTO ====");
            Medico medicoFinalizado = medicoService.finalizarAtendimento(medicoSalvo.getId());
            System.out.println("Disponibilidade do medico apos finalizar: " + medicoFinalizado.getDisponibilidadeMedico());
            System.out.println("Paciente associado apos finalizar: " + medicoFinalizado.getPacienteEmAtendimento());
            if (medicoFinalizado.getDisponibilidadeMedico() != DisponibilidadeMedico.DISPONIVEL
                    || medicoFinalizado.getPacienteEmAtendimento() != null) {
                throw new RuntimeException("Teste de finalizar atendimento falhou.");
            }
            System.out.println("Teste de finalizar atendimento finalizado com sucesso.");
        } catch (EntityException e) {
            System.out.println("Erro de validacao: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    private static void limparPacienteTeste(PacienteDao pacienteDao, String email) {
        Paciente pacienteExistente = pacienteDao.buscarPorEmail(email);
        if (pacienteExistente != null) {
            pacienteDao.deletar(pacienteExistente.getId());
        }
    }

    private static void limparMedicoTeste(MedicoDao medicoDao, String email) {
        Medico medicoExistente = medicoDao.buscarPorEmail(email);
        if (medicoExistente != null) {
            medicoDao.deletar(medicoExistente.getId());
        }
    }
}
