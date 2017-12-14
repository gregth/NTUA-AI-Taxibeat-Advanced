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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static Position parsePosition(String positionString) {
        String[] parts = positionString.split(" ");
        double x = Double.valueOf(parts[0].trim());
        double y = Double.valueOf(parts[1].trim());

        return new Position(x, y);
    }

    public double distanceTo(Position b) {
        return Haversine.distance(this.y, this.x, b.y, b.x);
    }
}
