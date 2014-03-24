package de.genialitaet.spidersolitaire;

import org.eclipse.swt.graphics.Color;

/* Die Reihenfolge l‰ﬂt sich erraten, wenn man nur 1 oder 2 Farben nimmt */
enum Suit {  
	// @see{http://de.wikipedia.org/wiki/Vierfarbige_Spielkarten}
	clubs   (0,128,0,"\u2663"), 
	diamonds(0,0,255,"\u2666"),  
	hearts  (255,0,0,"\u2665"), 
	spades  (0,0,0  ,"\u2660"),;
	public final Color color;
	public final String unicode;
	Suit(int r, int g, int b,String unicode) {
		this.color = new Color(null,r,g,b);
		this.unicode = unicode;
	}
	
	public Suit convert(int suitCount) {
		if(suitCount == 1) {
			return spades; // anything is spades.
		} else if(suitCount == 2) {
			if(equals(clubs))
				return spades; // black to spades
			if(equals(diamonds))
				return hearts; // red to hearts
			return this;       // stays the same.
		} else {
			assert(suitCount==4);
			return this;
		}
	}
} 


/**
 * An <em>immutable</em> card.
 * @author Franky
 *
 */
public final class Card {
	final private int rank;
	final  Suit suit;
	
	public Card(int rank, Suit suit) {
		if(rank<0 || rank>12)
			throw new IllegalArgumentException("Rank not in range 0..12 ("+rank+")");
		this.rank = rank;
		this.suit = suit;
	}
	
	public boolean equalsSuit(Card that) {
		return this.suit == that.suit;
		//return suit.equals(that.suit);
	}
	
	public boolean isAce() {
		return rank == 0;
	}
	
	public boolean isKing() {
		return rank==12;
	}

	private Card(byte ordinal) {
		this(ordinal >> 2, Suit.values()[ordinal & 3]);
		assert(ordinal == ordinal());
	}
	
	private static Card[] cardSingletons;
	static {
		cardSingletons = new Card[52];
		for(byte i=0; i<52;i++) {
			cardSingletons[i] = new Card(i);
		}
	}
	
	public static Card get(byte ordinal) {
		return  cardSingletons[ordinal];
		//return new Card(ordinal);
	}
	
	/**
	 * A unique number for each card, i.e. an ace of spades will always have the same ordinal.
	 * @see #get(byte)
	 * @return A unique number for each card, i.e. an ace of spades will always have the same ordinal.
	 */
	public byte ordinal() {
		int res = rank;
		return (byte)(4 * res + suit.ordinal()); 
	}
	
	public boolean fitsOn(Card that) {
		return this.rank+1  == that.rank; // no need for suit the be equal
	}
	
	public boolean fitsOn(Column col) {
		if(col.isEmpty())
			return true;
		return fitsOn(col.topCard());
	}
	
	public boolean isMoveableOn(Card that) {
		return fitsOn(that) && equalsSuit(that);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null) return false;
		if(o instanceof Card) {
			Card that = (Card)o;
			if(this==that)
				return true; // shortcut
			return this.rank == that.rank && suit.equals(that.suit);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return suit.ordinal() * 13 + rank;
	}
	
	public static String stringFromRank(int rank) {
		switch(rank) {
		case 0: return "ace";
		case 1: return "two";
		case 2: return "three";
		case 3: return "four";
		case 4: return "five";
		case 5: return "six";
		case 6: return "seven";
		case 7: return "eight";
		case 8: return "nine";
		case 9: return "ten";
		case 10: return "jack";
		case 11: return "queen";
		case 12: return "king";
		default: throw new Error("rank somehow slipped out of range 0..12 ("+rank+")");
		}
	}
	
	private static String abbreviation = "A23456789!JQK"; 
	public String getChar() {
		if(rank==9)
			return "10";
		return abbreviation.substring(rank, rank+1);
	}
	
	@Override
	public String toString() {
		return stringFromRank(rank)+" of "+suit;
	}
}
