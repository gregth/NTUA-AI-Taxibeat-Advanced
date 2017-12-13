public class GraphConnection {
    private String node;
    private double weight;
    private double h;

    public GraphConnection(String node, double weight, double h) {
        this.node = node;
        this.weight = weight;
        this.h = h;
    }

    public void setHeuristic(double h) {
        this.h = h;
    }

    public boolean isGoal() {
        return this.h == 0;
    }

    public String getNode() {
        return node;
    }

    public double getWeight() {
        return weight;
    }

    public double getTotalWeight() {
        return weight + h;
    }

    public void print() {
        System.out.println("('" + node + ", " + this.weight + ", " + this.h + ")");
    }
}
