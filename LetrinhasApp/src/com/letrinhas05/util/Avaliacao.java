package com.letrinhas05.util;

/**
 * Esta classe serve para calcular todos os possiveis parametros de avaliação
 *  dos testes realizados
 * 
 * @author Dàrio & Thiago
 */
public class Avaliacao {
	private int totalDePalavras, TotalDeSinaisPontuacao;
	private int plvErradas, pontua, vacil, fragment, silabs, repeti;
	public String obs, detalhes;

	//construtores
	public Avaliacao(int totalDePalavras, int TotalDeSinaisPontuacao) {
		this.totalDePalavras = totalDePalavras;
		this.TotalDeSinaisPontuacao = TotalDeSinaisPontuacao;
	}

	public Avaliacao(int totalDePalavras, int TotalDeSinaisPontuacao,
			int plvErradas) {
		this.totalDePalavras = totalDePalavras;
		this.TotalDeSinaisPontuacao = TotalDeSinaisPontuacao;
		this.plvErradas = plvErradas;
	}

	/**
	 * decrementa o nº de palavras erradas até 0
	 */
	public void decPalErrada() {
		if (plvErradas != 0) {
			plvErradas--;
		}
	}

	/**
	 * incrementa o nº de palavras erradas até ao nº total de palavras
	 */
	public void incPalErrada() {
		if (plvErradas < totalDePalavras) {
			plvErradas++;
		}
	}

	/**
	 * devolve o nº de palavras erradas
	 * @return numero inteiro
	 */
	public int getPlvErradas() {
		return plvErradas;
	}

	/**
	 * decrementa o nº de violações de pontuação até 0
	 */
	public void decPontua() {
		if (pontua != 0) {
			pontua--;
		}
	}

	/**
	 * incrementa o nº de violações de pontuação até ao nº total de pontuação no texto
	 */
	public void incPontua() {
		if (pontua < TotalDeSinaisPontuacao)
			pontua++;
	}

	/** devolve o nº de violações de pontuação
	 * 
	 * @return numero inteiro
	 */
	public int getPontua() {
		return pontua;
	}

	/** 
	 * decrementa o nº de vacilações até 0
	 */
	public void decVacil() {
		if (vacil != 0)
			vacil--;
	}

	/** 
	 * incrementa o nº de vacilações
	 */
	public void incVacil() {
		vacil++;
	}

	/** 
	 * devolve o nº de vacilações
	 * @return	numero inteiro
	 */
	public int getVacil() {
		return vacil;
	}

	/** 
	 * decrementa o nº de fragmentações até 0
	 */
	public void decFragment() {
		if (fragment != 0)
			fragment--;
	}

	/** 
	 * incrementa o nº de fragmentações
	 */
	public void incFragment() {
		fragment++;
	}

	/** 
	 * devolve o nº de fragmentações
	 * @return	numero inteiro
	 */
	public int getFragment() {
		return fragment;
	}

	/** 
	 * decrementa o nº de silabações até 0
	 */
	public void decSilabs() {
		if (silabs != 0)
			silabs--;
	}

	/** 
	 * incrementa o nº de silabações
	 */
	public void incSilabs() {
		silabs++;
	}

	/** 
	 * devolve o nº de silabações
	 * @return	numero inteiro
	 */
	public int getSilabs() {
		return silabs;
	}

	/** 
	 * decrementa o nº de repetições até 0
	 */
	public void decRepeti() {
		if (repeti != 0)
			repeti--;
	}

	/** 
	 * incrementa o nº de repetições
	 */
	public void incRepeti() {
		repeti++;
	}

	/** 
	 * devolve o nº de repetições
	 * @return	numero inteiro
	 */
	public int getRepeti() {
		return repeti;
	}

	/**
	 * Calcula todos os parametros necessários para a avaliação 
	 * @param minutos
	 * @param segundos
	 * @return String com o relatório
	 * @author Thiago
	 */
	public String calcula(int minutos, int segundos) {
		obs = "Tempo de Leitura (em segundos): " + (minutos * 60 + segundos);
		String resultado = "==========Avaliação============\n"
				+ "Tempo de Leitura (em segundos): "
				+ (minutos * 60 + segundos) + "\n";
		resultado += "Palavras lidas por minuto (plm): "
				+ PLM(minutos, segundos) + "\n";
		resultado += "Palavras corretamente lidas (pcl): " + palavrasCertas()
				+ "\n";
		resultado += "Precisão de Leitura (PL): " + PL() + "\n";
		resultado += "Velocidade de leitura (VL): " + VL(minutos, segundos)
				+ "\n";
		resultado += "Expressividade: " + Expressividade() + "\n";
		resultado += "Ritmo: " + Ritmo() + "\n\n";
		detalhes = "Tempo: " + minutos + ":" + segundos + "\n"
				+ "Total de Palavras no texto: " + totalDePalavras + "\n"
				+ "Total de sinais de pontuação no texto: "
				+ TotalDeSinaisPontuacao + "\n"
				+ "Palavras lidas incorretamente: " + plvErradas + "\n"
				+ "Sinais de pontuação desrespeitados: " + pontua + "\n"
				+ "Vacilações: " + vacil + "\n" + "Fragmentações: " + fragment
				+ "\n" + "Silabações: " + silabs + "\n" + "Repetições: "
				+ repeti + "\n";

		resultado += "===============================\n" + "Detalhes\n"
				+ "===============================\n" + detalhes;
		return resultado;
	}

	/**
	 * palavras lidas por minuto
	 * 
	 * @return numero real(float)
	 * @author Dário
	 */
	public float PLM(int minuts, int segundos) {
		float minutos = minuts;
		float sess = 60;
		//
		minutos += (segundos / sess);
		//
		float plm = (totalDePalavras / minutos);
		return plm;
	}

	public int palavrasCertas() {
		return totalDePalavras - plvErradas;
	}

	/**
	 * precisão na leitura
	 * 
	 * @return numero real(float)
	 * @author Dario
	 */
	public float PL() {
		float d = palavrasCertas() * 100;
		float t = totalDePalavras;
		d = (d / t);
		return d;
	}

	/**
	 * velocidade de leitura
	 * 
	 * @return numero real(float)
	 * @author Dario
	 */
	public float VL(int minuts, int segundos) {
		float minutos = minuts;
		float sess = 60;
		//
		minutos += (segundos / sess);
		//
		float vl = (palavrasCertas() / minutos);
		return vl;
	}

	/**
	 * calculo do nº total de sinais de pontuação menos os sinais de pontuação
	 * desrespeitados
	 * 
	 * @author Dário
	 */
	public int Expressividade() {
		return TotalDeSinaisPontuacao - pontua;
	}

	/**
	 * calculo do nº total de palavras menos o total de falhas(repetições,
	 * vacilações, silibações e fragmentações)
	 * 
	 * @author Dário
	 */
	public int Ritmo() {
		return totalDePalavras - (fragment + vacil + silabs + repeti);
	}

}