package main;

import java.io.File;
import java.util.HashMap;

/**
 * Implements A* search on a graph, given as an adjacency matrix,
 * from start node 'S' to end node 'G' or 'Z'.
 * Outputs a list of the order in which the nodes are expanded, showing
 * the contents of the priority queue when each node is expanded, and the list
 * of nodes that form the shortest path from the start to the nearest goal.
 */
public class Main {
    public static void main(String[] args) {
        FileIO fileIO = new FileIO();
        System.out.println("Select *_am.txt file with node data:");
        File fileName = fileIO.chooseFile();
        fileIO.write("File used: " + fileName.getName() + "\n");

        // get the name of a row of the adjacency matrix
        HashMap<Integer, String> rowToName = new HashMap<>();
        // get the heuristic of a node by name
        HashMap<String, Integer> nameToHeuristic = new HashMap<>();
        // create the adjacency matrix from the files given
        int[][] adjacencyMatrix = fileIO.createAdjacencyMatrix(rowToName, nameToHeuristic);
        // convert the adjacency matrix into an adjacency list
        SearchAStar searchAStar = new SearchAStar();
        searchAStar.convertMatrixToList(adjacencyMatrix, rowToName, nameToHeuristic);

        fileIO.write("Format:\n");
        fileIO.write("source node = |queue node:edges from start:via node|\n");
        fileIO.write("Priority queue after expanding node:\n");
        fileIO.write(searchAStar.findGoal());
        fileIO.write("Route to goal:\n");
        fileIO.write(searchAStar.getShortestPath());

        fileIO.close();
    }
}
