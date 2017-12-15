import java.util.*;

class Route {
    private ArrayList<String> nodes;
    private double cost;
    private Taxi driver;
    private boolean min;

    public Route(SearchNode goal, double cost) {
        nodes = new ArrayList<String>();
        SearchNode current = goal;
        while (current != null) {
            nodes.add(0, current.stringify());
            current = current.getPrevious();
        }
        this.nodes = nodes;
        this.cost = cost;
    }

    public void assignDriver(Taxi driver) {
        this.driver = driver;
    }

    public Taxi getDriver() {
        return driver;
    }

    public void setMin() {
        this.min = true;
    }

    public boolean isMin() {
        return min;
    }

    public void print() {
        for (String routeNode : nodes) {
            System.out.print(routeNode + ", ");
        }
        System.out.println("\nCost: " + cost);
    }

    public ArrayList<String> getNodesString() {
        return nodes;
    }

    public double getCost() {
        return cost;
    }
}
