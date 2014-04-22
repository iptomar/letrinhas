package com.letrinhas03.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Classe para lançar as activity's de um modo sequêncial de acordo com a lista
 * recebida de testes a realizar.
 * vai verificar o tipo de teste a realizar e montar a activity de acordo com o seu contúdo
 * 
 * 
 * 
 * @author Thiago
 * 
 */
public class ExecutaTestes extends Thread {
	Activity act; 
	boolean modo; 
	Teste lista[];
	
	
	/**
	 * Construtor
	 * 
	 * @param act
	 *            Activity base para poder lançar as novas activity's, (Testes)
	 * @param modo
	 *            Boolean para identificar o modo de execução dos testes  Aluno/Professor
	 * @param lista
	 *            Array com a identificação de cada um dos testes seleccionado anteriormente           
	 *            
	 */
	public ExecutaTestes(Activity act, boolean modo, Teste lista[]) {
		this.act = act;
		this.modo = modo;
		this.lista = lista;
	}

	
	public void run() {
		
			
		
		
		for (int i = 0; i < lista.length; i++) {
			switch (lista[i].tipo){
			case 0 :
				Toast.makeText(act.getApplicationContext(),(i+1)+" - Texto",
						Toast.LENGTH_SHORT).show();
				//lançar a nova activity do tipo texto, e o seu conteúdo
				//
				//
				
				break;
			case 1 :
				Toast.makeText(act.getApplicationContext(),(i+1)+" - Palavras",
						Toast.LENGTH_SHORT).show();
				//lançar a nova activity do tipo Palavras, e o seu conteúdo
				//
				//Intent it = new Intent(act.getApplicationContext(),texto.class);
				//act.startActivity(it);
				
				//esperar que esta termine 
				//while (!act.isDestroyed());
				
				break;
			case 2 :
				Toast.makeText(act.getApplicationContext(),(i+1)+" - Poemas",
						Toast.LENGTH_SHORT).show();
				//lançar a nova activity do tipo Poema, e o seu conteúdo
				//
				//
				
				break;
			case 3 :
				Toast.makeText(act.getApplicationContext(),(i+1)+" - Imagens",
						Toast.LENGTH_SHORT).show();
				//lançar a nova activity do tipo imagem, e o seu conteúdo
				//
				//
				
				
				break;
			default:
				Toast.makeText(act.getApplicationContext(),(i+1)+" - Tipo não defenido",
						Toast.LENGTH_SHORT).show();
				//não lançar nada e continuar
				
				break;
			}
			
			
		}
		//Verifica o modo.
		

	}

}
