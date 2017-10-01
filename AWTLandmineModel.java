import java.lang.Math;
import java.util.Random;

public class AWTLandmineModel {
	
	public int NumRows;
	public int NumCols;
	public int NumMines;
	public int NumOfMineLeft;
	public int CurrentTime;
	public int[][] NeighbourMineCount; // Holds the number of neighbouring landmines.
	public boolean[][] MineLocation; // Booleans indicating whether the location contains a landmine.
	public boolean[][] LocationRevealed; // Booleans indicating whether the location is revealed by player.
	public boolean[][] LocationMarked; // Booleans indicating whether the location is marked by player.

	public boolean Win;
	public boolean Lose;
	/* Constructors */
	public AWTLandmineModel (){
		this.NumRows = 15;
		this.NumCols = 10;
		this.NumMines = 10; // testing
		this.NumOfMineLeft = NumMines;
		this.CurrentTime = 0;
		this.NeighbourMineCount = new int[NumRows][NumCols]; // initialised to zeros by java.
		this.MineLocation = new boolean[NumRows][NumCols]; // initialised to false by java.
		this.LocationRevealed = new boolean[NumRows][NumCols]; // initialised to false by java.
		this.LocationRevealed = new boolean[NumRows][NumCols]; // initialised to false by java.
		this.LocationMarked = new boolean[NumRows][NumCols]; // initialised to false by java.
		
		this.Win = false;
		this.Lose = false;
		// The following setion creates landmines and fill in minecounts.
		int i = 0;
		while( i<NumMines ){
			int row = (int) Math.floor(Math.random()*NumRows);
			int col = (int) Math.floor(Math.random()*NumCols);
			if(MineLocation[row][col]==true){
				continue;
			}else{
				MineLocation[row][col] = true; // Generate bombs randomly on minefield
				
				// Add 1 to all the Neibouring location bombs.
				int[][] Nbrs = this.getNeighbourIndices(row,col);
				for(int k = 0; k<Nbrs.length ; k++){
					NeighbourMineCount[Nbrs[k][0]][Nbrs[k][1]]++;
				}
				i++;
			}
		}
	}

	public AWTLandmineModel (int NumRows, int NumCols, int NumMines) {
		this.NumRows = NumRows;
		this.NumCols = NumCols;
		this.NumMines = NumMines;
		this.NeighbourMineCount = new int[NumRows][NumCols]; // initialised to zeros by java.
		this.MineLocation = new boolean[NumRows][NumCols]; // initialised to false by java.
		this.LocationRevealed = new boolean[NumRows][NumCols]; // initialised to falsed by java.
		this.LocationRevealed = new boolean[NumRows][NumCols]; // initialised to false by java.
		// The following setion creates landmines and fill in minecounts.
		int i = 0;
		while( i<NumMines ){
			int row = (int) Math.floor(Math.random()*NumRows);
			int col = (int) Math.floor(Math.random()*NumCols);
			if(MineLocation[row][col]==true){
				continue;
			}else{
				MineLocation[row][col] = true; // Generate bombs randomly on minefield
				
				// Add 1 to all the Neibouring location bombs.
				int[][] Nbrs = this.getNeighbourIndices(row,col);
				for(int k = 0; k<Nbrs.length ; k++){
					NeighbourMineCount[Nbrs[k][0]][Nbrs[k][1]]++;
				}
				i++;
			}
		}
	}

	/* Event Handlers*/
	public void RightClick(int i, int j){
		if(!LocationMarked[i][j]){
			LocationMarked[i][j] = !LocationMarked[i][j]; // if the location is not marked, mark it and NumOfMineLeft-1
			NumOfMineLeft--;
		}else{
			LocationMarked[i][j] = !LocationMarked[i][j]; // if the location is marked, unmark it and NumOfMineLeft+1.
			NumOfMineLeft++;
		}
	}
	
	public void LeftClick(int i, int j){
		if(!LocationMarked[i][j] && !LocationRevealed[i][j]){
			LocationRevealed[i][j] = true;
			//testing
			//System.out.println("("+i+","+j+") LeftClicked.");
			if(MineLocation[i][j]==true){
				//lost
			}else if(NeighbourMineCount[i][j]>0){
				// LocationRevealed[i][j] is now true, Gui should be able to display it.
			}else{
				// NeighbourMineCounnt[i][j]==0
				int[][] indices = getNeighbourIndices(i,j);
				for(int k=0; k<indices.length;k++){
					LeftClick(indices[k][0],indices[k][1]);
				}
			}
		}
		this.Win = this.CheckWin();
		this.Lose = this.CheckLose(i,j);
		// testing
		//System.out.println("Win = "+this.Win);
		//System.out.println("Lose = "+this.Lose);
	}

	public boolean CheckWin(){
		// WinningCondition : reveal all the locations without stepping on bomb.
		boolean win = true;
		for(int i=0;i<this.NumRows;i++){
			for(int j=0;j<this.NumCols; j++){
				if(!MineLocation[i][j] && !LocationRevealed[i][j]){
					// Any Location that is not revealed and not a bomb, meaning the player hasn't won yet.
					win = false;
				}
			}
		}
		return win;
	}
	
	public boolean CheckLose(int i, int j){
		// We only check lose when left clicked and only need to check one location.
		// LosingCondition : Stepping on bomb.
		if(this.MineLocation[i][j]){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void Restart(){
		this.NeighbourMineCount = new int[NumRows][NumCols]; // initialised to zeros by java.
		this.MineLocation = new boolean[NumRows][NumCols]; // initialised to false by java.
		this.LocationRevealed = new boolean[NumRows][NumCols]; // initialised to false by java.
		this.LocationRevealed = new boolean[NumRows][NumCols]; // initialised to false by java.
		this.LocationMarked = new boolean[NumRows][NumCols]; // initialised to false by java;
		this.Win = false;
		this.Lose = false;
		// The following setion creates landmines and fill in minecounts.
		int i = 0;
		while( i<NumMines ){
			int row = (int) Math.floor(Math.random()*NumRows);
			int col = (int) Math.floor(Math.random()*NumCols);
			if(MineLocation[row][col]==true){
				continue;
			}else{
				MineLocation[row][col] = true; // Generate bombs randomly on minefield
				
				// Add 1 to all the Neibouring location bombs.
				int[][] Nbrs = this.getNeighbourIndices(row,col);
				for(int k = 0; k<Nbrs.length ; k++){
					NeighbourMineCount[Nbrs[k][0]][Nbrs[k][1]]++;
				}
				i++;
			}
		}
	}
	
	/* Getters functions*/
	public int[][] getNeighbourIndices(int i, int j){
		// This method is for internal use, therefor all indices
		// should start from 0 end with N-1.
		// Output is of the form {(r1,c1),
		//						  (r2,c2),
		//						  (r3,c3)}	
		int[][] indices;
		if(i==0 && j==0){
			indices = new int[][]{{i,j+1},{i+1,j+1},{i+1,j}};
		}else if(i==0 && j==(NumCols-1)){
			indices = new int[][]{{0,j-1},{1,j-1},{1,j}};
		}else if(i==(NumRows-1) && j==(NumCols-1)){
			indices = new int[][]{{i-1,j},{i-1,j-1},{i,j-1}};
		}else if(i==(NumRows-1) && j==0){
			indices = new int[][]{{i-1,0},{i-1,1},{i,1}};
		}else if (i==0){
			indices = new int[][]{{i,j-1},{i+1,j-1},{i+1,j},{i+1,j+1},{i,j+1}};
		}else if (i==(NumRows-1)){
			indices = new int[][]{{i,j-1},{i-1,j-1},{i-1,j},{i-1,j+1},{i,j+1}};
		}else if (j==0){
			indices = new int[][]{{i-1,j},{i-1,j+1},{i,j+1},{i+1,j+1},{i+1,j}};
		}else if (j==(NumCols-1)){
			indices = new int[][]{{i-1,j},{i-1,j-1},{i,j-1},{i+1,j-1},{i+1,j}};
		}else{
			indices = new int[][]{{i-1,j-1},{i-1,j},{i-1,j+1},{i,j-1},{i,j+1},{i+1,j-1},{i+1,j},{i+1,j+1}};
		}
		return indices;
	}
	
	public int[][] getNeighbourMineCount(){
		return this.NeighbourMineCount;
	}
	
	public boolean[][] getMineLocation(){
		return this.MineLocation;
	}
	
	public int getMineCount(int i, int j){
		return this.NeighbourMineCount[i][j];
	}
	
	public boolean getMine(int i, int j){
		return this.MineLocation[i][j];
	}

	public int getNumRows(){
		return this.NumRows;
	}
	
	public int getNumCols(){
		return this.NumCols;
	}
	
	public int getNumMines(){
		return this.NumMines;
	}
	
	/* ToString functions*/
	public String MatrixToString(int[][] input){
		String temp = "";
		for(int i=0;i<input.length;i++){
			for(int j=0;j<input[0].length;j++){
				temp = temp+input[i][j]+" ";
			}
			temp = temp+"\n";
		}
		temp = temp+"\n";
		return temp;
	}
	
	public String MatrixToString(boolean[][] input){
		String temp = "";
		for(int i=0;i<input.length;i++){
			for(int j=0;j<input[0].length;j++){
				temp = temp+input[i][j]+" ";
			}
			temp = temp+"\n";
		}
		temp = temp+"\n";
		return temp;
	}
 
	
	// The entry main() method
	public static void main(String[] args) {
		
	}
	
}