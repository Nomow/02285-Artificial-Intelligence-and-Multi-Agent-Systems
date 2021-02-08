package searchclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GraphSearch {

    public static Action[][] search(State initialState, Frontier frontier)
    {
        boolean outputFixedSolution = false;


        if (outputFixedSolution) {
            //Part 1:
            return new Action[][] {
                    {Action.MoveE},
                    {Action.MoveE},
                    {Action.MoveE},
                    {Action.MoveE},
                    {Action.MoveE},
                    {Action.MoveE},
                    {Action.MoveE},
                    {Action.MoveS},
                    {Action.MoveS},
                    {Action.MoveE},
                    {Action.MoveE},
                    {Action.MoveE},
                    {Action.MoveS},
                    {Action.MoveS},


            };
        } else {
            //Part 2:
            //Now try to implement the Graph-Search algorithm from R&N figure 3.7
            //In the case of "failure to find a solution" you should return null.
            //Some useful methods on the state class which you will need to use are:
            //state.isGoalState() - Returns true if the state is a goal state.
            //state.extractPlan() - Returns the Array of actions used to reach this state.
            //state.getExpandedStates() - Returns an ArrayList<State> containing the states reachable from the current state.
            //You should also take a look at Frontier.java to see which methods the Frontier interface exposes
            //
            //printSearchStates(explored, frontier): As you can see below, the code will print out status
            //(#explored states, size of the frontier, #generated states, total time used) for every 10000th node generated.
            //You might also find it helpful to print out these stats when a solution has been found, so you can keep
            //track of the exact total number of states generated.



            int iterations = 0;

            frontier.add(initialState);
            HashSet<State> explored = new HashSet<>();


            while (true) {
                //Print a status message every 10000 iteration
                if (++iterations % 10000 == 0) {
                    printSearchStatus(explored, frontier);
                }


                // BFS search problem

                // intial state is goal state to the problem
                if(initialState.isGoalState()) {
                    printSearchStatus(explored, frontier);
                    return initialState.extractPlan();
                }
                explored.add(initialState);

                while(!frontier.isEmpty()) {
                    State node = frontier.pop();
                    // acquires possible states of the node that was popped from frontier
                    // and determines if child is the goal state and if not then adds them to frontier
                    // and marks them as explored and repeats the process
                    for (State child : node.getExpandedStates()) {
                        if(child.isGoalState()){
                            printSearchStatus(explored, frontier);
                            return child.extractPlan();
                        }
                        if(!explored.contains(child)) {
                            explored.add(child);
                            frontier.add(child);
                        }
                    }
                }
                // Graph has been explored completely and goal state was not found
                return null;
            }
        }
    }

    private static long startTime = System.nanoTime();

    private static void printSearchStatus(HashSet<State> explored, Frontier frontier)
    {
        String statusTemplate = "#Expanded: %,8d, #Frontier: %,8d, #Generated: %,8d, Time: %3.3f s\n%s\n";
        double elapsedTime = (System.nanoTime() - startTime) / 1_000_000_000d;
        System.err.format(statusTemplate, explored.size(), frontier.size(), explored.size() + frontier.size(),
                          elapsedTime, Memory.stringRep());
    }
}
