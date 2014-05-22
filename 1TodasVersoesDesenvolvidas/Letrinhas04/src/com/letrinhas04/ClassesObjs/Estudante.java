package com.letrinhas04.ClassesObjs;

public class Estudante {

    // private variables
    protected int idEstudante;
    protected int idTurma;
    protected String nome;
    protected byte[] foto;
    protected String nomefoto;
    protected int estado;

    // Empty constructor
    public Estudante() {
    }

    // constructor 1
    public Estudante(int idEstudante, int idTurma, String nome, byte[] foto,
                     int estado) {

        this.idEstudante = idEstudante;
        this.idTurma = idTurma;
        this.nome = nome;
        this.foto = foto;
        this.estado = estado;
    }



    // constructor 2
    public Estudante(int idEstudante, int idTurma, String nome, String nomefoto,
                     int estado) {

        this.idEstudante = idEstudante;
        this.idTurma = idTurma;
        this.nome = nome;
        this.nomefoto = nomefoto;
        this.estado = estado;
    }


    public int getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(int idEstudante) {
        this.idEstudante = idEstudante;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }


    public String getNomefoto() {
        return nomefoto;
    }

    public void setNomefoto(String nomefoto) {
        this.nomefoto = nomefoto;
    }
}
