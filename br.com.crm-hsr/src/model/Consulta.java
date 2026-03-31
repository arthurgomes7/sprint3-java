package model;

import java.time.LocalDateTime;

public class Consulta {
    private Long id;
    private LocalDateTime date;
    private Paciente paciente;
    private Medico medico;

    public Consulta() {
    }

    public Consulta(Paciente paciente, Medico medico) {
        this.date = LocalDateTime.now();
        this.paciente = paciente;
        this.medico = medico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @Override
    public String toString() {
        return "Consulta{" +
                "id=" + id +
                ", date=" + date +
                ", paciente=" + paciente +
                ", medico=" + medico +
                '}';
    }
}
