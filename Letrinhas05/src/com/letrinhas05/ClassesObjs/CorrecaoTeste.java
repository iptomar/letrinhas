package com.letrinhas05.ClassesObjs;

/**
 * Created by Alex on 23/04/2014.
 */
public class CorrecaoTeste {

    // private variables
    protected int idCorrrecao;
    protected int testId;
    protected int idEstudante;
    protected long DataExecucao;
    protected int tipo;
    protected int estado;

    public CorrecaoTeste() {
    }

    public CorrecaoTeste( int idCorrrecao, int testId, int idEstudante, long dataExecucao, int tipo,int estado) {
       this.setIdCorrrecao(idCorrrecao);
        this.testId = testId;
        this.idEstudante = idEstudante;
        this.DataExecucao = dataExecucao;
        this.tipo = tipo;
        this.estado = estado ;
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

    public long getDataExecucao() {
        return DataExecucao;
    }

    public void setDataExecucao(long dataExecucao) {
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
