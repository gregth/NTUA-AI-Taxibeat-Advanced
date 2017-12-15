import java.util.*;

public class Node extends Position {
    private String streetName;
    private int streetID;
    private ArrayList<GraphEdge> neighbors;

    public Node(double x, double y, String streetName, int streetID) {
        super(x, y);

        this.streetName = streetName;
        this.streetID = streetID;
        this.neighbors = new ArrayList<GraphEdge>();
    }

    public void addNeighbor(GraphEdge edge) {
        neighbors.add(edge);
    }

    public ArrayList<GraphEdge> getNeighbors() {
        return neighbors;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getStreetID() {
        return streetID;
    }
}
