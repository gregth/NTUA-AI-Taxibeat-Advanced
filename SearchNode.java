import java.util.*;

public class SearchNode {
    Node positionNode;
    private ArrayList<GraphEdge> neighbors;
    private double routeCost;
    private boolean isGoal;
    private double h;
    SearchNode previous;

    public double getHeuristic() {
        return h;
    }

    public SearchNode(Node A, boolean isGoal, double h) {
        previous = null;
        this.neighbors = new ArrayList<GraphEdge>();
        routeCost = 0; //TODO
        this.isGoal = isGoal;
        this.h = h;
        positionNode = A;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setGoal(boolean value) {
        isGoal = value;
    }

    public void addNeighbor(GraphEdge edge) {
        neighbors.add(edge);
    }

    public ArrayList<GraphEdge> getNeighbors() {
        return neighbors;
    }

    public String stringify() {
        return positionNode.stringify();
    }

    public void setPrevious(SearchNode A) {
        this.previous = A;
    }

    public SearchNode getPrevious() {
        return this.previous;
    }

    public void setCost(double routeCost) {
        this.routeCost = routeCost;
    }

    public double getRouteCost() {
        return routeCost;
    }

    public void print() {
        System.out.print(this.stringify() + " ");
    }

    public void println() {
        System.out.println(this.stringify());
    }
}
