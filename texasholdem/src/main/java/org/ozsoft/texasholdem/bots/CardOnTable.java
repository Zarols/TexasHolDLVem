package org.ozsoft.texasholdem.bots;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("cardontable")
public class CardOnTable {
	@Param(0)
	private int rank;
	@Param(1)
	private int suit;
	public CardOnTable() {
		
	}
	public CardOnTable(int rank, int suit) {
		this.rank = rank;
		this.suit = suit;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getSuit() {
		return suit;
	}
	public void setSuit(int suit) {
		this.suit = suit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rank;
		result = prime * result + suit;
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
		CardOnTable other = (CardOnTable) obj;
		if (rank != other.rank)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CardOnTable [rank=" + rank + ", suit=" + suit + "]";
	}
	
	
}
