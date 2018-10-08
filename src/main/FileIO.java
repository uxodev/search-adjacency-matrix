package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Requires an *_am.txt file that describes the matrix and a *_h.txt file that has the heuristics for each node.
 */
public class FileIO {
    private File inputFileAM;
    private File inputFileH;
    private File outputFile;
    private Scanner inputAM = null;
    private Scanner inputH = null;
    private PrintWriter output = null;

    // asks for only the *_am.txt file from the data folder, gets the *_h.txt file from filename
    public File chooseFile() {
        String dataFolder = System.getProperty("user.dir") + "\\data\\";
        JFileChooser jFileChooser = new JFileChooser(dataFolder);
        jFileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
        jFileChooser.setDialogTitle("Select *_am.txt file");

        String selectedFilePath = null;
        // open file chooser
        int returnValue = jFileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            inputFileAM = jFileChooser.getSelectedFile();
            selectedFilePath = inputFileAM.getAbsolutePath();
            // find _h file
            inputFileH = new File(selectedFilePath.substring
                    (0, selectedFilePath.length() - 7)
                    + "_h.txt");
            // set output file
            outputFile = new File(selectedFilePath.substring
                    (0, selectedFilePath.length() - 7)
                    + "_result.txt");
        } else {
            System.out.println("\nNo file selected.\n");
            System.exit(0);
        }
        try {
            inputAM = new Scanner(inputFileAM);
            inputH = new Scanner(inputFileH);
            output = new PrintWriter(outputFile);
        } catch (FileNotFoundException e) {
            System.out.println("\nError: Could not find a file.\n");
            e.printStackTrace();
            System.exit(0);
        }
        return inputFileAM;
    }

    // write string to both output file and console
    public void write(String writeThisToFile) {
        System.out.print(writeThisToFile);
        output.write(writeThisToFile);
    }

    public void close() {
        output.close();
    }

    // parses files into an adjacency matrix
    public int[][] createAdjacencyMatrix(HashMap<Integer, String> rowToName, HashMap<String, Integer> nameToHeuristic) {
        int[][] adjacencyMatrix;
        String currentLine;
        String[] splitLine;
        boolean hasStart = false;
        boolean hasGoal = false;

        // populate rowToName from first row of _am file
        if (inputAM != null) {
            currentLine = inputAM.nextLine();
            // split by any whitespace
            splitLine = currentLine.trim().split("\\s+");
            for (int i = 1; i < splitLine.length; i++) {
                rowToName.put(i - 1, splitLine[i]);
                // check file has a start and end node
                if (splitLine[i].equals("S")) {
                    hasStart = true;
                }
                if (splitLine[i].equals("G") || splitLine[i].equals("Z")) {
                    hasGoal = true;
                }
            }
        }
        // report if file does not have a start and end node
        if (!hasStart) {
            System.out.println("Error: File does not indicate a start node");
        }
        if (!hasGoal) {
            System.out.println("Error: File does not indicate a goal node");
        }

        // populate adjacency matrix with remaining rows of _am file
        adjacencyMatrix = new int[rowToName.size()][rowToName.size()];
        if (inputAM != null) {
            int i = 0;
            while (inputAM.hasNextLine()) {
                currentLine = inputAM.nextLine();
                // split by any whitespace
                splitLine = currentLine.trim().split("\\s+");
                for (int j = 1; j < splitLine.length; j++) {
                    adjacencyMatrix[i][j - 1] = Integer.parseInt(splitLine[j]);
                }
                i++;
            }
        }

        // populate nameToHeuristic with _h file
        if (inputH != null) {
            while (inputH.hasNextLine()) {
                currentLine = inputH.nextLine();
                // split by any whitespace
                splitLine = currentLine.trim().split("\\s+");
                nameToHeuristic.put(splitLine[0], Integer.parseInt(splitLine[1]));
            }
        }
        return adjacencyMatrix;
    }
}
