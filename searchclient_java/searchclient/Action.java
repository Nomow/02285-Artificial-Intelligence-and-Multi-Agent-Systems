package searchclient;

enum ActionType
{
    NoOp,
    Move
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
    MoveW("Move(W)", ActionType.Move, 0, -1, 0, 0);

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
