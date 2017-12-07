public class GraphNode {
    private String position;
    private double weight;

    public GraphNode(String position, double weight) {
        this.position = position;
        this.weight = weight;
    }

    public void print() {
        System.out.println("('" + this.position + "', " + this.weight + ")");
    }
}

