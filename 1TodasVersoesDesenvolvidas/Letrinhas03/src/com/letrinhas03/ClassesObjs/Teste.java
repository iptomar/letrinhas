package com.letrinhas03.ClassesObjs;

/**
 * Created by Alex on 29/04/2014.
 */
public class Teste {

    protected int idTeste;
    protected int areaId;
    protected int professorId;
    protected String titulo;
    protected String Texto;
    protected long dataInsercaoTeste;
    protected int grauEscolar;
    protected int tipo;

    public Teste() {

    }

    public Teste(int idTeste,int areaId,int professorId ,String titulo ,String texto ,long dataInsercaoTeste, int grauEscolar, int tipo) {
        this.idTeste = idTeste;
        this.setAreaId(areaId);
        this.setProfessorId(professorId);
        this.titulo = titulo;
        this.dataInsercaoTeste = dataInsercaoTeste;
        this.Texto = texto;
        this.grauEscolar = grauEscolar;
        this.tipo = tipo;
    }

    public int getIdTeste() {
        return idTeste;
    }

    public void setIdTeste(int idTeste) {
        this.idTeste = idTeste;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return Texto;
    }

    public void setTexto(String texto) {
        Texto = texto;
    }

    public long getDataInsercaoTeste() {
        return dataInsercaoTeste;
    }

    public void setDataInsercaoTeste(long dataInsercaoTeste) {
        this.dataInsercaoTeste = dataInsercaoTeste;
    }

    public int getGrauEscolar() {
        return grauEscolar;
    }

    public void setGrauEscolar(int grauEscolar) {
        this.grauEscolar = grauEscolar;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipos(int tipo) {
        this.tipo = tipo;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }
}



