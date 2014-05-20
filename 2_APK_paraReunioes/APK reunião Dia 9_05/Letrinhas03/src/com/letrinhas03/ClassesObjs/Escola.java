package com.letrinhas03.ClassesObjs;

public class Escola {
    // private variables
    private int idEscola;
    private String nome;
    private byte[] logotipo;
    private String morada;

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

}
