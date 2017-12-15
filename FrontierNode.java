import java.util.*;

class FrontierNode {
    private GraphEdge edge;
    private ArrayList<String> route;
    private double routeCost;

    public FrontierNode(GraphEdge edge, String prevNode) {
       this.edge = edge;
       this.route = new ArrayList<String>();
       this.route.add(prevNode);
       this.routeCost = edge.getWeight();
    }

    public FrontierNode(GraphEdge edge) {
       this.edge = edge;
       this.route = new ArrayList<String>();
       this.routeCost = 0;
    }

    public GraphEdge getEdge() {
        return edge;
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

		final FrontierNode other = (FrontierNode) obj;

        return other.getEdge().getNode().stringify().equals(this.getEdge().getNode().stringify());
	}

	@Override
	public int hashCode() {
        return Objects.hash(this.getEdge().getNode());
	}
}
