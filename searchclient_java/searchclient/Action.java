package searchclient;

enum ActionType
{
    NoOp,
    Move,
    Push,
    Pull,
}

public enum Action
{
    /*
        List of possible actions. Each action has the following parameters, 
        taken in order from left to right:
        1. The name of the action as a string. This is the string sent to the server
        when the action is executed. Note that for Pull and Push actions the syntax is
        "Push(X,Y)" and "Pull(X,Y)" with no spaces.
        2. Action type: NoOp, Move, Push or Pull (only NoOp and Move initially supported)
        3. agentRowDelta: the vertical displacement of the agent (-1,0,+1)
        4. agentColDelta: the horisontal displacement of the agent (-1,0,+1)
        5. boxRowDelta: the vertical displacement of the box (-1,0,+1)
        6. boxColDelta: the horisontal discplacement of the box (-1,0,+1) 
        Note: Origo (0,0) is in the upper left corner. So +1 in the vertical direction is down (S) 
        and +1 in the horisontal direction is right (E).
    */
    NoOp("NoOp", ActionType.NoOp, 0, 0, 0, 0),

    MoveN("Move(N)", ActionType.Move, -1, 0, 0, 0),
    MoveS("Move(S)", ActionType.Move, 1, 0, 0, 0),
    MoveE("Move(E)", ActionType.Move, 0, 1, 0, 0),
    MoveW("Move(W)", ActionType.Move, 0, -1, 0, 0),

    PushNN("Push(N,N)", ActionType.Push, -1, 0, -1, 0),
    PushNS("Push(N,S)", ActionType.Push, -1, 0, 1, 0),
    PushNE("Push(N,E)", ActionType.Push, -1, 0, 0, 1),
    PushNW("Push(N,W)", ActionType.Push, -1, 0, 0, -1),

    PushSN("Push(S,N)", ActionType.Push, 1, 0, -1, 0),
    PushSS("Push(S,S)", ActionType.Push, 1, 0, 1, 0),
    PushSE("Push(S,E)", ActionType.Push, 1, 0, 0, 1),
    PushSW("Push(S,W)", ActionType.Push, 1, 0, 0, -1),

    PushEN("Push(E,N)", ActionType.Push, 0, 1, -1, 0),
    PushES("Push(E,S)", ActionType.Push, 0, 1, 1, 0),
    PushEE("Push(E,E)", ActionType.Push, 0, 1, 0, 1),
    PushEW("Push(E,W)", ActionType.Push, 0, 1, 0, -1),

    PushWN("Push(W,N)", ActionType.Push, 0, -1, -1, 0),
    PushWS("Push(W,S)", ActionType.Push, 0, -1, 1, 0),
    PushWE("Push(W,E)", ActionType.Push, 0, -1, 0, 1),
    PushWW("Push(W,W)", ActionType.Push, 0, -1, 0, -1),

    PullNN("Pull(N,N)", ActionType.Pull, -1, 0, -1, 0),
    PullNS("Pull(N,S)", ActionType.Pull, -1, 0, 1, 0),
    PullNE("Pull(N,E)", ActionType.Pull, -1, 0, 0, 1),
    PullNW("Pull(N,W)", ActionType.Pull, -1, 0, 0, -1),

    PullSN("Pull(S,N)", ActionType.Pull, 1, 0, -1, 0),
    PullSS("Pull(S,S)", ActionType.Pull, 1, 0, 1, 0),
    PullSE("Pull(S,E)", ActionType.Pull, 1, 0, 0, 1),
    PullSW("Pull(S,W)", ActionType.Pull, 1, 0, 0, -1),

    PullEN("Pull(E,N)", ActionType.Pull, 0, 1, -1, 0),
    PullES("Pull(E,S)", ActionType.Pull, 0, 1, 1, 0),
    PullEE("Pull(E,E)", ActionType.Pull, 0, 1, 0, 1),
    PullEW("Pull(E,W)", ActionType.Pull, 0, 1, 0, -1),

    PullWN("Pull(W,N)", ActionType.Pull, 0, -1, -1, 0),
    PullWS("Pull(W,S)", ActionType.Pull, 0, -1, 1, 0),
    PullWE("Pull(W,E)", ActionType.Pull, 0, -1, 0, 1),
    PullWW("Pull(W,W)", ActionType.Pull, 0, -1, 0, -1);



    public final String name;
    public final ActionType type;
    public final int agentRowDelta; // vertical displacement of agent (-1,0,+1)
    public final int agentColDelta; // horisontal displacement of agent (-1,0,+1)
    public final int boxRowDelta; // vertical diplacement of box (-1,0,+1)
    public final int boxColDelta; // horisontal displacement of box (-1,0,+1)

    Action(String name, ActionType type, int ard, int acd, int brd, int bcd)
    {
        this.name = name;
        this.type = type;
        this.agentRowDelta = ard;
        this.agentColDelta = acd;
        this.boxRowDelta = brd;
        this.boxColDelta = bcd;
    }
}
