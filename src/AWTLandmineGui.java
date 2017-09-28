import java.awt.*;
import java.awt.event.*;
 
// An AWT GUI program inherits the top-level container java.awt.Frame
public class AWTLandmineGui extends Frame implements WindowListener{
	private AWTLandmineModel LandmineModel;
	  // Contains : NeighbourMineCount, MineLocation.
	  // Accessible using getXXX() functions.
	
	private Panel panelDisplay;
	private Panel panelMineField;
	
	private Button[][] btnLocations;  // 2D array of MineField.
	private Button btnRestart;
	private TextField tfTimer, tfMineCounter;
 

	// Constructor to setup GUI components and event handlers
	public AWTLandmineGui () {
		// Set up the LandmineModel.
		this.LandmineModel = new AWTLandmineModel();
		
		// Set up display panel
		this.panelDisplay = new Panel(new FlowLayout()); // MineCounter TextField
		tfMineCounter = new TextField("MineCounter", 5);
		tfMineCounter.setEditable(false);
		panelDisplay.add(tfMineCounter);
		
		btnRestart = new Button("Restart");  // Construct Button
		btnRestart.addMouseListener(new RestartListener());
		panelDisplay.add(btnRestart); 
		
		tfTimer = new TextField("Timer", 5); // Timer TextField.
		tfTimer.setEditable(false);
		panelDisplay.add(tfTimer);
	
		// Set up minefield panel
		int NumRows = LandmineModel.getNumRows();
		int NumCols = LandmineModel.getNumCols();
		this.panelMineField = new Panel(new GridLayout(NumRows, NumCols));
		
		btnLocations = new Button[NumRows][NumCols];  // Construct an array of 400 numeric Buttons

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
	}
 
	// The entry main() method
	public static void main(String[] args) {
		new AWTLandmineGui();  // Let the constructor do the job
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
				System.out.println(i);
				LandmineModel.RightClick(i,j);
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
			LandmineModel.Restart();
			ObserveAndUpdate();
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
					if(LandmineModel.MineLocation[i][j]){
						btnLocations[i][j].setLabel("B");
					}else{
						btnLocations[i][j].setLabel(LandmineModel.NeighbourMineCount[i][j]+"");
					}
				}else{
					// If the location was not revealed.
					btnLocations[i][j].setLabel("");
				}
			}
		}
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