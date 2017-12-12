import java.util.*;

public class GraphNode {
    private Position position;
    private boolean goal = false;
    private boolean visited = false;

    @Override
    public boolean equals(Object b) {
        // self check
        if (this == b) {
            return true;
        }
        // null check
        if (b == null) {
            return false;
        }
        // type check and cast
        if (getClass() != b.getClass()) {
            return false;
        }

        GraphNode node = (GraphNode) b;
        return position.distanceTo(node.getPosition()) == 0; 
    }

    @Override
    public int hashCode() {
        return Objects.hash(position.getX(), position.getY());
    }

    public GraphNode(Position position) {
        this.position = position;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited() {
        visited = true;
    }

    public Position getPosition() {
        return position;
    }

    public void setGoal() {
        goal = true;
    }

    public boolean isGoal() {
        return goal;
    }

    public void print() {
        System.out.println("('" + this.position.stringify() + "', " + this.goal + ")");
    }
}

