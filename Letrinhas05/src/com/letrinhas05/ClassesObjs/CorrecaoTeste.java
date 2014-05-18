package com.letrinhas05.ClassesObjs;

/**
 * Created by Alex on 23/04/2014.
 */
public class CorrecaoTeste {

    // private variables
    protected int idCorrrecao;
    protected int testId;
    protected int idEstudante;
    protected String DataExecucao;
    protected int estado;



    public CorrecaoTeste( int idCorrrecao, int testId, int idEstudante, String dataExecucao, int estado) {
       this.setIdCorrrecao(idCorrrecao);
        this.testId = testId;
        this.idEstudante = idEstudante;
        this.DataExecucao = dataExecucao;
        this.setEstado(estado);
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(int idEstudante) {
        this.idEstudante = idEstudante;
    }

    public String getDataExecucao() {
        return DataExecucao;
    }

    public void setDataExecucao(String dataExecucao) {
        this.DataExecucao = dataExecucao;
    }

    public int getIdCorrrecao() {
        return idCorrrecao;
    }

    public void setIdCorrrecao(int idCorrrecao) {
        this.idCorrrecao = idCorrrecao;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
