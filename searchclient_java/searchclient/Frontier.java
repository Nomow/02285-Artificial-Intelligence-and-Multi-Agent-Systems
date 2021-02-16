package searchclient;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

public interface Frontier
{
    void add(State state);
    State pop();
    boolean isEmpty();
    int size();
    boolean contains(State state);
    String getName();
}

class FrontierBFS implements Frontier
{
    private final ArrayDeque<State> queue = new ArrayDeque<>(65536);
    private final HashSet<State> set = new HashSet<>(65536);

    @Override
    public void add(State state)
    {
        this.queue.addLast(state);
        this.set.add(state);
    }

    @Override
    public State pop()
    {
        State state = this.queue.pollFirst();
        this.set.remove(state);
        return state;
    }

    @Override
    public boolean isEmpty()
    {
        return this.queue.isEmpty();
    }

    @Override
    public int size()
    {
        return this.queue.size();
    }

    @Override
    public boolean contains(State state)
    {
        return this.set.contains(state);
    }

    @Override
    public String getName()
    {
        return "breadth-first search";
    }
}

class FrontierDFS implements Frontier
{
    private Stack<State> stack = new Stack<>();
    private HashSet<State> set = new HashSet<>(65536);


    @Override
    public void add(State state)
    {
        stack.add(state);
        set.add(state);
    }

    @Override
    public State pop()
    {
        State n = stack.pop();
        set.remove(n);
        return n;
    }

    @Override
    public boolean isEmpty()
    {
        return stack.isEmpty();
    }

    @Override
    public int size()
    {
        return stack.size();
    }

    @Override
    public boolean contains(State state)
    {
        return set.contains(state);
    }

    @Override
    public String getName()
    {
        return "depth-first search";
    }
}

class FrontierBestFirst implements Frontier
{
    private Heuristic heuristic;
    private PriorityQueue<State> priorityQueue;
    private HashSet<State> set;


    public FrontierBestFirst(Heuristic h)
    {
        this.heuristic = h;
        priorityQueue = new PriorityQueue<State>(this.heuristic);
        set = new HashSet<State>(65536);
    }

    @Override
    public void add(State state)
    {
        priorityQueue.add(state);
        set.add(state);
    }

    @Override
    public State pop()
    {
        State state = priorityQueue.poll();
        set.remove(state);
        return state;
    }

    @Override
    public boolean isEmpty()
    {
        return priorityQueue.isEmpty();
    }

    @Override
    public int size()
    {
        return priorityQueue.size();
    }

    @Override
    public boolean contains(State state)
    {
        return set.contains(state);
    }

    @Override
    public String getName()
    {
        return String.format("best-first search using %s", this.heuristic.toString());
    }
}
