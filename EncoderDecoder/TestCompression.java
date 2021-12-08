import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;
import java.nio.file.Files;

public class TestCompression {

   private static void testInput(String[] args) throws IOException, ClassNotFoundException {
   	Huffman myHuff=new Huffman();
   	
   	if (args.length <2) { System.out.println("Usage: TestCompression E/D inputfile outputfile\nUsage: TestCompression T testfile");
   	                        return;
   	}
   	switch (args[0]) {
   	   case "E": case "e": 
   		   myHuff.encode(args[1], args[2]);
           break;
   	   case "D": case "d":
   		   myHuff.decode(args[1], args[2]);
   	       break;
   	   case "T": case "t":
   		   Path path = Paths.get(args[1]);
   	       try (Stream<String> line = Files.lines(path)) {
   	             line.forEach(row -> {try {
					testInput(row.split(" "));
				} catch (Exception e) {
					System.out.println("Error in split");
				}
   	             });
   	       } catch (IOException e) {
   	           System.out.println("Cannot find file.");
   	       }
   	       System.out.println("Test file was completed.");
   	       break;
            
   	     default: System.out.println("Error: first argument must be E, D or T.");
   	     return;
    }
   }

public static void main(String[] args) throws IOException, ClassNotFoundException { 
  	 Huffman myHuff=new Huffman();
  	 
  	 if (args.length==0) {

  		 myHuff.encode("genes.txt", "genes.huf");
  		 myHuff.decode("genes.huf", "genesRecovered.txt");
  		 
  		 while (true) { 
           System.out.println("\nCommand Formats:");
           System.out.println("E <inputfile> <outputfile>");
           System.out.println("D <inputfile> <outputfile>");
           System.out.println("T <testfile_with_commands>");
           System.out.println("or type Q for quiting\n");
           System.out.print("Enter command > ");
           Scanner input = new Scanner(System.in);
           String command=input.nextLine();
           if (command.equals("Q")) break;
           testInput(command.split(" "));
  		 }
         System.out.println("Ended program.");
  		 
  	 }
  	 else
  		 testInput(args);
 	 
  }
}
