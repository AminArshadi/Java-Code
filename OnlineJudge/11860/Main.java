import java.io.*;
import java.util.*;
import java.util.stream.*;

class Main {

	static String read() {

		StringBuilder input = new StringBuilder();

		Scanner in = new Scanner(System.in);

		int numberOfDocuments = in.nextInt();

		int counterEND = 0;

		String line;

		while(in.hasNextLine()) {

			line = in.nextLine();

			if ( line.equals("END") ) {

				if (++counterEND == numberOfDocuments)
					break;
			}

			input.append(line);
			input.append(" ");
		}  

		in.close();

		String string = input.toString();

        StringBuilder result = new StringBuilder();
		Boolean wasSpace = false;

		for (int i=0; i < string.length(); i++) {

			Character ch = string.charAt(i);
					
			if ( Character.isLetter(ch) ) {

				result.append(ch);
				wasSpace = false;
			}

			else if ( !wasSpace ) {

				result.append(" ");
				wasSpace = true;
			}
		}

		return result.toString();
	}
	



	static void findSmallestRange(int i ,String[] currentListOfWords, HashSet<String> currentNoDuplicationListOfWords) {

		int finalCurrentListOfWordsLength = currentListOfWords.length;
		int finalCurrentNoDuplicationListOfWords = currentNoDuplicationListOfWords.size();

		int upper = -1;
		int lower = -1;

		if (finalCurrentNoDuplicationListOfWords == finalCurrentListOfWordsLength) {

			System.out.println("Document " + i + ": " + 1 + " " + finalCurrentNoDuplicationListOfWords);
			return;
		}


		HashMap<String, Integer> tmpElemsCheck = new HashMap<>();

		int newLower = 0;

		int counter = 0;

		for (int j=0; j < finalCurrentListOfWordsLength; j++) {

			tmpElemsCheck.put( currentListOfWords[j], j );

			counter++;

			if ( tmpElemsCheck.size() == finalCurrentNoDuplicationListOfWords) {

				newLower = Collections.min(tmpElemsCheck.values());

				// the first time we find a possible answer
				if (upper == -1 && lower == -1) {

					upper = j;
					lower = newLower;

					if (counter == finalCurrentNoDuplicationListOfWords)
						break;
				}

				// after the first time that we find a possible answer
				else if ( (j - newLower) < (upper - lower) ) {

					upper = j;
					lower = newLower;

					if (counter == finalCurrentNoDuplicationListOfWords)
						break;
				}

				tmpElemsCheck.clear();
				j = newLower;
				counter = 0;

			}
		}

		System.out.println("Document " + i + ": " + (lower+1) + " " + (upper+1));
		return;
	}

	// Helper Methods ******************************************************************************
	static String[] subArray(String[] array, int beg, int end) {
		return Arrays.copyOfRange(array, beg, end + 1);
	}
	//**********************************************************************************************
	
	public static void main(String[] args) {

		String input = read();

		String[] splittedInput = input.split("END");

		String[] splittedDocuments;
		String[] newSplittedDocuments;

		for (int i=0; i < splittedInput.length; i++) {

			splittedDocuments = splittedInput[i].split(" ");

			/// making a hashSet, putting the elemets of splittedDocuments that are equal to "" at the end of newSplittedDocuments,
			/// and counting the number of "" and saving it in nullCounter.
			int splittedDocumentsLength = splittedDocuments.length;
			newSplittedDocuments = new String[splittedDocumentsLength];

			HashSet<String> hashSet = new HashSet<>();

			int counter = 0;
			int nullCounter = 0;

			for (int j=0; j < splittedDocumentsLength; j++) {

				if ( !splittedDocuments[j].equals("") ) {

					hashSet.add( splittedDocuments[j] );

					newSplittedDocuments[counter] = splittedDocuments[j];
					counter++;
				}

				else
					nullCounter++;
			}

			if (hashSet.size() == 0) {
				continue;
			}
			///

			findSmallestRange(i+1, subArray(newSplittedDocuments, 0, newSplittedDocuments.length-nullCounter-1), hashSet);
		}
	}
}
