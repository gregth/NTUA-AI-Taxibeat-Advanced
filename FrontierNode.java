import java.util.*;

class FrontierNode {
    private GraphConnection connection;
    private ArrayList<String> route;

    public FrontierNode(GraphConnection connection, String prevNode) {
       this.connection = connection;
       this.route = new ArrayList<String>();
       this.route.add(prevNode);
    }

    public FrontierNode(GraphConnection connection) {
       this.connection = connection;
       this.route = new ArrayList<String>();
    }

    public GraphConnection getConnection() {
        return connection;
    }

    public ArrayList<String> getRoute() {
        return route;
    }

    public void printRoute() {
        for (String routeNode : route) {
            System.out.print(routeNode + ", ");
        }
        System.out.println("");
    }
}
