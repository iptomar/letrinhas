/*
 * Esta class serve para calcular todos os possiveis parâmetros de avaliação dos testes realizados
 * @author Dário
 */

package com.letrinhas03.util;

public class Avaliacao {

	/**
	 * palavras lidas por minuto
	 * @author Dário
	 */
	public float PLM(int minutos, int segundos,int totalDePalavrasLidas){
		minutos = minutos + (segundos/60);
		return  (totalDePalavrasLidas / minutos)*60;
	}
	
	/**
	 * precisão na leitura
	 * @author Dário
	 */
	public double PL(int totalDePalavras, int palavrasCertas){
		return (palavrasCertas/totalDePalavras)*100;
	}
	
	/**
	 * velocidade de leitura
	 * @author Dário
	 */
	public double VL(int minutos, int segundos,int totalDePalavras, int palavrasErradas){
		minutos = minutos + (segundos/60);
		int palavrasCertas = totalDePalavras - palavrasErradas;
		return  (palavrasCertas / minutos)*60;
	}
		
	/**
	 * calculo do nº todal de sinais de pontuação menos os sinais de pontuação desrespeitados
	 * @author Dário
	 */
	public int Expressividade(int TotalDeSinaisPontuacao, int SinaisDesrespeitados){
		return TotalDeSinaisPontuacao - SinaisDesrespeitados;
	}
	
	/**
	 * calculo do nº total de palavras menos o total de falhas(repetições, vacilações, silibações e fragmentações)
	 * @author Dário
	 */
	public int Ritmo(int totalDePalavras, int fragmentacoes, int vacilacoes, int silibacoes, int repeticoes){
		return totalDePalavras - (fragmentacoes + vacilacoes + silibacoes + repeticoes);
	}
	
}
