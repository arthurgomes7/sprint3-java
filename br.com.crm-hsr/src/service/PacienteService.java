package service;

import model.Consulta;
import model.DisponibilidadeMedico;
import model.Medico;
import model.Paciente;

import java.util.ArrayList;
import java.util.List;

public class PacienteService {
    private Medico medico;
    private Paciente paciente;
    private List<Consulta> consultas = new ArrayList<>();

    public void agendarConsulta(Paciente paciente, Medico medico){
        if (medico.getDisponibilidadeMedico() != DisponibilidadeMedico.DISPONIVEL){
            System.out.println("Médico não disponivel");
            return;
        }
        Consulta consulta = new Consulta(paciente, medico);
        consultas.add(consulta);
    }

    public void listarConsultas(){
        for (Consulta c : consultas){
            System.out.println(c);
        }
    }
}
