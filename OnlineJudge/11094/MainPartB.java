import java.io.*;
import java.util.*;
import java.util.stream.*;

class MainPartB {

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

			findBiggestContinent(map, x, y); // finding and updating savedGraphSize and savedDistance
			System.out.println(savedGraphSize + " " + savedDistance); // printing the output


			try{ String spaceLine = in.nextLine(); } // when we reach the space between the two maps in the same txt file

			catch(NoSuchElementException e){ break; } // when the files is finished
		}

		in.close();

		return;
	}
	

	static HashMap<String, Boolean> labels;
	static int savedGraphSize = 0;
	static int savedDistance = 0;

	static void findBiggestContinent(String[][] map, int x, int y) {

		//System.out.println(Arrays.deepToString(map));

		String landNotation = map[x][y]; // finding out what the notation of land is

		labels = new HashMap<>(); // creating a new hash map for regions of the current map
		savedGraphSize = 0;
		savedDistance = 0;

		DFS(map, x, y, labels, landNotation); // labeling all the rigons of the current continet where the king resides as visited

		for (int i=0; i < map.length; i++) {

			for (int j=0; j < map[i].length; j++) { // going through each region

				String key = Integer.toString(i) + " " + Integer.toString(j); // getting the key of the selected region for labels

				if ( (map[i][j].equals(landNotation)) && (labels.get(key) == null) ) { // checking if the selected region has not been visited in previous DFS calls

					//
					int newGraphSize = DFS(map, i, j, labels, landNotation);

					if (XnorthWest == -1 && YnorthWest == -1) {
						XnorthWest = i;
						YnorthWest = j;
					}
					//

					if ( newGraphSize > savedGraphSize ) {

						savedGraphSize = newGraphSize;
						savedDistance = BFS(map, XnorthWest, YnorthWest, landNotation);
					}

					else if ( newGraphSize == savedGraphSize ) {

						int newDistance = BFS(map, XnorthWest, YnorthWest, landNotation);

						if ( newDistance > savedDistance ) {

							savedGraphSize = newGraphSize;
							savedDistance = newDistance;
						}
					}
				}
			}
		}
	}


	static int XnorthWest = -1;
	static int YnorthWest = -1;

	static int DFS(String[][] map, int i, int j, HashMap<String, Boolean> labels, String landNotation) {

		XnorthWest = -1;
		YnorthWest = -1;

		int graphSize = 1;

		String key = Integer.toString(i) + " " + Integer.toString(j);
	  
	    labels.put(key, true); // updating the label

	    //
	    if ( (i-1 >= 0) && (map[i-1][j].equals(landNotation)) && (labels.get(Integer.toString(i-1) + " " + Integer.toString(j)) == null) ) {

	    	if (i-1 < XnorthWest)
	    		XnorthWest = i-1;

	    	graphSize = graphSize + DFS(map, i-1, j, labels, landNotation);
	    }
	    //

	    //
	    if ( (j+1 >= map[0].length) ) {

	    	if ( (map[i][0].equals(landNotation)) && (labels.get(Integer.toString(i) + " " + Integer.toString(0)) == null) ) {
	    		
	    		graphSize = graphSize + DFS(map, i, 0, labels, landNotation);
	    	}
	    }
	    else if ( (map[i][j+1].equals(landNotation)) && (labels.get(Integer.toString(i) + " " + Integer.toString(j+1)) == null) ) {

	    	graphSize = graphSize + DFS(map, i, j+1, labels, landNotation);
	    }
	    //

	    //
	    if ( (i+1 < map.length) && (map[i+1][j].equals(landNotation)) && (labels.get(Integer.toString(i+1) + " " + Integer.toString(j)) == null) ) {

	    	graphSize = graphSize + DFS(map, i+1, j, labels, landNotation);
	    }
	    //

	    //
	    if ( (j-1 < 0) ) {
	    	
	    	if ( (map[i][map[0].length-1].equals(landNotation)) && (labels.get(Integer.toString(i) + " " + Integer.toString(map[0].length-1)) == null) ) {

	    		if (map[0].length-1 < YnorthWest)
	    			YnorthWest = map[0].length-1;

	    		graphSize = graphSize + DFS(map, i, map[0].length-1, labels, landNotation);
	    	}
	    }
	    else if ( (map[i][j-1].equals(landNotation)) && (labels.get(Integer.toString(i) + " " + Integer.toString(j-1)) == null) ) {

	    	if (j-1 < YnorthWest)
	    		YnorthWest = j-1;

	    	graphSize = graphSize + DFS(map, i, j-1, labels, landNotation);
	    }
	    //

	    return graphSize;
	}



	static int BFS(String[][] map, int i, int j, String landNotation) {

		HashMap<String, Integer> tmpLabels = new HashMap<>();

		Queue<String> queue = new LinkedList<>();


		String key = Integer.toString(i) + " " + Integer.toString(j);

		int distance = 0;
		boolean distanceAlreadyIncremented = false;

		queue.add(key);
		tmpLabels.put(key, distance);

		while( !queue.isEmpty() ) {

			key = queue.remove();
			int currentKeySavedDistance = tmpLabels.get(key);

			int x = Integer.parseInt(key.split(" ")[0]);
			int y = Integer.parseInt(key.split(" ")[1]);

			//
			String newKey = Integer.toString(x-1) + " " + Integer.toString(y);
		    if ( (x-1 >= 0) && (map[x-1][y].equals(landNotation)) && (tmpLabels.get(newKey) == null) ) {

		    	queue.add( newKey );
		    	tmpLabels.put(newKey, currentKeySavedDistance + 1);
		    }
		    //

			//
			if ( (y+1 >= map[0].length) ) {

				newKey = Integer.toString(x) + " " + Integer.toString(0);
		    	if ( (map[x][0].equals(landNotation)) && (tmpLabels.get(newKey) == null) ) {
		    		
			    	queue.add( newKey );
			    	tmpLabels.put(newKey, currentKeySavedDistance + 1);
		    	}
		    }
		    else if ( (map[x][y+1].equals(landNotation)) && (tmpLabels.get(Integer.toString(x) + " " + Integer.toString(y+1)) == null) ) {

		    	newKey = Integer.toString(x) + " " + Integer.toString(y+1);
			    queue.add( newKey );
			    tmpLabels.put(newKey, currentKeySavedDistance + 1);
		    }
		    //

		    //
		    newKey = Integer.toString(x+1) + " " + Integer.toString(y);
		    if ( (x+1 < map.length) && (map[x+1][y].equals(landNotation)) && (tmpLabels.get(newKey) == null) ) {

			    queue.add( newKey );
			    tmpLabels.put(newKey, currentKeySavedDistance + 1);
		    }
		    //

		    //
		    if ( (y-1 < 0) ) {
		    	
		    	newKey = Integer.toString(x) + " " + Integer.toString(map[0].length-1);
		    	if ( (map[x][map[0].length-1].equals(landNotation)) && (tmpLabels.get(newKey) == null) ) {

				    queue.add( newKey );
				    tmpLabels.put(newKey, currentKeySavedDistance + 1);
		    	}
		    }
		    else if ( (map[x][y-1].equals(landNotation)) && (tmpLabels.get(Integer.toString(x) + " " + Integer.toString(y-1)) == null) ) {

		    	newKey = Integer.toString(x) + " " + Integer.toString(y-1);
				queue.add( newKey );
				tmpLabels.put(newKey, currentKeySavedDistance + 1);
		    }
		    //

		    distanceAlreadyIncremented = false;
		}

		return biggestValue(tmpLabels);
	}

	//***Helper methods**********************************************************************************************
	static int biggestValue(HashMap<String, Integer> hashMap) {
		int maxValue = 0;
		for(Integer value : hashMap.values()) {
		     if(value > maxValue) {
		         maxValue = value;
		     }
		}
		return maxValue;
	}
	//***************************************************************************************************************
	
	public static void main(String[] args) {
		read();
	}
}
