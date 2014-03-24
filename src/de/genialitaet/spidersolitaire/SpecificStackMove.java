package de.genialitaet.spidersolitaire;

public class SpecificStackMove extends Move {
	
	private int src, dst;
	/** Number of Cards to move */
	private int n; 
	public SpecificStackMove(CompactGameState cgs, int source, int dest, int cardCount) {
		super(cgs);
		src = source;
		dst = dest;
		n = cardCount;
	}

	@Override
	public CompactGameState execute() {
		GameState gameState = GameState.getGameState(this.gameState);
		int ptr = gameState.columns[dst].cards.size();
		Column source = gameState.columns[src];
		Column dest = gameState.columns[dst]; 
		for(int i=0;i<n;i++) {
			dest.cards.add(ptr, source.cards.remove(source.cards.size()-1));
		}
		if(source.hiddenCount==source.cards.size() && source.hiddenCount >0)
			source.hiddenCount--;
		dest.checkAndRemoveCompleteRun();
		return new CompactGameState(gameState,this.gameState);
	}

}
