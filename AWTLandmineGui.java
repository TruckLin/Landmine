import java.awt.*;
import java.awt.event.*;
 
// An AWT GUI program inherits the top-level container java.awt.Frame
public class AWTLandmineGui extends Frame implements WindowListener{
	private AWTLandmineModel LandmineModel;
	  // Contains : NeighbourMineCount, MineLocation.
	  // Accessible using getXXX() functions.
	
	private Panel panelDisplay;
	private Panel panelMineField; // first layer (labels)
	
	private Button[][] btnLocations;  // 2D array of MineField Bottuns.
	private Button btnRestart;
	private TextField tfTimer, tfMineCounter;
	
	TimeCounter TimeThread;
 

	// Constructor to setup GUI components and event handlers
	public AWTLandmineGui () {
		// Set up the LandmineModel.
		this.LandmineModel = new AWTLandmineModel();
		
		// Set up display panel
		this.panelDisplay = new Panel(new FlowLayout()); // MineCounter TextField
		tfMineCounter = new TextField(LandmineModel.NumOfMineLeft+"", 5);
		tfMineCounter.setEditable(false);
		panelDisplay.add(tfMineCounter);
		
		btnRestart = new Button(":|");  // Construct Button
		btnRestart.addMouseListener(new RestartListener());
		panelDisplay.add(btnRestart); 
		
		tfTimer = new TextField("Timer", 5); // Timer TextField.
		tfTimer.setEditable(false);
		panelDisplay.add(tfTimer);

		// Set up minefield panels
		int NumRows = LandmineModel.getNumRows();
		int NumCols = LandmineModel.getNumCols();
		this.panelMineField = new Panel(new GridLayout(NumRows, NumCols));
		
		btnLocations = new Button[NumRows][NumCols];  // Construct an array of buttons of size NumRows*NumCols.
		
		MineFieldListener listener = new MineFieldListener(); // Add a listener for all the btnMine;
		
		for(int i = 0; i < NumRows ; i++){
			for(int j = 0; j < NumCols ; j++){
				btnLocations[i][j] = new Button("");  // Construct Button
				btnLocations[i][j].addMouseListener(listener);
				btnLocations[i][j].setActionCommand(i+","+j); // Store the indices as string in ActionCommand for later use.
				
				panelMineField.add(btnLocations[i][j]);
			}
		}
 
		setLayout(new BorderLayout());  // "super" Frame sets to BorderLayout
		add(panelDisplay, BorderLayout.NORTH);
		add(panelMineField, BorderLayout.CENTER);
 
		addWindowListener(this);
			// "super" Frame (source object) fires WindowEvent.
			// "super" Frame adds "this" object as a WindowEvent listener.
		
		setTitle("BorderLayout Demo"); // "super" Frame sets title
		setSize(NumCols*20,NumRows*20+20); // "super" Frame sets initial size
		setVisible(true);              // "super" Frame shows
		
		// Start the timer.
		TimeThread = new TimeCounter();
		TimeThread.start();
	}
	
	/**
    * MineFieldListener is a "named inner class" used as MouseListener for MineField Buttons.
    * This inner class can access private variables of the outer class.
	*/
	private class MineFieldListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent evt) {
			
			Button btn = (Button) evt.getSource();
			String AC = btn.getActionCommand();
			int sepIndex = AC.indexOf(',');
			String firstnum = AC.substring(0,sepIndex);
			String secondnum = AC.substring(sepIndex+1);
			
			int i = Integer.parseInt(firstnum);
			int j = Integer.parseInt(secondnum);
			if(evt.getButton()==3){
				// Right click event.
				//System.out.println("("+i+","+j+") Right Clicked.");//testing
				LandmineModel.RightClick(i,j);
				tfMineCounter.setText(LandmineModel.NumOfMineLeft+"");
			}else{
				// Left click event.
				//System.out.println(btn.getActionCommand());
				LandmineModel.LeftClick(i,j);
			}
			ObserveAndUpdate(); // Updates the Gui all together.
		}
		// Not used - need to provide an empty body to compile.
		@Override public void mousePressed(MouseEvent evt) { }
		@Override public void mouseReleased(MouseEvent evt) { }
		@Override public void mouseEntered(MouseEvent evt) { }
		@Override public void mouseExited(MouseEvent evt) { }
	}
	
	/**
	* RestartListener is a "named inner class" used as MouseListener for Restart Buttons.
	*/
	private class RestartListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent evt) {
			// reset the LandmineModel.
			LandmineModel = new AWTLandmineModel();
			// Reset Gui================================================
			// Set up minefield panels
			int NumRows = LandmineModel.getNumRows();
			int NumCols = LandmineModel.getNumCols();
			panelMineField.removeAll();
		
			for(int i = 0; i < NumRows ; i++){
				for(int j = 0; j < NumCols ; j++){
					btnLocations[i][j].setLabel("");
					panelMineField.add(btnLocations[i][j]);
				}
			}
			// Reset restart button
			btnRestart.setLabel(":|");
			//==========================================================
			// Enable the buttons again as we diable the buttons when win or lose.
			panelMineField.setEnabled(true);
			// Reset the mine count text field.
			tfMineCounter.setText(LandmineModel.NumOfMineLeft+"");
			// Reset the timer time.
			TimeThread.restart();

		}
		// Not used - need to provide an empty body to compile.
		@Override public void mousePressed(MouseEvent evt) { }
		@Override public void mouseReleased(MouseEvent evt) { }
		@Override public void mouseEntered(MouseEvent evt) { }
		@Override public void mouseExited(MouseEvent evt) { }
	}
	
	/* This function observes the global variable LandmineModel
	*  and updates btnLocations,btnRestart,tfTimer,tfMineCounter accordingly.
	*/
	private void ObserveAndUpdate(){
		// Updates btnLocations
		int NumRows = LandmineModel.NumRows;
		int NumCols = LandmineModel.NumCols;
		for(int i=0; i<NumRows; i++){
			for(int j=0; j<NumCols; j++){
				if(LandmineModel.LocationRevealed[i][j]){
					ReplaceButton(i,j);
				}else if(LandmineModel.LocationMarked[i][j]){
					btnLocations[i][j].setLabel("M");
					
					// testing
					//System.out.println("LocationsMarked[i][j] = "+LandmineModel.LocationMarked[i][j]);
					
				}else{
					btnLocations[i][j].setLabel(""); // if location not revealed and not marked, setLabel(""); This also remove any "M" that we don't want.
					//System.out.println("LocationsMarked[i][j] = "+LandmineModel.LocationMarked[i][j]);
					continue;
				}
			}
		}
		if(LandmineModel.Win){
			btnRestart.setLabel(":)");
			
			// We need to disable all btnLocations buttons. We can easily disable the container.
			panelMineField.setEnabled(false);
		}else if(LandmineModel.Lose){
			btnRestart.setLabel(":(");
			
			// Agin, we need to dable the buttons
			panelMineField.setEnabled(false);
		}else{
			btnRestart.setLabel(":|");
		}
	}
	
	public void ReplaceButton(int i, int j){
		// This method should disable button at location (i,j).
		// Replace button with a Label component.
		//btnLocations[i][j].setEnabled(false);
		//btnLocations[i][j].setBackground(Color.white);
		//btnLocations[i][j].setForeground(Color.black);
		Rectangle rec = btnLocations[i][j].getBounds();
		panelMineField.remove(btnLocations[i][j]);
		
		Label temp = new Label(LandmineModel.NeighbourMineCount[i][j]+"");
		
		if(LandmineModel.MineLocation[i][j]){
			temp.setText("!");
		}else if(LandmineModel.NeighbourMineCount[i][j] > 0){
			//Location not a landmine and MineCount > 0
			temp.setText(LandmineModel.NeighbourMineCount[i][j]+"");
		}else{
			temp.setText("");
		}
		panelMineField.add(temp);
		temp.setBounds(rec);
		
		//Testing
		//System.out.println("Rectangle = "+rec.toString());
		//System.out.println("(x,y,width,height) = ("+x+","+y+","+Width+","+Height+")");
	}
	
	/**
	*	This named inner class TimeCounter extends Thread, which allow
	*   us to count time in another thread and play the game in main thread.
	*/
	class TimeCounter extends Thread {
		long StartTime;
		TimeCounter(){
			StartTime = System.currentTimeMillis();
		}
		public void restart(){
			StartTime = System.currentTimeMillis();
		}
		public void run() {
			try {
				boolean stop = false;
				while(!stop){
					long rightNow = System.currentTimeMillis();
					int second = (int)((rightNow - StartTime)/1000);
					tfTimer.setText(second+"");
					Thread.sleep(500);
				}
			}catch (InterruptedException e) {
					System.out.println("Thread interrupted.");
			}
		}
	}
	
	
	
	// The entry main() method
	public static void main(String[] args) {
		new AWTLandmineGui();  // Let the constructor do the job
	}
	
	/* WindowEvent handlers */
	// Called back upon clicking close-window button
	@Override
	public void windowClosing(WindowEvent evt) {
		System.exit(0);  // Terminate the program
	}
 
	// Not Used, but need to provide an empty body to compile.
	@Override public void windowOpened(WindowEvent evt) { }
	@Override public void windowClosed(WindowEvent evt) { }
	@Override public void windowIconified(WindowEvent evt) { }
	@Override public void windowDeiconified(WindowEvent evt) { }
	@Override public void windowActivated(WindowEvent evt) { }
	@Override public void windowDeactivated(WindowEvent evt) { }
}