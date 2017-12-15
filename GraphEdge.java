public class GraphEdge {
    private Node leadsToNode;
    private double weight;
    private double h;

    public GraphEdge(Node node, double weight, double h) {
        this.leadsToNode = node;
        this.weight = weight;
        this.h = h;
    }

    public boolean isGoal() {
        return this.h == 0;
    }

    public Node getNode() {
        return leadsToNode;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeuristic() {
        return h;
    }

    public double getTotalWeight() {
        return weight + h;
    }

    public void print() {
        System.out.println("('" + leadsToNode.stringify() + ", " + this.weight + ", " + this.h + ")");
    }
}
