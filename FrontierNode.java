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
}
