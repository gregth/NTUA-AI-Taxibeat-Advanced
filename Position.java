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

    public String stringify() {
        return this.x + " " + this.y;
    }

    public double distanceTo(Position b) {
       return Math.sqrt(Math.pow(this.x - b.x, 2) + Math.pow(this.y - b.y, 2));
    }
}
