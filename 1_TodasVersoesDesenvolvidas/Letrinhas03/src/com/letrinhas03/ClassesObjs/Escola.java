package com.letrinhas03.ClassesObjs;

public class Escola {
    // private variables
    protected int idEscola;
    protected String nome;
    protected byte[] logotipo;
    protected String logotipoNome;
    protected String morada;

    // Empty constructor
    public Escola() {

    }

    // constructor 1
    public Escola(int idEscola, String nome, byte[] logotipo,
                  String morada) {

        this.idEscola = idEscola;
        this.nome = nome;
        this.logotipo = logotipo;
        this.morada = morada;
    }



    // constructor 2 -usado para os get da informacao da bd
    public Escola(int idEscola, String nome, String logotipoNome,
                  String morada) {

        this.idEscola = idEscola;
        this.nome = nome;
        this.setLogotipoNome(logotipoNome);
        this.morada = morada;
    }

    public int getIdEscola() {
        return idEscola;
    }

    public void setIdEscola(int idEscola) {
        this.idEscola = idEscola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getLogotipo() {
        return logotipo;
    }

    public void setLogotipo(byte[] logotipo) {
        this.logotipo = logotipo;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getLogotipoNome() {
        return logotipoNome;
    }

    public void setLogotipoNome(String logotipoNome) {
        this.logotipoNome = logotipoNome;
    }
}
