package com.letrinhas05.ClassesObjs;


/**
 * @author Alexandre
 */
public class CorrecaoTesteLeitura extends CorrecaoTeste {
    protected String audiourl;
    protected String observacoes;
    protected float numPalavrasMin;
    protected int numPalavCorretas;
    protected int numPalavIncorretas;
    protected float precisao;
    protected float velocidade;
    protected int expressividade;
    protected int ritmo;
    protected String detalhes;
    
    public CorrecaoTesteLeitura(){
    	
    }

    public CorrecaoTesteLeitura(long idCorrrecao, int testId, int idEstudante, long dataExecucao, int tipo ,int estado,String audiourl, int numPalavCorretas, String observacoes, float numPalavrasMin, float precisao, float velocidade, int numPalavIncorretas, int expressividade, int ritmo, String detalhes) {
        super(idCorrrecao, testId, idEstudante, dataExecucao, tipo, estado);
        this.setAudiourl(audiourl);
        this.setNumPalavCorretas(numPalavCorretas);
        this.setObservacoes(observacoes);
        this.setNumPalavrasMin(numPalavrasMin);
        this.setPrecisao(precisao);
        this.setVelocidade(velocidade);
        this.setNumPalavIncorretas(numPalavIncorretas);
        this.setExpressividade(expressividade);
        this.setRitmo(ritmo);
        this.setDetalhes(detalhes);
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
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

    public void setExpressividade(int expressividade) {
        this.expressividade = expressividade;
    }

    public float getRitmo() {
        return ritmo;
    }

    public void setRitmo(int ritmo) {
        this.ritmo = ritmo;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }
}