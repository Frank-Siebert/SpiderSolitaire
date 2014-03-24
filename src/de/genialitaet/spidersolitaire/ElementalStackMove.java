/**
 * 
 */
package de.genialitaet.spidersolitaire;

/**
 * @author Franky
 *
 */
public class ElementalStackMove extends Move {

	private int src,dest;
	public ElementalStackMove(CompactGameState gameState, int sourceColumn, int destColumn ) {
		super(gameState);
		src = sourceColumn;
		dest = destColumn;
		
	}

	private String toString = null;
	/* (non-Javadoc)
	 * @see de.genialitaet.spidersolitaire.Move#doit()
	 */
	@Override
	public CompactGameState execute() {
		toString = toString();
		GameState gameState = GameState.getGameState(this.gameState);
		//GameState resul
		//System.out.println("Attempting Move "+res+" in game\n"+gameState.toString());
		gameState.columns[src].moveTo(gameState.columns[dest]);
		//System.out.println("New Gamestate:\n"+gameState);
		return new CompactGameState(gameState,this.gameState);
	}
	
	@Override
	public String toString() {
		if(toString==null) {
			return "ElementalStackMove from Column {"+src+"} to Column{"+dest+"}";
		} else {
			return toString;
		}
	}

}
