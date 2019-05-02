package org.ozsoft.texasholdem.bots;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((giocata == null) ? 0 : giocata.hashCode());
		result = prime * result + puntata;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comand other = (Comand) obj;
		if (giocata == null) {
			if (other.giocata != null)
				return false;
		} else if (!giocata.equals(other.giocata))
			return false;
		if (puntata != other.puntata)
			return false;
		return true;
	}
	
}
