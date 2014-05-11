package com.letrinhas04.ClassesObjs;

public class Estudante {

    // private variables
    private int idEstudante;
    private int idTurma;
    private String nome;
    private byte[] foto;
    private int estado;

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


}
