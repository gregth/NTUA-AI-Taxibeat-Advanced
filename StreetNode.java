public class StreetNode extends Position {
    public boolean isIntersection = false;

    public StreetNode(double x, double y) {
        super(x, y);
    }

    public void setIntersection() {
        this.isIntersection = true;
    }
}
