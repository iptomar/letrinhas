package com.letrinhas05.ClassesObjs;

/**
 * @author Alexandre
 */
public class TurmaProfessor {

    protected int idTurma;
    protected int idProfessor;

    public TurmaProfessor() {
    }

    public TurmaProfessor(int idTurma, int idProfessor) {
        this.idTurma = idTurma;
        this.idProfessor = idProfessor;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }
}
