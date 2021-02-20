package searchclient;

import java.util.*;

public abstract class Heuristic
        implements Comparator<State>
{
    static int b = 0;

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

        // min distance from each goal
        int[] minDstGoal = new int[this.goalsBoxes.size()];
        int[] usedBoxes = new int[boxes.size()];
        int[] boxIds = new int[boxes.size()];
        Color[] boxIdColor = new Color[boxes.size()];

        Arrays.fill(usedBoxes, 0);

        for (int row = 0; row < pdistBoxGoals.length; row++) {
            int minDst = Integer.MAX_VALUE;
            int minCol = 0;
            Color c = null;
            for (int col = 0; col < pdistBoxGoals[row].length; col++) {
                int dst = pdistBoxGoals[row][col];
                if(minDst > dst && usedBoxes[col] == 0) {
                    minDst = dst;
                    minCol = col;
                    c = boxesColor.get(col);
                }
            }
            minDstGoal[row] = minDst;
            usedBoxes[minCol] = 1;
            boxIds[row] = minCol;
            boxIdColor[row] = c;

        }




//        int[][] pdistBoxAgents = new int[boxes.size()][s.agentRows.length];

        int[] minDstag = new int[s.agentRows.length];

        // for each agent find furthest box
        for(int i = 0; i < s.agentRows.length; i++) {
            int dst = Integer.MIN_VALUE;
            Color agc = s.agentColors[i];
            int bid = 0;
            int dstagentbox = 0;
            for(int j = 0; j < minDstGoal.length; j++) {
                Color bcolor = boxIdColor[j];
                if(agc == bcolor) {
                    if(dst  < minDstGoal[j]) {
                        dst = minDstGoal[j];
                        bid = boxIds[j];
                    }
                }
            }

            minDstag[i] = pdistBoxAgents[bid][i];

        }

        if(b % 10000 == 0) {
            System.out.println(Arrays.toString(minDstag));
            System.out.println(Arrays.toString(minDstGoal));
            System.out.println("-----------------------");
        }
        int cost1 = 0;

        for (int i = 0; i < minDstGoal.length; i++) {
            cost1 += minDstGoal[i];
        }

        int cost2 = 0;
        for (int i = 0; i < minDstag.length; i++) {
            cost2 += minDstag[i];;

        }

        b++;

        return (cost1 + cost2);
















//        int cost = 0;
//        int min_val = Integer.MIN_VALUE;
//        for (int i = 0; i < minDstGoal.length; i++) {
//            cost += minDstGoal[i];
//        }
//
//        for (int i = 0; i < minDstag.length; i++) {
//
//            cost += minDstag[i];;
//
//        }
//        cost += min_val;
//        return cost;


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




