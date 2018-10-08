package main;

import java.util.*;

/**
 * Used to search a graph with A* search from start node "S" to end node "G" or "Z".
 * First take an adjacency matrix, row title map and heuristic map and
 * convert it to an adjacency list with convertMatrixToList.
 * Then use findGoal to set the node values to the solution and get the output of the A* priority queue.
 * Then use getShortestPath to get the shortest path found.
 */
public class SearchAStar {
    // each node in an adjacency list has a list of its neighbors and their distance
    private HashMap<String, Node> adjacencyList;
    // ranks by Integer value and breaks ties by ranking the String key alphabetically
    private PriorityQueue<Map.Entry<String, Integer>> solution =
            new PriorityQueue<>(new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                    int ret = entry1.getValue() - entry2.getValue();
                    if (ret == 0) {
                        return entry1.getKey().compareToIgnoreCase(entry2.getKey());
                    } else {
                        return ret;
                    }
                }
            });
    // stores which goal was found first
    private String goalNodeFoundName;

    // Converts the given adjacency matrix to an adjacency list of Nodes.
    public void convertMatrixToList(int[][] adjacencyTable, HashMap<Integer, String> rowToName, HashMap<String, Integer> nameToHeuristic) {
        adjacencyList = new HashMap<>();

        // For each row (starting node), create a node and populate the node's neighbors list with
        // its neighbors as a map entry from neighbor's name to distance from this node to the neighbor.
        // Add this node to the adjacency list mapped from name to node.
        for (int i = 0; i < rowToName.size(); i++) {
            String currentNodeName = rowToName.get(i);
            Node temp = new Node();
            temp.name = currentNodeName;
            temp.heuristic = nameToHeuristic.get(currentNodeName);
            temp.distanceFromStart = Integer.MAX_VALUE;

            for (int j = 0; j < rowToName.size(); j++) {
                int distance = adjacencyTable[i][j];
                if (distance > 0) {
                    temp.neighborToDistance.add(new AbstractMap.SimpleEntry<>(rowToName.get(j), distance));
                }
            }
            adjacencyList.put(currentNodeName, temp);
        }
    }

    // Finds the goal node through A* search of the adjacency list.
    // Returns formatted contents of the solution priority queue after expanding each node.
    public String findGoal() {
        String message = "";
        // initialize start node
        String currentName = "S";
        Node currentNode = adjacencyList.get(currentName);
        currentNode.distanceFromStart = 0;

        while (!isGoal(currentName)) {
            // get the node object of the next node of the solution queue
            currentNode = adjacencyList.get(currentName);

            // add current node's neighbors to the solution queue
            for (Map.Entry<String, Integer> neighbor : currentNode.neighborToDistance) {
                String neighborName = neighbor.getKey();
                Node neighborNode = adjacencyList.get(neighborName);
                int newDistanceFromStart = currentNode.distanceFromStart + neighbor.getValue();

                // if the old distance is greater than the new distance, update distance and estimate
                if (neighborNode.distanceFromStart > newDistanceFromStart) {
                    // if old distance from start is not max value and the new is less than the old,
                    // then it is updating a node that is already in the solution queue
                    // remove the soon to be outdated node
                    if (neighborNode.distanceFromStart != Integer.MAX_VALUE) {
                        for (Iterator<Map.Entry<String, Integer>> iterator = solution.iterator(); iterator.hasNext(); ) {
                            Map.Entry sol = iterator.next();
                            if (sol.getKey().equals(neighborName)) {
                                iterator.remove();
                            }
                        }
                    }
                    // record the actual distance from start
                    neighborNode.distanceFromStart = currentNode.distanceFromStart + neighbor.getValue();
                    // record the estimate of the distance from start to goal node
                    int neighborEstimatedDistance =
                            neighborNode.distanceFromStart + neighborNode.heuristic;
                    // record who the distance came from
                    neighborNode.via = currentName;
                    // give the solution priority queue the name and new estimated distance
                    solution.offer(new AbstractMap.SimpleEntry<String, Integer>(
                            neighborName,
                            neighborEstimatedDistance));
                }
            }
            // add a line of the solution contents table to method output
            message += (currentName + " = " + toStringPriorityQueue(solution) + "\n");
            // get next closest node by polling queue
            currentName = solution.poll().getKey();
        }
        // return which found goal
        goalNodeFoundName = currentName;
        message += "Goal found: " + goalNodeFoundName + "\n";
        return message;
    }

    private boolean isGoal(String nextNode) {
        return (nextNode.equals("G") || nextNode.equals("Z"));
    }

    private boolean isStart(String nextNode) {
        return (nextNode.equals("S"));
    }

    // Finds the shortest path by going backwards from goal node through via nodes.
    // Returns names of nodes of the route from start to goal.
    public String getShortestPath() {
        String message = goalNodeFoundName;
        Node currentNode = adjacencyList.get(goalNodeFoundName);
        String nextNode = "";
        while (!isStart(nextNode)) {
            nextNode = currentNode.via;
            message = nextNode + " " + message;
            currentNode = adjacencyList.get(nextNode);
        }
        return message;
    }

    // Copies priority queue to tempToString to allow output, then outputs formatted list of contents of the queue.
    private String toStringPriorityQueue(PriorityQueue<Map.Entry<String, Integer>> solution) {
        String contentsOfQueue = "";
        PriorityQueue<Map.Entry<String, Integer>> tempToString = new PriorityQueue<>(solution);
        while (!tempToString.isEmpty()) {
            // poll top entry of queue
            Map.Entry<String, Integer> temp = tempToString.poll();
            // get node with name of that entry
            Node tempNode = adjacencyList.get(temp.getKey());
            // add that node's toString to contentsOfQueue
            contentsOfQueue += tempNode.toString() + " ";
        }
        return contentsOfQueue;
    }

    class Node {
        private String name;
        private int distanceFromStart;
        private int heuristic;
        private String via;
        private ArrayList<Map.Entry<String, Integer>> neighborToDistance = new ArrayList<>();

        @Override
        public String toString() {
            return "|" + name +
                    ":" + (distanceFromStart + heuristic) +
                    ":" + via +
                    "|";
        }
    }
}
