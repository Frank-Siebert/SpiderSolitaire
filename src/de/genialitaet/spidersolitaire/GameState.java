package de.genialitaet.spidersolitaire;

import java.io.*;
import java.util.*;



public class GameState {
	/* Package visibility intensional. */
	Column columns[] = new Column[10];
	Deck deck;
	int completeRunCount = 0;
	//Suit[] completeRun = new Suit[8];
	int dealtRows = 0;
	
	public int getCardCount() {
		int res = 0;
		for(int i=0; i<10; i++) {
			res += columns[i].cards.size();
		}
		return res;
		// completeRunCount könnte falsch sein!
		//return 54+10*dealtRows-13*completeRunCount;
	}
	
	
	
	//final static int[] dealtRowsPenalty = new int[]{230,215,200,196,195, 188}; //freundlich
	final static int[] dealtRowsPenalty = new int[]{00,1000,2000,3000,4000,5000}; // verhindert deal()
	public int getChaos() {
		//try {
		return getChaos(true);
		//} catch(RuntimeException e) {
		//	e.printStackTrace(System.err);
		//	System.err.println(this);			
		//	throw e;
		//}
	}
	
	private int getChaos(boolean check) {
		if(getCardCount()>=1 && getCardCount()<10)
			return Integer.MAX_VALUE;
		int res = 0;
		int emptyColumns = getEmptyColumns();
		if(dealtRows==5) // TODO: ist das nötig? - nicht mehr, 2011
			emptyColumns = Math.max(emptyColumns, 2); // 2 ist auch die Grenze in Card.getChaos
		for(Column c:columns) {
			//res += Math.round(Math.sqrt(c.getChaos(emptyColumns)));
			res += c.getChaos(emptyColumns);
		}
		res += dealtRowsPenalty[dealtRows];
		completeRunCount = (3*getCardCount() +8) % 10; // stimmt trotzdem nicht. Jetzt doch: zuvor wurde getCardCount() mit completeRunCount ausgerechnet, da beißt sich die Katze natürlich in den Schwanz.
		res += 2*(8-completeRunCount);
		
		return res;
	}
	
	private static GameState singleton = new GameState();
	public static GameState getGameState(CompactGameState cgs) {
		singleton.copyFromCompact(cgs.deck, cgs.data, cgs.columnNumbers);
		return singleton;
	}
	
	/*private GameState(CompactGameState cgs) {
		this(cgs.deck,cgs.data,cgs.columnNumbers);
	}*/
	
	public GameState(Deck deck, byte[] data, byte[] columnNumbers) {
		copyFromCompact(deck, data, columnNumbers);
	}
	
	// only to be called from SpiderWindow.display
	void copyFromCompact(CompactGameState cgs) {
		copyFromCompact(cgs.deck, cgs.data, cgs.columnNumbers);
	}
	
	private void copyFromCompact(Deck deck, byte[] data, byte[] columnNumbers) {
		this.deck = deck;
		int ptr = 0;
		for(int k=0; k<10; k++) {
			int i = columnNumbers[k];
			byte height = data[ptr++];
			byte hidden = data[ptr++];
			if(columns[i]==null) 
				columns[i] = new Column(height+12); // TODO: 12 tweaken
			else {
				columns[i].clear();
			}
			//columns[i] = new Column(); 
			for(int j=0; j<height; j++) {
				columns[i].addCard(Card.get(data[ptr++]), j>=hidden);
			}
		}
		int cards = ptr - 20; // theoretisch überflüssig, da kongruent mod 10,
		// aber wie soll ich das morgen noch verstehen?
		/*
		 * cards          = 54 + dealtRows * 10 - 13*completeRun  | mod 10
		 * cards          = 4 +7*completeRun | Seiten vertauschen, 6 rüber
		 * 7*completeRun  = cards mod 10 + 6 | *3 (invers zu 7)
		 * 21*completeRun = 3*cards + 8
		 * completeRun    = (3*cards + 8) mod 10
		 */
		completeRunCount = (3*cards +8) % 10;
		dealtRows = (cards - 54 + completeRunCount*13) / 10;
		//System.out.printf("GameState from Compact Constructor,\n%3d cards\n%d complete runs\n%d dealtRows\n",
		//		cards,completeRunCount,dealtRows);
	}

	public GameState() {
		; //everything initialized?!
	}

	/**
	 * omFg, why is byte signed??????????????
	 * @param b
	 * @return b treated as a byte should
	 */
	public static int JAVASUCKS(byte b) {
		//@SuppressWarnings
		return b & 0xFF;
	}
	
	public void loadSpiderSav(String spiderSav) {
		int[] raw = new int[0x48+104+20+1];
		InputStream in = null;
		try {
			in = new FileInputStream(spiderSav);
			int i = 0;
			byte[] buf = new byte[4];
			while(true) {
				if(in.read(buf)!=4 || buf[0] == -1)
					break;
				raw[i] = JAVASUCKS(buf[0]) | (JAVASUCKS(buf[1])<<8) | (JAVASUCKS(buf[2])<<16) | (JAVASUCKS(buf[3]) <<24);
				i++;
			}
			in.close();
		} catch (IOException e) {
			System.err.println("could not open "+spiderSav);
			throw new RuntimeException(e);
		}
		
		deck = new Deck(raw[0],raw[1]);
		// cardCount1 = raw[2]; // excluding the undealt cards.
		// cardCount2 = raw[3]; // further excluding the completed runs.
		// moves = raw[4];
		dealtRows = raw[5];
		completeRunCount = raw[6] + raw[7] + raw[8] + raw[9]; // club..spades
		/*for(int i=0; i<completeRunCount;i++) {
			completeRun[i] = Suit.values()[raw[10+i]];
		}*/
		int ptr = 18; // data start.
		for(int i=0; i<10; i++) {
			columns[i] = new Column();
			int height = raw[ptr++];
			int hidden = raw[ptr++];
			for(int j=0; j<height; j++) {
				columns[i].addCard(deck.cards.get(raw[ptr++]), j>=hidden);
			}
		}
		// score = raw[ptr]
	}
	
	public int getEmptyColumns() {
		int res = 0;
		for(int i=0;i<10;i++) {
			if(columns[i].isEmpty())
				res++;
		}
		return res;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Dealtrows=");
		sb.append(dealtRows);
		sb.append(", CompletedRuns=");
		sb.append(completeRunCount);
		for(int i=0;i<10;i++) {
			sb.append("\nColumn ");
			sb.append(i);
			sb.append(':');
			sb.append(columns[i].toString());
		}
		return sb.toString();
	}
	
	public boolean isDealPossible() {
		if(dealtRows<5) {
			boolean anyFree = false;
			for(int i=0;i<10;i++) {
				if(columns[i].cards.size()==0)
					anyFree = true;
			}
			return !anyFree;
		}
		return false;
	}
	
	public java.util.List<Move> possibleMoves(CompactGameState cgs) {
		java.util.List<Move> res = new java.util.LinkedList<Move>();
//TODO: Es macht nie Sinn, eine Kreuz 9 auf eine Herz 10 zu legen, wenn man sie auch auf eine Kreuz 10 legen kann?!
		for(int i=0;i<10;i++) {
			boolean emptyCol = false; // bei mehr als einer leeren Spalte kann man was sparen.
			// FIXME: 2011: wenn nochmal eine Reihe gegeben wird, ist das Sparen falsch.
			for(int j=0;j<10;j++) {
				if(columns[j].isEmpty()) { 
					if(!emptyCol) {
						for(int k=columns[i].getRun();k>0;k--) {
							res.add(new SpecificStackMove(cgs,i,j,k));
						}
						emptyCol = true;
					}
				} else if(columns[i].fitsOn(columns[j]))
					res.add(new ElementalStackMove(cgs,i,j));
			
			}
		}
		
		if(isDealPossible()) res.add(new Deal(cgs));
		//System.out.println("Possible Moves: "+res.toString());
		//cachedPossibleMoves = new java.lang.ref.WeakReference<List<Move>>(res);
		return res;
	}
	
	public GameState deal() {
		if(dealtRows>=5) {
			throw new Error("Can't deal more than 5 times!");
		}
		for(int i=0;i<10;i++) {
			columns[i].addCard(deck.cards.get(54+i+10*dealtRows), true);
		}
		dealtRows++;
		return this;
	}

	public boolean isWon() {
		return completeRunCount == 8;
	}
	
	public static void main(String[] args) {
		GameState game = new GameState();
		game.loadSpiderSav("G:\\Dokumente und Einstellungen\\Franky\\Eigene Dateien\\spider.sav");
		final int MAX = 50000;
		System.out.println(game);
		CompactGameState c = new CompactGameState(game,null);
		for(int warmupHotSpot=0;warmupHotSpot<2;warmupHotSpot++) {
			long start = System.currentTimeMillis();
			GameState[] dummy = new GameState[MAX];
			for(int i=0;i<MAX;i++) {
				//dummy[i] = new GameState(c);
			}
			System.out.println("Time with new: "+(System.currentTimeMillis()-start));
			start = System.currentTimeMillis();
			for(int i=0;i<MAX;i++) {
				dummy[i].copyFromCompact(c.deck, c.data, c.columnNumbers);
			}
			System.out.println("Time without new: "+(System.currentTimeMillis()-start));
			start = System.currentTimeMillis();
			int res = 0;
			for(int i=0;i<MAX;i++) {
				res += dummy[i].getChaos();
			}
			System.out.println("total Chaos "+res+", time: "+(System.currentTimeMillis()-start));
			System.gc();
			res = 0;
			CompactGameState[] cdummy = new CompactGameState[MAX];
			start = System.currentTimeMillis();
			for(int i=0;i<MAX;i++) {
				cdummy[i] = new CompactGameState(dummy[i],null/*previous*/);
			}
			System.out.println("Time for cgs from gs: "+(System.currentTimeMillis()-start));
			
			start = System.currentTimeMillis();
			
			for(int i=0;i<MAX;i++) {
				res += dummy[i].possibleMoves(cdummy[i]).size();
			}
			System.out.println("total Possibilities "+res+", time: "+(System.currentTimeMillis()-start));
			System.gc();
			res = 0;
			start = System.currentTimeMillis();
			for(int i=0;i<MAX;i++) {
				for(Move m : dummy[i].possibleMoves(cdummy[i])) {
					res+=m.execute().chaos;
				}
			}
			System.out.println("Moves.execute "+res+", time: "+(System.currentTimeMillis()-start));
			System.gc();
		}
	}
	
	public void solve(SpiderWindow.ProgressCallback progressCallback) {
		GameState gs = this;
		long lastProgress = Long.MIN_VALUE;
		// eigentlich beide gleichschnell, auf 233000ms getestet.
		final Map<CompactGameState,CompactGameState> searched = new HashMap<CompactGameState,CompactGameState>(400000,0.8f); // slower solution
		//Map<CompactGameState,CompactGameState> searched = new TreeMap<CompactGameState,CompactGameState>(); // slower solution
		//Queue<CompactGameState> current  = new LinkedList<CompactGameState>();
		// chaos implemented in compareTo of CompactGameState 
		final Queue<CompactGameState> current  = new PriorityQueue<CompactGameState>();
		CompactGameState cgs = new CompactGameState(gs,null); 
		current.add(cgs );
		searched.put(cgs,cgs);
		CompactGameState solution = null;
		int possibilitySum = 0;
		int done = 0;
		int[] emptyColumnsStatistic = new int[11];
		int[] dealtRowsStatistic = new int[6];
		int[] completedRunsStatistic = new int[9];
		int dealtRowsRecord = 0;
		while(!current.isEmpty()) {
			if(Thread.interrupted())
				return;
			cgs = current.poll();
			//searched.add(cgs);
			gs = cgs.getGameState();
			if(dealtRowsRecord<gs.dealtRows)
				dealtRowsRecord = gs.dealtRows;
			if(dealtRowsRecord<5 && (dealtRowsStatistic[dealtRowsRecord]>=100000 || searched.size()>600000)) {
				Queue<CompactGameState> tmp = new PriorityQueue<CompactGameState>(searched.keySet());
				current.clear();
				//TreeSet<CompactGameState> tmp2 = new TreeSet<CompactGameState>(searched.keySet());
				int i=0;
				while(!tmp.isEmpty()) {
					if(Thread.interrupted())
						return;
					//cgs = tmp2.first();
					//tmp2.remove(cgs);
					cgs = tmp.poll();
					if(cgs.getDealtRows() != dealtRowsRecord)
						continue;
				
					gs = cgs.getGameState();
					if(gs.isDealPossible()) {
						current.add(new Deal(new CompactGameState(gs,cgs)).execute());
						i++;
						if(i>1000) {
							break;
						}
					}	
				}
				dealtRowsRecord++;
				searched.clear();
				System.gc();
				continue;
			}
			
			done++;
			emptyColumnsStatistic[gs.getEmptyColumns()]++;
			dealtRowsStatistic[gs.dealtRows]++;
			completedRunsStatistic[gs.completeRunCount]++;
			if(lastProgress+40<=System.currentTimeMillis() || done%13456==0 || gs.isWon()) {
				lastProgress = System.currentTimeMillis();
				String es = new String();
				for(int i=0;i<=10;i++) {
					if(emptyColumnsStatistic[i]==0) break;
					es += emptyColumnsStatistic[i]+",";
				}
				String ds = new String();
				for(int i=0;i<=5;i++) {
					ds += (dealtRowsStatistic[i]+",");
				}
				String cs = new String();
				for(int i=0;i<=8;i++) {
					cs += (completedRunsStatistic[i]+",");
				}
				
				progressCallback.set(es, ds, cs, done, searched.size(), current.size(),done%13456==0 ? cgs:null);
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
				if(!searched.containsKey(cgs2) /*&&!current.contains(cgs2)*/) {
					current.add(cgs2);
					searched.put(cgs2,cgs2);
				} else { // Optimierung für kurze Pfade
					CompactGameState cgs3 = searched.get(cgs2);
					if(cgs2.getDepth()<cgs3.getDepth()) {
						cgs3.previous = cgs2.previous;
						// TODO: Spaltensortierung kopieren
					}
				}
			}
		}
		if(solution == null) {
			System.out.println("no solution!");
		} else {
			//System.out.println("Solution found!");
			progressCallback.solution(solution.path());
		}
	}
}
