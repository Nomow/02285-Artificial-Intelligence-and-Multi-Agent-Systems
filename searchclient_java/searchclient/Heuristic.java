package searchclient;

import java.util.Comparator;

public abstract class Heuristic
        implements Comparator<State>
{
    public Heuristic(State initialState)
    {
        // Here's a chance to pre-process the static parts of the level.
    }

    public int h(State s)
    {
        int goal_counter = 0;
        for (int row = 1; row < s.goals.length - 1; row++)
        {
            for (int col = 1; col < s.goals[row].length - 1; col++)
            {
                char goal = s.goals[row][col];

                if (goal >= 'A' && goal <= 'Z' && s.boxes[row][col] != goal)
                {
                    goal_counter++;
                }
                if (goal >= '0' && goal <= '9' && s.agentRows[goal - '0'] != row && s.agentCols[goal - '0'] != col)
                {
                    goal_counter++;
                }
            }
        }
        return goal_counter;
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
