package org.ozsoft.texasholdem.bots;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;


@Id("allowedcomand")
public class AllowedComand {
	
	@Param(0)
	private String giocata;
	
	public AllowedComand() {
		//
	}
	
	public AllowedComand(String giocata) {
		this.giocata = giocata.toLowerCase();
	}
	
	@Override
	public String toString() {
		return "AllowedComand [giocata=" + giocata + "]";
	}

	public String getGiocata() {
		return giocata;
	}
	public void setGiocata(String giocata) {
		this.giocata = giocata;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((giocata == null) ? 0 : giocata.hashCode());
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
		AllowedComand other = (AllowedComand) obj;
		if (giocata == null) {
			if (other.giocata != null)
				return false;
		} else if (!giocata.equals(other.giocata))
			return false;
		return true;
	}


	
}

