package model;

public class Medico extends Pessoa{
    private String crm;
    private String especialidade;
    private DisponibilidadeMedico disponibilidadeMedico;

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public DisponibilidadeMedico getDisponibilidadeMedico() {
        return disponibilidadeMedico;
    }

    public void setDisponibilidadeMedico(DisponibilidadeMedico disponibilidadeMedico) {
        this.disponibilidadeMedico = disponibilidadeMedico;
    }
}
