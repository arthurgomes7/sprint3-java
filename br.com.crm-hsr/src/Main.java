import dao.MedicoDao;
import dao.MedicoDaoImpl;
import dao.PacienteDao;
import dao.PacienteDaoImpl;
import exceptions.EntityException;
import model.Consulta;
import model.DisponibilidadeMedico;
import model.Medico;
import model.Paciente;
import service.PacienteService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PacienteDao pacienteDao = new PacienteDaoImpl();
        MedicoDao medicoDao = new MedicoDaoImpl();
        PacienteService pacienteService = new PacienteService();

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
            pacienteService.agendarConsulta(pacienteSalvo.getId(), medicoSalvo.getId());
            System.out.println("Consulta agendada com sucesso.");

            List<Consulta> consultas = pacienteService.listarConsultasDoPaciente(pacienteSalvo.getId());
            System.out.println("Consultas encontradas para o paciente: " + consultas.size());

            for (Consulta consulta : consultas) {
                System.out.println(consulta);
            }
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
