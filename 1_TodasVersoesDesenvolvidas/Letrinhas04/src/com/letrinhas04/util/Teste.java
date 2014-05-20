package com.letrinhas04.util;

public class Teste {
	int ID;
	int tipo;
	String titulo;
	
	public Teste(int iD,int tipo, String titulo) {
		super();
		ID = iD;
		this.tipo = tipo;
		this.titulo = titulo;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	
}
