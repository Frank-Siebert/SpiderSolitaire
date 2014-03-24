package de.genialitaet.spidersolitaire;

//import java.util.ArrayList;
//import java.util.List;

import de.genialitaet.visualc.Random;

public class Deck {
	final ArrayList<Card> cards;
	final int suitCount;
	
	public Deck(int suitCount, int randseed) {
		this.suitCount = suitCount;
		cards = new ArrayList<Card>(104);
		for(int i=0; i<104; i++) {
			cards.add(null);
		}
		shuffle(randseed);
	}
	
	private void shuffle(int randseed) {
		boolean[] carddealt = new boolean[104];
		final int deckCount = 2;
		de.genialitaet.visualc.Random prng = new Random(randseed);
		
		for(int i=0;i<carddealt.length; i++) {
			carddealt[i] = false;
		}
		for(int deckCounter = 0; deckCounter < deckCount; deckCounter++) {
			for(Suit suit : Suit.values()) {
				for(int rank=0; rank<=12; rank++) {
					int slot;
					do {
						slot = prng.rand() % 104;
					} while(carddealt[slot]);
					carddealt[slot] = true;
					cards.set(slot, new Card(rank,suit.convert(suitCount)));
				}
			}
		}
	}
	
	public void printOpenCards() {
		for(int i=44; i<54; i++) {
			System.out.print(cards.get(i)+", ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		Deck d = new Deck(4,0x4603970b);
		d.printOpenCards();
	}

}
