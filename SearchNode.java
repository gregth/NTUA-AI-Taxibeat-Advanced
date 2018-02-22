import java.util.*;

public class SearchNode {
    Node positionNode;
    private ArrayList<GraphEdge> neighbors;
    private double routeCost;
    private boolean isGoal;
    private double h;
    private double haversine;
    SearchNode previous;

    public double getHeuristic() {
        return h;
    }

    public double getHaversine() {
        return haversine;
    }

    public SearchNode(Node A, boolean isGoal, double haversine) {
        previous = null;
        this.neighbors = new ArrayList<GraphEdge>();
        routeCost = 0; //TODO
        this.isGoal = isGoal;
        this.haversine = haversine;
        // TODO
        this.h = haversine;
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

    public Node getNode() {
        return positionNode;
    }
}
