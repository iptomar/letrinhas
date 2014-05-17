package com.letrinhas04.ClassesObjs;

/**
 * Created by Alex on 23/04/2014.
 */
public class Sistema {

    // private variables
    protected int id;
    protected String nome;
    protected String valor;

    public Sistema() {

    }

    public Sistema(int id , String nome, String valor) {
        this.nome = nome;
        this.id = id;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
