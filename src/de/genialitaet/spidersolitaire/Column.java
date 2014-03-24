/**
 * 
 */
package de.genialitaet.spidersolitaire;

//import java.util.ArrayList;
//import java.util.List;

/**
 * in einer Spalte stehen die Karten
 * @author Franky
 *
 */
public class Column implements Comparable<Column> {
	/* package-friendly intensional. */
	int hiddenCount = 0;
	
	/**
	 * The cards in this Column
	 */
	ArrayList<Card> cards;
	/**
	 * fortlaufende, zusammengehörige Reihe.
	 */
	private int run = 0;
	
	byte number;
	
	public Column() {
		this(15);
	}
	
	/**
	 * creates a new Column and reserves cardCount places in the underlying arrayList
	 * @param cardCount
	 */
	public Column(int cardCount) {
		cards = new ArrayList<Card>(cardCount);
	}
	
	public void clear() {
		cards.clear();
		hiddenCount = 0;
		run = 0;
	}

	public void addCard(Card card, boolean open) {
		if(!open) {
			if(hiddenCount!=cards.size())
				throw new Error("Added hidden card after an open Card");
			hiddenCount++;
		} else {
			if(hiddenCount==cards.size()) {
				run = 1;
			} else {
				if(card.isMoveableOn(topCard()))
					run++;
				else
					run = 1;
			}
		}
		cards.add(card);
		//checkAndRemoveCompleteRun(); //TODO: wieder einkommentieren,
		// eigentlich sollte es nur fü den Spezialfall "Deal komplettiert Reihe" nötig sein...
		// ups, auch der sollte woanders abgefangen werden (in Deal)
	}
	
	public int getChaos(int emptyColumns) {
		if(isEmpty())
			return 0;
		int res = 10; 
		int run = 0;
		Card lower = cards.get(hiddenCount); // erste offene Karte
		for(int i=hiddenCount+1;i<cards.size();i++) {
			Card upper = cards.get(i);
			if(upper.fitsOn(lower)) {
				if(!upper.isMoveableOn(lower)) {
					res += 4; // TODO: 4 erlaubt das schwierige zu lösen, 24 ist bei anderen schneller
					if(emptyColumns>2) // fang mal an, aufzuteilen, wenn viel frei ist!
						res += 10;
					res -=run;
					run = 0;
				} else {
					//run++; // sowie das auskommentiert war, fand er eine Lösung!
					res -= run;
				}
			} else {
				res+= 23;
				if(emptyColumns<2) { // Die Bruchstellen werden nur dann weniger teuer,
					if(lower.isAce()) // wenn nicht zu viele freie Spalten da sind.
						res-= 13;
					if(upper.isKing())
						res-= 5;
				}
				res-=run;
				run = 0;
			}
			lower = upper;
		}
		res -=run;
		if(cards.get(0).isKing() && hiddenCount==0 && emptyColumns==0) // TODO: jüngst geändert ec==0 oder ec>0?
			res+= 1;
		if(topCard().isAce())
			res+= 3;
		return res+hiddenCount*50;
		//return res+hiddenCount*(50-hiddenCount); // Diese Variante verlangsamt das Programm erheblich!!!
		/* Mit dieser Variante werden erheblich mehr Stati gefunden, der Durchsatz fällt erheblich (6000->4500)
		 * Noch schlimmer: mit dieser Variante hat auch die Lösung mehr Züge.
		 */
	}
	
	boolean checkAndRemoveCompleteRun() {
		//if(run<13) // wenn entkommentiert, versagt der Entfernen in SpecificStackMove.
		//	return false; // liegt woanders dran
		if(cards.size()<13)
			return false;
		for(int i=cards.size()-1;i>cards.size()-13;i--)
			if(!cards.get(i).isMoveableOn(cards.get(i-1))) {
				return false;
			}
		for(int i=0;i<13;i++) {
			cards.remove(cards.size()-1);
		}
		if(cards.size()==hiddenCount && hiddenCount>0)
			hiddenCount--;
		recalcRun();
		return true;
	}

	/**
	 * Returns the top card (which is lowest)
	 * @return the top card or null 
	 */ 
	public Card topCard() {
		if(isEmpty())
			return null;
		else
			return cards.get(cards.size()-1); 
	}
	
	private void recalcRun() {
		if(isEmpty())
			run = 0;
		else {
			int i = cards.size();
			do {
				i--;
			} while(i>0 && cards.get(i).isMoveableOn(cards.get(i-1)));
			run = cards.size()-i;
		}
	}
	
	private int fittingCards(Column that) {
		if(isEmpty())
			return 0;
		int i = cards.size();
		do {
			i--;
			if(cards.get(i).fitsOn(that))
				return cards.size()-i; 
		} while(i>hiddenCount && cards.get(i).isMoveableOn(cards.get(i-1)));
		return 0;
		
	}
	
	private int openCards() {
		return cards.size()-hiddenCount;
	}
	
	public boolean fitsOn(Column that) {
		int fittingCards = fittingCards(that);
		if(fittingCards==0)
			return false;
		if(openCards()>fittingCards)  // wenns auf offener Karte liegt, die auch noch die gleiche Farbe hat,
			if(cards.get(cards.size()-fittingCards).isMoveableOn(cards.get(cards.size()-fittingCards-1))) {
				if(that.topCard().equalsSuit(topCard())) //  doch erlauben, wenn Ziel auch farblich passt.
					return true;
				else
					return false; // sonst natürlich nicht.
			}
		return true;
	}

	/**
	 * Moves all possible cards form this to that column
	 * You should not use this method if destination column is empty
	 * @param that destination column
	 * @return the moved cards in textual representation
	 */
	public String moveTo(Column that) {
		if(!fitsOn(that))
			throw new RuntimeException("Cant move this Column={"+this+"} to that Column={"+that+"}");
		int dest = that.cards.size();
		for(int i=fittingCards(that);i>0;i--) {
			that.cards.add(dest,cards.remove(cards.size()-1));
		} 
		if(!isEmpty() && hiddenCount == cards.size()) {
			hiddenCount--;
		}
		that.recalcRun(); // TODO: Performance Issue here.
		that.checkAndRemoveCompleteRun();
		return ""; //TODO: Das geht besser!
	}
	

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<hiddenCount;i++) {
			sb.append('X');
		}
		for(int i=hiddenCount; i<cards.size(); i++) {
			if(i!=hiddenCount)
				sb.append(", ");
			sb.append(cards.get(i).toString());
		}
		return sb.toString();
	}

	public int compareTo(Column that) {
		int tmp = this.cards.size()-that.cards.size();
		if(tmp!=0)
			return tmp;
		for(int i=cards.size()-1;i>0; i--) {
			tmp = this.cards.get(i).ordinal()-that.cards.get(i).ordinal();
			if(tmp!=0)
				return tmp;
		}
		return 0;
	}

	public int getRun() {
		return run;
	}
	
	
}
