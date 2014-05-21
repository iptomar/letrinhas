package com.letrinhas04.ClassesObjs;

/**
 * Created by Alex on 23/04/2014.
 */
public class CorrecaoTeste {

    // private variables
    protected int testId;
    protected int idEstudante;
    protected String DataExecucao;


    public CorrecaoTeste(int testId, int idEstudante, String dataExecucao) {
        this.testId = testId;
        this.idEstudante = idEstudante;
        DataExecucao = dataExecucao;
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
}
