package org.ozsoft.texasholdem.actions;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("comand")
public class Comand {
	
	@Param(0)
	private String giocata;
	@Param(1)
	private int puntata;
	
	public Comand() {
		//
	}
	
	public Comand(String giocata, int puntata) {
		this.giocata = giocata;
		this.puntata = puntata;
	}
	
	@Override
	public String toString() {
		return "Comand [giocata=" + giocata + ", puntata=" + puntata + "]";
	}

	public String getGiocata() {
		return giocata;
	}
	public void setGiocata(String giocata) {
		this.giocata = giocata;
	}
	public int getPuntata() {
		return puntata;
	}
	public void setPuntata(int puntata) {
		this.puntata = puntata;
	}
	
}
