/**
 * 
 */
package de.genialitaet.spidersolitaire;

/**
 * @author Franky
 *
 */
public abstract class Move {
	protected CompactGameState gameState;
	public Move(CompactGameState gameState) { 
		this.gameState = gameState;
	}
	public abstract CompactGameState execute();
	/* dont need this anymore */
	//abstract void undo();
}
