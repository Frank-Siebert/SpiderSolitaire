/**
 * 
 */
package de.genialitaet.spidersolitaire;

/**
 * @author Franky
 * just final for speedUp.
 * Geschwindigskeitsunterschied ist recht groﬂ
 * Vielleicht h‰tte es aber auch schon gereicht, nicht mehr List&lt;E&gt;
 * zu verwenden und stattdessen gleich auf ArrayList gehen...
 * 
 * braucht nur noch 70% der Zeit von vorher.
 */
public final class ArrayList<E> extends java.util.ArrayList<E> {

	public ArrayList(int cardCount) {
		super(cardCount);
	}
}
