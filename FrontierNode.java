import java.util.*;

class FrontierNode {
    private GraphConnection connection;
    private ArrayList<String> route;
    private double routeCost;

    public FrontierNode(GraphConnection connection, String prevNode) {
       this.connection = connection;
       this.route = new ArrayList<String>();
       this.route.add(prevNode);
       this.routeCost = connection.getWeight();
    }

    public FrontierNode(GraphConnection connection) {
       this.connection = connection;
       this.route = new ArrayList<String>();
       this.routeCost = 0;
    }

    public GraphConnection getConnection() {
        return connection;
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
