import java.util.*;

class Route {
    private ArrayList<String> nodes;
    private double cost;

    public Route(ArrayList<String> nodes, double cost) {
        this.nodes = nodes;
        this.cost = cost;
    }

    public void print() {
        for (String routeNode : nodes) {
            System.out.print(routeNode + ", ");
        }
        System.out.println("\nCost: " + cost);
    }

    public void writeToXML() {
        XMLFile.getInstance().write(nodes);
    }
}
