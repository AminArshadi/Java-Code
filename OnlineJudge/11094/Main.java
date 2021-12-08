import java.io.*;
import java.util.*;
import java.util.stream.*;

class Main {

	static void read() {

		Scanner in = new Scanner(System.in);

		while(in.hasNextLine()) {

			String firstLine = in.nextLine(); // # #

			int numRows = Integer.parseInt(firstLine.split(" ")[0]);
			int numColumns = Integer.parseInt(firstLine.split(" ")[1]);

			String[][] map = new String[numRows][numColumns];

			String line;

			for (int i=0; i < numRows; i++) {

				line = in.nextLine();

				String[] lineElems = line.split("");

				for (int j=0; j < numColumns; j++) {

					map[i][j] = lineElems[j]; // creating the map
				}
			}

			String lastLine = in.nextLine(); // # #

			int x = Integer.parseInt(lastLine.split(" ")[0]);
			int y = Integer.parseInt(lastLine.split(" ")[1]);


			System.out.println( findBiggestContinent(map, x, y) ); // printing the output


			try{ String spaceLine = in.nextLine(); } // when we reach the space between the two maps in the same txt file

			catch(NoSuchElementException e){ break; } // when the files is finished
		}

		in.close();

		return;
	}
	

	static HashMap<String, Boolean> labels = new HashMap<>(); 

	static int findBiggestContinent(String[][] map, int x, int y) {

		String landNotation = map[x][y]; // finding out what the notation of land is

		labels = new HashMap<>(); // creating a new hash map for regions of the current map

		DFS(map, x, y, labels, landNotation); // labeling all the rigons of the current continet where the king resides as visited

		int result = 0;

		for (int i=0; i < map.length; i++) {

			for (int j=0; j < map[i].length; j++) { // going through each region

				String key = Integer.toString(i) + " " + Integer.toString(j); // getting the key of the selected region for labels

				if ( (map[i][j].equals(landNotation)) && (labels.get(key) == null) ) { // checking if the selected region has not been visited in previous DFS calls

					//
					int graphSize = DFS(map, i, j, labels, landNotation);
					//

					if ( graphSize > result )
						result = graphSize;	
				}
			}
		}

		return result;
	}




	static int DFS(String[][] map, int i, int j, HashMap<String, Boolean> labels, String landNotation) {

		int graphSize = 1;

		String key = Integer.toString(i) + " " + Integer.toString(j);
	  
	    labels.put(key, true); // updating the label

	    // up
	    if ( (i-1 >= 0) && (map[i-1][j].equals(landNotation)) && (labels.get(Integer.toString(i-1) + " " + Integer.toString(j)) == null) ) {

	    	graphSize = graphSize + DFS(map, i-1, j, labels, landNotation);
	    }
	    //

	    // right
	    if ( (j+1 >= map[0].length) ) {

	    	if ( (map[i][0].equals(landNotation)) && (labels.get(Integer.toString(i) + " " + Integer.toString(0)) == null) ) {
	    		
	    		graphSize = graphSize + DFS(map, i, 0, labels, landNotation);
	    	}
	    }
	    else if ( (map[i][j+1].equals(landNotation)) && (labels.get(Integer.toString(i) + " " + Integer.toString(j+1)) == null) ) {

	    	graphSize = graphSize + DFS(map, i, j+1, labels, landNotation);
	    }
	    //

	    // down
	    if ( (i+1 < map.length) && (map[i+1][j].equals(landNotation)) && (labels.get(Integer.toString(i+1) + " " + Integer.toString(j)) == null) ) {

	    	graphSize = graphSize + DFS(map, i+1, j, labels, landNotation);
	    }
	    //

	    // left
	    if ( (j-1 < 0) ) {
	    	
	    	if ( (map[i][map[0].length-1].equals(landNotation)) && (labels.get(Integer.toString(i) + " " + Integer.toString(map[0].length-1)) == null) ) {

	    		graphSize = graphSize + DFS(map, i, map[0].length-1, labels, landNotation);
	    	}
	    }
	    else if ( (map[i][j-1].equals(landNotation)) && (labels.get(Integer.toString(i) + " " + Integer.toString(j-1)) == null) ) {

	    	graphSize = graphSize + DFS(map, i, j-1, labels, landNotation);
	    }
	    //

	    return graphSize;
	}




	
	public static void main(String[] args) {

		read();
	}
}
