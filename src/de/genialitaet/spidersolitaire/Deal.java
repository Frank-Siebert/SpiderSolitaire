package de.genialitaet.spidersolitaire;

public class Deal extends Move {

	public Deal(CompactGameState gameState) {
		super(gameState);
	}

	
	@Override
	public String toString() {
		return "Deal a row.";
	}

	@Override
	public CompactGameState execute() {
		return new CompactGameState(gameState.getGameState().deal(),this.gameState);
	}

}
