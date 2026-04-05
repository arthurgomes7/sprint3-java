package model;

public class Paciente extends Pessoa {
    private String sintomas;
    private Integer frequenciaCardiaca;
    private Double temperatura;
    private String prioridadeTriagem;
    private boolean triagemRealizada;
    private Integer tempoMaximoEsperaMinutos;

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public Integer getFrequenciaCardiaca() {
        return frequenciaCardiaca;
    }

    public void setFrequenciaCardiaca(Integer frequenciaCardiaca) {
        this.frequenciaCardiaca = frequenciaCardiaca;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public String getPrioridadeTriagem() {
        return prioridadeTriagem;
    }

    public void setPrioridadeTriagem(String prioridadeTriagem) {
        this.prioridadeTriagem = prioridadeTriagem;
    }

    public boolean isTriagemRealizada() {
        return triagemRealizada;
    }

    public void setTriagemRealizada(boolean triagemRealizada) {
        this.triagemRealizada = triagemRealizada;
    }

    public Integer getTempoMaximoEsperaMinutos() {
        return tempoMaximoEsperaMinutos;
    }

    public void setTempoMaximoEsperaMinutos(Integer tempoMaximoEsperaMinutos) {
        this.tempoMaximoEsperaMinutos = tempoMaximoEsperaMinutos;
    }
}
