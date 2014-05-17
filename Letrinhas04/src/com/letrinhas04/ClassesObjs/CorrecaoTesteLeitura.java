package com.letrinhas04.ClassesObjs;


/**
 * Created by Alex on 23/04/2014.
 */
public class CorrecaoTesteLeitura extends CorrecaoTeste {
    protected String observacoes;
    protected float numPalavrasMin;
    protected int numPalavCorretas;
    protected int numPalavIncorretas;
    protected float precisao;
    protected float velocidade;
    protected float expressividade;
    protected float ritmo;
    protected byte[] audio;

    public CorrecaoTesteLeitura(int testId, int idEstudante, String dataExecucao, String observacoes, float numPalavrasMin, int numPalavCorretas, int numPalavIncorretas, float precisao, float velocidade, float expressividade, float ritmo, byte[] audio) {
        super(testId, idEstudante, dataExecucao);
        this.observacoes = observacoes;
        this.numPalavrasMin = numPalavrasMin;
        this.numPalavCorretas = numPalavCorretas;
        this.numPalavIncorretas = numPalavIncorretas;
        this.precisao = precisao;
        this.velocidade = velocidade;
        this.expressividade = expressividade;
        this.ritmo = ritmo;
        this.audio = audio;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public float getNumPalavrasMin() {
        return numPalavrasMin;
    }

    public void setNumPalavrasMin(float numPalavrasMin) {
        this.numPalavrasMin = numPalavrasMin;
    }

    public int getNumPalavCorretas() {
        return numPalavCorretas;
    }

    public void setNumPalavCorretas(int numPalavCorretas) {
        this.numPalavCorretas = numPalavCorretas;
    }

    public int getNumPalavIncorretas() {
        return numPalavIncorretas;
    }

    public void setNumPalavIncorretas(int numPalavIncorretas) {
        this.numPalavIncorretas = numPalavIncorretas;
    }

    public float getPrecisao() {
        return precisao;
    }

    public void setPrecisao(float precisao) {
        this.precisao = precisao;
    }

    public float getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(float velocidade) {
        this.velocidade = velocidade;
    }

    public float getExpressividade() {
        return expressividade;
    }

    public void setExpressividade(float expressividade) {
        this.expressividade = expressividade;
    }

    public float getRitmo() {
        return ritmo;
    }

    public void setRitmo(float ritmo) {
        this.ritmo = ritmo;
    }

    public byte[] getAudio() {
        return audio;
    }

    public void setAudioUrl( byte[] audio) {
        this.audio = audio;
    }
}
