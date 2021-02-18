package searchclient;

import java.util.*;

public abstract class Heuristic
        implements Comparator<State>
{
    private final ArrayList<Integer> goalBoxLocRow = new ArrayList<Integer>();
    private final ArrayList<Integer> goalBoxLocCol = new ArrayList<Integer>();
    private final ArrayList<Character> goalsBoxes = new ArrayList<Character>();

    private final ArrayList<Integer> goalAgentLocRow = new ArrayList<Integer>();
    private final ArrayList<Integer> goalAgentLocCol = new ArrayList<Integer>();
    private final ArrayList<Integer> goalsAgents = new ArrayList<Integer>();


    public Heuristic(State initialState)
    {
        // retrieves goal locations and characters that are assigned to goals
        // 1...n-1 as first and last elements in rows and columns are walls
        for (int row = 1; row < initialState.goals.length - 1; row++)
        {
            for (int col = 1; col < initialState.goals[row].length - 1; col++)
            {
                char goal = initialState.goals[row][col];


                // determines if goal is a box
                if (goal >= 'A' && goal <= 'Z')
                {
                    this.goalsBoxes.add(goal);
                    this.goalBoxLocRow.add(row);
                    this.goalBoxLocCol.add(col);
                }
                // determines if the goal is agent
                if (goal >= '0' && goal <= '9')
                {
                    this.goalsAgents.add(Character.getNumericValue(goal));
                    this.goalAgentLocRow.add(row);
                    this.goalAgentLocCol.add(col);
                }
            }
        }
    }


    public int h(State s)
    {
        // gets box information out of the current state
        ArrayList<Integer> boxesLocRow = new ArrayList<Integer>();
        ArrayList<Integer> boxesLocCol = new ArrayList<Integer>();
        ArrayList<Character> boxes = new ArrayList<Character>();
        ArrayList<Color> boxesColor = new ArrayList<Color>();
        // Arraylist is used as add and get operations are constant time
        // retrieves box color location and character
        // 1...n-1 as first and last elements in rows and columns are walls
        for (int row = 1; row < s.boxes.length - 1; row++)
        {
            for (int col = 1; col < s.boxes[row].length - 1; col++)
            {
                char character = s.boxes[row][col];
                // determines the boxes location, character and color.
                if (character >= 'A' && character <= 'Z')
                {
                    boxes.add(character);
                    boxesLocRow.add(row);
                    boxesLocCol.add(col);
                    boxesColor.add(s.boxColors[(int)character - 65]);
                }
            }
        }

        // Manhattan distance between boxes and their corresponding goals
        // max value for comparison as there can be mutiple boxes with the same character

        
        int[] dstBoxGoals = new int[this.goalsBoxes.size()];
        Arrays.fill(dstBoxGoals, Integer.MAX_VALUE);

        for (int i = 0; i < dstBoxGoals.length; i++) {
            int goalRow = this.goalBoxLocRow.get(i);
            int goalCol = this.goalBoxLocCol.get(i);
            char goal = this.goalsBoxes.get(i);
            for (int j = 0; j < boxes.size(); j++) {
                char box = boxes.get(j);
                int boxRow = boxesLocRow.get(j);
                int boxCol = boxesLocCol.get(j);
                // characters match and we calculate manhattan distance
                if (goal == box) {
                    int dst = Math.abs(goalRow - boxRow) + Math.abs(goalCol - boxCol);
                    // minimal distance for each goal
                    if (dstBoxGoals[i] > dst) {
                        dstBoxGoals[i] = dst;
                    }
                }
            }
        }

        // Manhattan distance between agents and their corresponding goals
        // max value for comparison as there can be mutiple boxes with the same character
        int[] dstAgentGoals = new int[this.goalsAgents.size()];
        Arrays.fill(dstAgentGoals, Integer.MAX_VALUE);
        for (int i = 0; i < dstAgentGoals.length; i++) {
            int agent = goalsAgents.get(i);
            int goalRow = goalAgentLocRow.get(i);
            int goalCol = goalAgentLocCol.get(i);
            int agentRow = s.agentRows[agent];
            int agentCol = s.agentCols[agent];
            int dst = Math.abs(goalRow - agentRow) + Math.abs(goalCol - agentCol);
            // minimal distance for each goal
            if (dstAgentGoals[i] > dst) {
                dstAgentGoals[i] = dst;
            }
        }

        // manhattan distance between agents and the boxes.
        int[] dstBoxAgents = new int[this.goalsBoxes.size()];
        Arrays.fill(dstBoxAgents, Integer.MAX_VALUE);
        // assigns value 0 to the boxes that are already in goals
        for (int i = 0; i < dstBoxAgents.length; i++){
            if(dstBoxGoals[i] == 0) {
                dstBoxAgents[i] = dstBoxGoals[i];
            }
        }

        for (int i = 0; i < dstBoxAgents.length; i++) {
            char boxGoal = this.goalsBoxes.get(i);
            for (int j = 0; j < boxes.size(); j++) {
                char box = boxes.get(j);
                // boxgoal and current box is the same
                if(boxGoal == box) {
                    int boxRow = boxesLocRow.get(j);
                    int boxCol = boxesLocCol.get(j);
                    Color boxColor = boxesColor.get(j);
                    // checks each agents location with the box location if colors matches
                    for (int k = 0; k < s.agentRows.length; k++) {
                        int agentRow = s.agentRows[k];
                        int agentCol = s.agentCols[k];
                        Color agentColor = s.agentColors[k];
                        // colors matches
                        if(agentColor == boxColor) {
                            int dst = Math.abs(boxRow - agentRow) + Math.abs(boxCol - agentCol);
                            if (dstBoxAgents[i] > dst) {
                                dstBoxAgents[i] = dst;
                            }
                        }
                    }
                }
            }
        }
        // sums up the full cost
        int cost = 0;
        for (int i = 0; i < dstBoxGoals.length; i++) {
            cost += dstBoxGoals[i];
        }

        for (int i = 0; i < dstAgentGoals.length; i++) {
            cost += dstAgentGoals[i];
        }

        for (int i = 0; i < dstBoxAgents.length; i++) {
            cost += dstBoxAgents[i];
        }
        return cost;


//        int goal_counter = 0;
//        // 1...n-1 as first and last elements in rows and columns are walls
//        for (int row = 1; row < s.goals.length - 1; row++)
//        {
//            for (int col = 1; col < s.goals[row].length - 1; col++)
//            {
//                char goal = s.goals[row][col];
//                // determines if goal is a box and if the boxes are the same
//                if (goal >= 'A' && goal <= 'Z' && s.boxes[row][col] != goal)
//                {
//                    goal_counter++;
//                }
//                // determines if the goal is agent and if the agents are the same
//                if (goal >= '0' && goal <= '9' && s.agentRows[goal - '0'] != row && s.agentCols[goal - '0'] != col)
//                {
//                    goal_counter++;
//                }
//            }
//        }
//        return goal_counter;
    }

    public abstract int f(State s);

    @Override
    public int compare(State s1, State s2)
    {
        return this.f(s1) - this.f(s2);
    }
}

class HeuristicAStar
        extends Heuristic
{
    public HeuristicAStar(State initialState)
    {
        super(initialState);
    }

    @Override
    public int f(State s)
    {
        return s.g() + this.h(s);
    }

    @Override
    public String toString()
    {
        return "A* evaluation";
    }
}

class HeuristicWeightedAStar
        extends Heuristic
{
    private int w;

    public HeuristicWeightedAStar(State initialState, int w)
    {
        super(initialState);
        this.w = w;
    }

    @Override
    public int f(State s)
    {
        return s.g() + this.w * this.h(s);
    }

    @Override
    public String toString()
    {
        return String.format("WA*(%d) evaluation", this.w);
    }
}

class HeuristicGreedy
        extends Heuristic
{
    public HeuristicGreedy(State initialState)
    {
        super(initialState);
    }

    @Override
    public int f(State s)
    {
        return this.h(s);
    }

    @Override
    public String toString()
    {
        return "greedy evaluation";
    }
}
