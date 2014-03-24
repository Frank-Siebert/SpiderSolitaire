package de.genialitaet.spidersolitaire;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.cloudgarden.resource.SWTResourceManager;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class SpiderWindow extends org.eclipse.swt.widgets.Composite {

	private Menu menu1;
	private MenuItem aboutMenuItem;
	private MenuItem contentsMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private MenuItem exitMenuItem;
	private MenuItem closeFileMenuItem;
	private MenuItem newFileMenuItem;
	private MenuItem openFileMenuItem;
	private Button buttonForAbort;
	private Label labelDealtStats;
	private Label labelMemory;
	private Composite composite1;
	private Label labelPerformance;
	private Label labelCompletedRuns;
	private Label label6;
	private Label label5;
	private Label labelEmptyStats;
	private Label label4;
	private Label labelTodo;
	private Label label3;
	private Label labelKnown;
	private Label label2;
	private Label labelDone;
	private Label label1;
	private ProgressBar progressBar;
	private Button buttonForStart;
	private Group groupForControl;
	private Group groupForProgress;
	private List listForGameStates;
	private Canvas canvasForCards;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	private FileDialog fileDialog;
	
	private Thread threadForSolve;
	private GameState initialGameState;

	{
		//Register as a resource user - SWTResourceManager will
		//handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public SpiderWindow(Composite parent, int style) {
		super(parent, style);
		initGUI();
	}
	
	/**
	* Initializes the GUI.
	*/
	private void initGUI() {
		try {
			this.setSize(750, 300);
			this.setBackground(SWTResourceManager.getColor(192, 192, 192));
			GridLayout thisLayout = new GridLayout();
			thisLayout.numColumns = 3;
			thisLayout.marginWidth = 0;
			thisLayout.marginHeight = 0;
			thisLayout.verticalSpacing = 4;
			this.setLayout(thisLayout);
			{
				GridData canvasForCardsLData = new GridData();
				canvasForCardsLData.horizontalSpan = 3;
				canvasForCardsLData.horizontalAlignment = GridData.FILL;
				canvasForCardsLData.grabExcessHorizontalSpace = true;
				canvasForCardsLData.grabExcessVerticalSpace = true;
				canvasForCardsLData.verticalAlignment = GridData.FILL;
				canvasForCards = new Canvas(this, SWT.NONE);
				canvasForCards.setLayoutData(canvasForCardsLData);
				canvasForCards.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent evt) {
						canvasForCardsPaintControl(evt);
					}
				});
			}
			{
				composite1 = new Composite(this, SWT.NONE);
				GridData composite1LData = new GridData();
				composite1LData.widthHint = 168;
				composite1LData.heightHint = 126;
				composite1.setLayoutData(composite1LData);
				composite1.setLayout(null);
				{
					listForGameStates = new List(composite1, SWT.VERTICAL);
					listForGameStates.setBounds(0, 0, 168, 126);
					listForGameStates.addSelectionListener(new SelectionAdapter() { @Override
						public void widgetSelected(SelectionEvent evt) {
							canvasForCards.redraw();
							CompactGameState cgs = listCgs.get(listForGameStates.getSelectionIndex());
							
							listForGameStates.setToolTipText(cgs.infoLong());
						}
						});
				}
			}
			{
				groupForProgress = new Group(this, SWT.NONE);
				GridLayout groupForProgressLayout = new GridLayout();
				groupForProgressLayout.numColumns = 2;
				groupForProgress.setLayout(groupForProgressLayout);
				GridData groupForProgressLData = new GridData();
				groupForProgressLData.horizontalAlignment = GridData.FILL;
				groupForProgressLData.verticalAlignment = GridData.FILL;
				groupForProgressLData.grabExcessHorizontalSpace = true;
				groupForProgress.setLayoutData(groupForProgressLData);
				groupForProgress.setText("Fortschritt");
				{
					label1 = new Label(groupForProgress, SWT.NONE);
					GridData label1LData = new GridData();
					label1LData.horizontalSpan = 2;
					label1.setText("Durchsuchte Spielstati:");
				}
				{
					labelDone = new Label(groupForProgress, SWT.NONE);
					GridData labelDoneLData = new GridData();
					labelDoneLData.horizontalAlignment = GridData.END;
					labelDone.setLayoutData(labelDoneLData);
					labelDone.setText("MMMMMMMM");
					labelDone.setAlignment(SWT.RIGHT);
				}
				{
					label2 = new Label(groupForProgress, SWT.NONE);
					label2.setText("Bekannte Stati:");
				}
				{
					labelKnown = new Label(groupForProgress, SWT.NONE);
					labelKnown.setText("MMMMMMMM");
					GridData labelKnownLData = new GridData();
					labelKnownLData.horizontalAlignment = GridData.END;
					labelKnown.setLayoutData(labelKnownLData);
					labelKnown.setAlignment(SWT.RIGHT);
				}
				{
					label3 = new Label(groupForProgress, SWT.NONE);
					label3.setText("Aufträge");
				}
				{
					labelTodo = new Label(groupForProgress, SWT.NONE);
					labelTodo.setText("MMMMMMMM");
					GridData labelTodoLData = new GridData();
					labelTodoLData.horizontalAlignment = GridData.END;
					labelTodo.setLayoutData(labelTodoLData);
					labelTodo.setAlignment(SWT.RIGHT);
				}
				{
					label4 = new Label(groupForProgress, SWT.NONE);
					label4.setText("EmptyStats:");
				}
				{
					labelEmptyStats = new Label(groupForProgress, SWT.NONE);
					labelEmptyStats.setText("MMMMMM,MMMMMM,MMMMMM,MMMMMM,MMMMMM");
					GridData labelEmptyStatsLData = new GridData();
					labelEmptyStatsLData.horizontalAlignment = GridData.FILL;
					labelEmptyStatsLData.heightHint = 13;
					labelEmptyStats.setLayoutData(labelEmptyStatsLData);
					labelEmptyStats.setAlignment(SWT.RIGHT);
				}
				{
					label5 = new Label(groupForProgress, SWT.NONE);
					label5.setText("dealtStats");
				}
				{
					labelDealtStats = new Label(groupForProgress, SWT.NONE);
					GridData labelDealtStatsLData = new GridData();
					labelDealtStatsLData.horizontalAlignment = GridData.FILL;
					labelDealtStatsLData.heightHint = 13;
					labelDealtStats.setLayoutData(labelDealtStatsLData);
					labelDealtStats.setText("MMMMMM,MMMMMM,MMMMMM,MMMMMM,MMMMMM");
					labelDealtStats.setAlignment(SWT.RIGHT);
				}
				{
					label6 = new Label(groupForProgress, SWT.NONE);
					label6.setText("completedRuns");
				}
				{
					labelCompletedRuns = new Label(groupForProgress, SWT.NONE);
					labelCompletedRuns.setText("MMMMMM,MMMMMM,MMMMMM,MMMMMM,MMMMMM");
					GridData labelCompletedRunsLData = new GridData();
					labelCompletedRunsLData.horizontalAlignment = GridData.FILL;
					labelCompletedRuns.setLayoutData(labelCompletedRunsLData);
					labelCompletedRuns.setAlignment(SWT.RIGHT);
				}
			}
			{
				groupForControl = new Group(this, SWT.NONE);
				GridLayout groupForControlLayout = new GridLayout();
				groupForControlLayout.makeColumnsEqualWidth = true;
				groupForControl.setLayout(groupForControlLayout);
				GridData groupForControlLData = new GridData();
				groupForControlLData.horizontalAlignment = GridData.END;
				groupForControlLData.verticalAlignment = GridData.FILL;
				groupForControl.setLayoutData(groupForControlLData);
				groupForControl.setText("Kontrolle");
				{
					buttonForStart = new Button(groupForControl, SWT.PUSH
						| SWT.CENTER);
					buttonForStart.setText("Berechnen!");
					buttonForStart.addSelectionListener(new SelectionAdapter() { @Override
						public void widgetSelected(SelectionEvent evt) {
							buttonForStartWidgetSelected(evt);
						}
					});
				}
				{
					buttonForAbort = new Button(groupForControl, SWT.PUSH
						| SWT.CENTER);
					GridData buttonForAbortLData = new GridData();
					buttonForAbortLData.heightHint = 23;
					buttonForAbort.setLayoutData(buttonForAbortLData);
					buttonForAbort.setText("Abbrechen");
					buttonForAbort.setEnabled(false);
					buttonForAbort.addSelectionListener(new SelectionAdapter() { @Override
						public void widgetSelected(SelectionEvent evt) {
							buttonForAbort.setEnabled(false);
							System.out.println("buttonForAbort.widgetSelected, event="+ evt);
							threadForSolve.interrupt();
							try {
								threadForSolve.join();
							} catch (InterruptedException e) {
								throw new Error("threadForSolve.join() failed",e);
							}
							threadForSolve = null;
							buttonForStart.setEnabled(true);
						}
					});
				}
				{
					labelPerformance = new Label(groupForControl, SWT.NONE);
					labelPerformance.setText("performance");
				}
				{
					labelMemory = new Label(groupForControl, SWT.NONE);
					labelMemory.setText("memory usage");
				}
			}
			{
				GridData progressBarLData = new GridData();
				progressBarLData.horizontalSpan = 3;
				progressBarLData.verticalAlignment = GridData.END;
				progressBarLData.horizontalAlignment = GridData.FILL;
				progressBar = new ProgressBar(this, SWT.NONE);
				progressBar.setLayoutData(progressBarLData);
			}
			{
				menu1 = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menu1);
				{
					fileMenuItem = new MenuItem(menu1, SWT.CASCADE);
					fileMenuItem.setText("File");
					{
						fileMenu = new Menu(fileMenuItem);
						{
							openFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							openFileMenuItem.setText("Open");
							openFileMenuItem
								.addSelectionListener(new SelectionAdapter() { @Override
								public void widgetSelected(SelectionEvent evt) {
									System.out
										.println("openFileMenuItem.widgetSelected, event="
											+ evt);
									//TODO add your code for openFileMenuItem.widgetSelected
									fileDialog.open();
								}
								});
						}
						{
							newFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							newFileMenuItem.setText("New");
						}
						{
							closeFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							closeFileMenuItem.setText("Close");
						}
						{
							exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							exitMenuItem.setText("Exit");
						}
						fileMenuItem.setMenu(fileMenu);
					}
				}
				{
					helpMenuItem = new MenuItem(menu1, SWT.CASCADE);
					helpMenuItem.setText("Help");
					{
						helpMenu = new Menu(helpMenuItem);
						{
							contentsMenuItem = new MenuItem(helpMenu, SWT.CASCADE);
							contentsMenuItem.setText("Contents");
						}
						{
							aboutMenuItem = new MenuItem(helpMenu, SWT.CASCADE);
							aboutMenuItem.setText("About");
						}
						helpMenuItem.setMenu(helpMenu);
					}
				}
			}
			{
				fileDialog = new FileDialog(getShell(),SWT.OPEN);
				fileDialog.setFilterNames(new String[]{"Spider Save (*.sav)","Any (*.*"});
				fileDialog.setFilterExtensions(new String[]{"*.sav","*.*"});
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		SpiderWindow inst = new SpiderWindow(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.setText("Spider Solitaire Solver");
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
	private void buttonForStartWidgetSelected(SelectionEvent evt) {
		buttonForStart.setEnabled(false);
		//TODO: erst enabled, wenn geladen!
		
		initialGameState = new GameState();
		//HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Explorer\User Shell Folders
		//personal
		initialGameState.loadSpiderSav(System.getenv("USERPROFILE")+"\\Eigene Dateien\\spider.sav");
		threadForSolve = new Thread(new Runnable() {
			public void run() {
				initialGameState.solve(progressCallback);
			}
			
		});
		threadForSolve.setDaemon(true);
		//threadForSolve.setPriority(Thread.MIN_PRIORITY); // enormer Boost, das auszukommentieren!
		//threadForSolve.setPriority(Thread.MAX_PRIORITY);
		//Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // Gui ist unwichtig!
		threadForSolve.start();
		buttonForAbort.setEnabled(true);
	}
	
	private void canvasForCardsPaintControl(PaintEvent evt) {
		int width = canvasForCards.getSize().x;
		int ix = listForGameStates.getSelectionIndex();
		if(ix == -1) {
			return;
		}
		//evt.gc.drawString(listCgs.get(ix).info(), 0, 0);
		GameState gs = new GameState();
		gs.copyFromCompact(listCgs.get(ix));
		
		for(int i=0;i<10;i++) {
			int y=0;
			int x= width*i/10;
			for(int j=0;j<gs.columns[i].cards.size();j++) {
				Card c = gs.columns[i].cards.get(j);
				evt.gc.setForeground(c.suit.color);
				if(j>=gs.columns[i].hiddenCount)
					evt.gc.drawString(" "+c.suit.unicode+c.getChar()+" ", x, y);
				else
					evt.gc.drawString("("+c.suit.unicode+c.getChar()+")", x, y);
					//evt.gc.drawString("?", x, y);
				y+=14;
			}
		}
	}

	private java.util.List<CompactGameState> listCgs = new java.util.ArrayList<CompactGameState>();
	private ProgressCallback progressCallback = new ProgressCallback();
	public class ProgressCallback implements Runnable {
		
		private String emptyStats,
		dealtRowsStats,completedRunsStats;
		
		private int done,known,todo;
		private CompactGameState cgs;
		private int lastProgress;
		
		private final static int ringSize = 250;
		private long[] times = new long[ringSize];
		private int[] doneBuffer = new int[ringSize];
		private int ringIndex = 0;
		
		
		public void set(String emptyStats, String dealtRowsStats, String completedRunsStats,int done,
				int known, int todo, CompactGameState cgs) {
			this.emptyStats = emptyStats;
			this.dealtRowsStats = dealtRowsStats;
			this.completedRunsStats = completedRunsStats;
			this.done = done;
			this.known = known;
			this.todo = todo;
			this.cgs = cgs;
			Display.getDefault().asyncExec(this);
		}
		
		private java.util.List<CompactGameState> sol = null;
		public void solution(java.util.List<CompactGameState> sol) {
			this.sol = sol;
			Display.getDefault().asyncExec(this);
		}
		long lastCall;
		long firstCall;

		public void run() {
			if(sol==null) {
				labelEmptyStats.setText(emptyStats);
				labelDealtStats.setText(dealtRowsStats);
				labelCompletedRuns.setText(completedRunsStats);
				labelDone .setText(String.format("%,d", done));
				labelKnown.setText(String.format("%,d", known));
				labelTodo .setText(String.format("%,d", todo));
				
				long now = System.currentTimeMillis();
				if(now != firstCall)
					labelPerformance.setText(String.format("%6d/s",1000*(done)/(now-firstCall)));
				if(firstCall==0) {
					firstCall = now;
				}

				//labelMemory.setText(Runtime.getRuntime().totalMemory()/1048576+"M");
				int ringNew = (ringIndex+1)%ringSize; 
				doneBuffer[ringNew] = done;
				times[ringNew] = now;
				int ringLongAgo = (ringIndex+2)%ringSize;
				if(times[ringNew]!=times[ringLongAgo])
					labelMemory.setText(String.format("%6d/s",1000*(done-doneBuffer[ringLongAgo])/(now-times[ringLongAgo])));

				ringIndex = ringNew;
				int newProgress = done*100/(123567*6);
				if(newProgress != lastProgress) {
					progressBar.setSelection(newProgress);
					lastProgress = newProgress;
				}
				if(cgs != null) {
					listCgs.add(cgs);
					listForGameStates.add(cgs.info());
				}
			} else {
				buttonForAbort.setEnabled(false);
				listCgs = sol;
				listForGameStates.removeAll();
				//listForGameStates.setRedraw(false);
				for(CompactGameState cgs:sol) {
					listForGameStates.add(cgs.info());
				}
				//listForGameStates.setRedraw(true);
				try {
					threadForSolve.join();
				} catch (InterruptedException e) {
					throw new Error("threadForSolve.join() failed",e);
				}
				threadForSolve = null;
				buttonForStart.setEnabled(true);
			}
		}

	}


}
