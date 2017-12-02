public class Position {
    public double x;
    public double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void print() {
        System.out.println("x: " + x + " y: " + y);
    }
}
