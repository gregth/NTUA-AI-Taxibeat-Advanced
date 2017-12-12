public class GraphNode {
    private String position;
    private double weight;
    private double h;
    private boolean goal = false;
    private boolean visited = false;

    public GraphNode(String position, double weight, double h) {
        this.position = position;
        this.weight = weight;
        this.h = h;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited() {
        visited = true;
    }

    public String getPosition() {
        return position;
    }

    public void setGoal() {
        goal = true;
    }

    public boolean isGoal() {
        return goal;
    }

    public double getTotalWeight() {
        return weight + h;
    }

    public void print() {
        System.out.println("('" + this.position + "', " + this.weight + ", " + this.h + ", " + this.goal + ")");
    }
}

