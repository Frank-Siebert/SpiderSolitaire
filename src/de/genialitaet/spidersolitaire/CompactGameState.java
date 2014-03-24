/**
 * 
 */
package de.genialitaet.spidersolitaire;

import java.util.*;
/**
 * @author Franky
 *
 */
public class CompactGameState implements Comparable<CompactGameState> {
	byte[] data;
	Deck deck;
	CompactGameState previous = null;
	int chaos;
	byte[] columnNumbers = new byte[10];

	CompactGameState(GameState src, CompactGameState previous) {
		this(src,previous, true); 
	}

	CompactGameState(GameState src, CompactGameState previous,boolean setChaos) {
		byte[] tmp = new byte[124];
		int ptr = 0;
		Column[] columns = new Column[10];
		for(byte i=0;i<10;i++) { 
			columns[i] = src.columns[i]; 
			columns[i].number = i;
		}
		// Macht doch einen gelösten Eindruck, oder?!
		if(src.dealtRows==5)
			java.util.Arrays.sort(columns); 
		for(int i=0;i<10;i++) {
			Column col = columns[i];
			columnNumbers[i] = col.number;
			tmp[ptr++] = (byte)col.cards.size();
			tmp[ptr++] = (byte)col.hiddenCount;
			for(int j=0;j<col.cards.size();j++) {
				tmp[ptr++] = col.cards.get(j).ordinal();
			}
		}
		this.previous = previous;
		//try { 
			if(setChaos)
				chaos = src.getChaos()+getDepth();
		//} catch(RuntimeException e) {
		//	System.err.println("Previous GameState: "+previous.getGameState());
		//	throw e;
		//}
		data = new byte[ptr];
		while(--ptr>=0) {
			data[ptr] = tmp[ptr];
		}
		deck = src.deck;
		//largeGameState = new java.lang.ref.SoftReference<GameState>(src);
	}
	
	public void check() {
		/*GameState gs = new GameState(deck,data,columnNumbers);
		if(!equals(new CompactGameState(gs,previous,false))|| Math.abs(gs.getChaos()-chaos)>1000) { // wenn dies false true gesetzt wird, entsteht eine Endlosschleife!
			System.err.println("fehler beim Übergang der Gametati");
			System.err.println(previous.getGameState());
			System.err.println("\n\nzu\n");
			System.err.println(getGameState());
			throw new Error("Gamestate wrong: did not remove complete row");

		}*/
	}
	
	public int getCompleteRunCount() {
		int cards = data.length - 20; // theoretisch überflüssig, da kongruent mod 10,
		// aber wie soll ich das morgen noch verstehen?
		/*
		 * cards          = 54 + dealtRows * 10 - 13*completeRun  | mod 10
		 * cards          = 4 +7*completeRun | Seiten vertauschen, 6 rüber
		 * 7*completeRun  = cards mod 10 + 6 | *3 (invers zu 7)
		 * 21*completeRun = 3*cards + 8
		 * completeRun    = (3*cards + 8) mod 10
		 */
		return (3*cards +8) % 10;
	}

	public int getDealtRows() {
		int cards = data.length - 20;
		return (cards - 54 + getCompleteRunCount()*13) / 10;
	}

	
	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;
		if(o instanceof CompactGameState) {
			CompactGameState that = (CompactGameState)o;
			return java.util.Arrays.equals(this.data         , that.data) &&
			       java.util.Arrays.equals(this.columnNumbers, that.columnNumbers);
		} else
			return false;
	}
	
	@Override
	public int hashCode() {
		return java.util.Arrays.hashCode(data);
	}
	
	public GameState getGameState() {
		//return new GameState(deck, data,columnNumbers);
		return GameState.getGameState(this);
	}
	
	public int getDepth() {
		int res = 0;
		CompactGameState cgs = this;
		while((cgs=cgs.previous)!=null)
			res++;
		return res;
	}
	
	public static void solve(String fileName) {
		GameState gs = new GameState();
		gs.loadSpiderSav(fileName);
		long startTime = System.currentTimeMillis();
		// eigentlich beide gleichschnell, auf 233000ms getestet.
		Set<CompactGameState> searched = new HashSet<CompactGameState>(200000,0.8f); // slower solution
		//Set<CompactGameState> searched = new TreeSet<CompactGameState>(); // faster solution
		//Queue<CompactGameState> current  = new LinkedList<CompactGameState>();
		// chaos implemented in compareTo of CompactGameState 
		Queue<CompactGameState> current  = new PriorityQueue<CompactGameState>();
		CompactGameState cgs = new CompactGameState(gs,null); 
		current .add(cgs);
		searched.add(cgs);
		CompactGameState solution = null;
		int possibilitySum = 0;
		int done = 0;
		//CompactGameState[] delTmp = new CompactGameState[200000];
		int[] emptyColumnsStatistic = new int[11];
		int[] dealtRowsStatistic = new int[6];
		int[] completedRunsStatistic = new int[9];
		while(!current.isEmpty()) {
			cgs = current.poll();
			//searched.add(cgs);
			done++;
			gs = cgs.getGameState();
			
			if(done%134567==0 &&gs.dealtRows<5) {
				System.out.println("Cleaning");
				Queue<CompactGameState> tmp = new PriorityQueue<CompactGameState>(current); 
				current.clear();
				int i=0;
				do {
					gs = cgs.getGameState();
					if(gs.isDealPossible()) {
//						CompactGameState cgs2; 
//						current.add(cgs2 = new Deal(new CompactGameState(gs,)).execute());
//						cgs2.previous = cgs;
						throw new Error("Auskommentierungen, funzt nicht mehr!");
					}	
					i++;
					if(i>1000)
						break;
				} while((cgs=tmp.poll())!=null);
				Set<CompactGameState>searched2 = new HashSet<CompactGameState>(searched);
				for(CompactGameState cgs2:searched2) {
					if(cgs2.getDealtRows()<5) {
						searched.remove(cgs2);
					}
				}
				for(CompactGameState cgs2:current) {
					cgs = cgs2;
					while(cgs!=null) {
						searched.add(cgs);
						cgs = cgs.previous;
					}
				}
				searched2.clear();
				searched2 = null;
				System.gc();

				System.out.println("Cleaned");
				continue;
					
			}
			
			
			emptyColumnsStatistic[gs.getEmptyColumns()]++;
			dealtRowsStatistic[gs.dealtRows]++;
			completedRunsStatistic[gs.completeRunCount]++;
			if(done%5000==0) {
				System.out.print("\nEmptystats: ");
				for(int i=0;i<=10;i++) {
					if(emptyColumnsStatistic[i]==0) break;
					System.out.print(emptyColumnsStatistic[i]+",");
				}
				System.out.print(" DealtStats: ");
				for(int i=0;i<=5;i++) {
					System.out.print(dealtRowsStatistic[i]+",");
				}
				System.out.print(" completedRunsStats: ");
				for(int i=0;i<=8;i++) {
					System.out.print(completedRunsStatistic[i]+",");
				}
				System.out.println();
				
				System.out.print("Chaos="+cgs.chaos+", ");
				System.out.println(gs.toString());
				System.out.println("Depth="+cgs.getDepth()+"; Durchsuchte Spielstati "+done+", known: "+searched.size()+", todo "+current.size()+
						" Zeit:"+(System.currentTimeMillis()-startTime)+" avg_poss="+(1.0*possibilitySum/done));
			}
			if(gs.isWon()) {
				solution = cgs;
				break;
			}
			
			List<Move> possibleMoves = gs.possibleMoves(cgs);
			for(Move move:possibleMoves) {
				possibilitySum++;
				CompactGameState cgs2 = move.execute();
				cgs2.previous = cgs;
				if(!searched.contains(cgs2) /*&&!current.contains(cgs2)*/) {
					current.add(cgs2);
					searched.add(cgs2);
				}
				if(cgs2.data.length==20) {
					solution = cgs2;
					current.clear();
					break;
				}
			}
		}
		if(solution == null) {
			System.out.println("no solution!");
		} else {
			System.out.println("Solution found!");
			solution.displayPath();
			System.out.println("moves="+solution.getDepth());
		}
		
		System.out.println("Final Stats");
		System.out.print("\nEmptystats: ");
		for(int i=0;i<=10;i++) {
			if(emptyColumnsStatistic[i]==0) break;
			System.out.print(emptyColumnsStatistic[i]+",");
		}
		System.out.print(" DealtStats: ");
		for(int i=0;i<=5;i++) {
			System.out.print(dealtRowsStatistic[i]+",");
		}
		System.out.print(" completedRunsStats: ");
		for(int i=0;i<=8;i++) {
			System.out.print(completedRunsStatistic[i]+",");
		}
		System.out.println();
	}
	
	public void displayPath() {
		if(previous!=null)
			previous.displayPath();
		System.out.println(GameState.getGameState(this).toString());
		System.out.println();
	}
	
	public java.util.List<CompactGameState> path() {
		CompactGameState ptr = this;
		java.util.LinkedList<CompactGameState> res = new java.util.LinkedList<CompactGameState>();
		do {
			res.add(0,ptr);
			ptr = ptr.previous;
		} while(ptr!=null);
		return res;
	}
	
	public static void main(String[] args) {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		solve("G:\\Dokumente und Einstellungen\\Franky\\Eigene Dateien\\spider.sav");

	}

	//public int compareTo(Object arg0) {
	//	CompactGameState that = (CompactGameState)arg0;
	public int compareTo(CompactGameState that) {
		int tmp = this.chaos-that.chaos;
		if(tmp!=0)
			return tmp;
		int len = this.data.length - that.data.length;
		if(len !=0)
			return len;
		len = data.length;
		while(--len>=0) {
			tmp = this.data[len] - that.data[len];
			if(tmp!=0)
				return tmp;
		}
		assert(equals(that));
		return 0;
	}

	public String info() {
		return getCompleteRunCount()+"/"+getDealtRows()+"/"+chaos+"/"+getDepth();
	}
	
	public String infoLong() {
		return getCompleteRunCount()+" Complete Runs, "+getDealtRows()+" Dealt Rows, "+chaos+" Chaos value, "+getDepth()+" Moves";
	}
}
