public class GraphConnection {
    private GraphNode node;
    private Integer streetID;
    private double weight;
    private double h;

    public GraphConnection(GraphNode node, Integer streetID, double weight, double h) {
        this.node = node;
        this.streetID = streetID;
        this.weight = weight;
        this.h = h;
    }

    public GraphNode getNode() {
        return node;
    }

    public double getTotalWeight() {
        return weight + h;
    }

    public void print() {
        System.out.println("('" + node.getPosition().stringify() + ", " + this.weight + ", " + this.h + ")");
    }
}
