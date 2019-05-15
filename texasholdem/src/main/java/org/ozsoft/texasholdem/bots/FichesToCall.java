package org.ozsoft.texasholdem.bots;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("fichesToCall")
public class FichesToCall {
	@Param(0)
	private int amount;

	public FichesToCall() {
		
	}
	public FichesToCall(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
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
		FichesToCall other = (FichesToCall) obj;
		if (amount != other.amount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FichesToCall [amount=" + amount + "]";
	}
	
	
}
