package com.letrinhas05.ClassesObjs;

/**
 * Created by Alex on 11/05/2014.
 */
public class Turma {

    protected int id;
    protected int idEscola;
    protected int anoEscolar;
    protected String nome;
    protected String anoLetivo;

    public Turma(int id, int idEscola, int anoEscolar, String nome,  String anoLetivo) {
        this.id = id;
        this.idEscola = idEscola;
        this.nome = nome;
        this.anoEscolar = anoEscolar;
        this.anoLetivo = anoLetivo;
    }

    public Turma() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEscola() {
        return idEscola;
    }

    public void setIdEscola(int idEscola) {
        this.idEscola = idEscola;
    }

    public int getAnoEscolar() {
        return anoEscolar;
    }

    public void setAnoEscolar(int anoEscolar) {
        this.anoEscolar = anoEscolar;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(String anoLetivo) {
        this.anoLetivo = anoLetivo;
    }
}
