package com.letrinhas05.ClassesObjs;

/**
 * @author Alexandre
 */
public class CorrecaoTeste {

    // private variables
    protected long idCorrrecao;
    protected int testId;
    protected int idEstudante;
    protected long DataExecucao;
    protected int tipo;
    protected int estado;

    public CorrecaoTeste() {
    }

    public CorrecaoTeste( long idCorrrecao, int testId, int idEstudante, long dataExecucao, int tipo,int estado) {
       this.idCorrrecao = idCorrrecao ;
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

    public long getIdCorrrecao() {
        return idCorrrecao;
    }

    public void setIdCorrrecao(long idCorrrecao) {
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
