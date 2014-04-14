package com.letrinhas02.util;

/**
 * Objecto que representa um teste, suporte para possibilitar a construção das Activity's 
 * dos testes.
 * 
 * @author Thiago
 */
public class Teste {
	int tipo;
	String titulo;
	String endereco;
	
	/**
	 * De momento este objeto tem um tipo, um título e o endereço/Query do seu conteúdo.
	 * 
	 * @param tipo int que apresenta de que forma se irá construir a Activity (Texto/Palavras/Imagens/etc..)
	 * @param titulo String para identificar o teste.
	 * @param endereco String com o endereço / Query / comando para retirar o conteúdo deste.
	 */
	public Teste(int tipo, String titulo, String endereco) {
		super();
		this.tipo = tipo;
		this.titulo = titulo;
		this.endereco = endereco;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
		
}
