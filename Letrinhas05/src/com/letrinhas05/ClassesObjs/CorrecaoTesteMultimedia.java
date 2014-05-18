package com.letrinhas05.ClassesObjs;

/**
 * Created by Alex on 18/05/2014.
 */
public class CorrecaoTesteMultimedia extends CorrecaoTeste {

    protected int opcaoEscolhida;
    protected int certa;

    public CorrecaoTesteMultimedia(int idCorrrecao, int testId, int idEstudante, String dataExecucao, int estado, int opcaoEscolhida, int certa) {
        super(idCorrrecao, testId, idEstudante, dataExecucao, estado);
        this.opcaoEscolhida = opcaoEscolhida;
        this.certa = certa;
    }

    public int getOpcaoEscolhida() {
        return opcaoEscolhida;
    }

    public void setOpcaoEscolhida(int opcaoEscolhida) {
        this.opcaoEscolhida = opcaoEscolhida;
    }

    public int getCerta() {
        return certa;
    }

    public void setCerta(int certa) {
        this.certa = certa;
    }
}
