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
        // preprocessing
        // gets box information out of the current state
        // Arraylist is used as add and get operations are constant time
        ArrayList<Integer> boxesLocRow = new ArrayList<Integer>();
        ArrayList<Integer> boxesLocCol = new ArrayList<Integer>();
        ArrayList<Character> boxes = new ArrayList<Character>();
        ArrayList<Color> boxesColor = new ArrayList<Color>();

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

        // box and goal calculations
        // pairwise Manhattan distance between boxes and their corresponding goals
        int[][]pdistBoxGoals = new int[this.goalsBoxes.size()][boxes.size()];
        for (int row = 0; row < pdistBoxGoals.length; row++) {
            Arrays.fill(pdistBoxGoals[row], Integer.MAX_VALUE);
            int goalRow = this.goalBoxLocRow.get(row);
            int goalCol = this.goalBoxLocCol.get(row);
            char goal = this.goalsBoxes.get(row);
            for(int col = 0; col < pdistBoxGoals[row].length; col++) {
                char box = boxes.get(col);
                int boxRow = boxesLocRow.get(col);
                int boxCol = boxesLocCol.get(col);
                // characters match and we calculate manhattan distance
                if (goal == box) {
                    int dst = Math.abs(goalRow - boxRow) + Math.abs(goalCol - boxCol);
                    // minimal distance for each goal
                    pdistBoxGoals[row][col] = dst;
                }
            }
        }

        // min distance from each goal
        int[] dstBoxGoals = new int[this.goalsBoxes.size()];
        for (int row = 0; row < pdistBoxGoals.length; row++) {
            int minDst = pdistBoxGoals[row][0];
            for (int col = 1; col < pdistBoxGoals[row].length; col++) {
                int dst = pdistBoxGoals[row][col];
                if(minDst > dst) {
                    minDst = dst;
                }
            }
            dstBoxGoals[row] = minDst;
        }

        // Agent and goal calculations
        // Manhattan distance between agents and their corresponding goals
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

        // Agent and box calculations
        // pairwise manhattan distance between agents and the boxes.
        int[][] pdistBoxAgents = new int[boxes.size()][s.agentRows.length];
        for (int row = 0; row < pdistBoxAgents.length; row++) {
            Arrays.fill(pdistBoxAgents[row], Integer.MAX_VALUE);
            Color boxColor = boxesColor.get(row);
            int boxRow = boxesLocRow.get(row);
            int boxCol = boxesLocCol.get(row);
            for (int col = 0; col < pdistBoxAgents[row].length; col++) {
                int agentRow = s.agentRows[col];
                int agentCol = s.agentCols[col];
                Color agentColor = s.agentColors[col];
                if(agentColor == boxColor) {
                    int dst = Math.abs(boxRow - agentRow) + Math.abs(boxCol - agentCol);
                    pdistBoxAgents[row][col] = dst;
                }
            }
        }

        int[] dstAgentBoxGoal = new int[this.goalsBoxes.size()];
        Arrays.fill(dstAgentBoxGoal, Integer.MAX_VALUE);
        // fills with 0's if box in place
        for (int i = 0; i < dstAgentBoxGoal.length; ++i) {
            if(dstBoxGoals[i] == 0) {
                dstAgentBoxGoal[i] = 0;
            }
        }

        // calculates cumulative distance between box and agent and box and goal
        for (int i = 0; i < pdistBoxGoals.length; i++) { //this.goalsBoxes.size()
            if(dstAgentBoxGoal[i] == 0) {
                continue;
            }
            for (int j = 0; j < pdistBoxGoals[i].length; j++) { // boxes.size()
                for (int k = 0; k < pdistBoxAgents[j].length; k++){ // s.agentRows.length
                    int dstBG = pdistBoxGoals[i][j];
                    int dstBA = pdistBoxAgents[j][k];
                    if(dstBG != Integer.MAX_VALUE && dstBA != Integer.MAX_VALUE) {
                        int dst = dstBG + dstBA;
                        if(dstAgentBoxGoal[i] > dst) {
                            dstAgentBoxGoal[i] = dst;
                        }
                    }
                }
            }
        }

        // sums up the full cost
        int cost = 0;
        for (int i = 0; i < dstAgentBoxGoal.length; i++) {
            cost += dstAgentBoxGoal[i];
        }

        for (int i = 0; i < dstBoxGoals.length; i++) {
            cost += dstBoxGoals[i];
        }

        for (int i = 0; i < dstAgentGoals.length; i++) {
            cost += dstAgentGoals[i];
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
