package org.ozsoft.texasholdem.bots;

import java.util.Random;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("bluff")
public class Bluff {
	@Param(0)
	private String bluff;

	public Bluff() {
		Random random=new Random();
		int x=random.nextInt(100+1);
		if(x>=85) {
			bluff="yes";
		}
		else
			bluff="no";
	}
	public String getBluff() {
		return bluff;
	}

	public void setBluff(String bluff) {
		this.bluff = bluff;
	}
}
