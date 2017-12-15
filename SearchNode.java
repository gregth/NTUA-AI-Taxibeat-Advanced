import java.util.*;

public class SearchNode {
    Node positionNode;
    private ArrayList<GraphEdge> neighbors;
    private ArrayList<String> route;
    private double routeCost;
    private boolean isGoal;

    public SearchNode(Node A, boolean isGoal) {
        this.neighbors = new ArrayList<GraphEdge>();
        routeCost = 0; //TODO
        route = new ArrayList<String>();
        this.isGoal = isGoal;
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

    public void setCost(double routeCost) {
        this.routeCost = routeCost;
    }

    public double getRouteCost() {
        return routeCost;
    }

    public ArrayList<String> getRoute() {
        return route;
    }

    public void printRoute() {
        for (String routeNode : route) {
            System.out.print(routeNode + ", ");
        }
        System.out.println("\nCost: " + this.routeCost);
    }

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

        if (obj == this) {
            return true;
        }

		final SearchNode other = (SearchNode) obj;

        return other.stringify().equals(this.stringify());
	}

	@Override
	public int hashCode() {
        return Objects.hash(this.stringify());
	}

}
