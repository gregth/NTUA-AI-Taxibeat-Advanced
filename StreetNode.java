public class StreetNode extends Position {
    private  boolean isIntersection = false;

    public StreetNode(double x, double y) {
        super(x, y);
    }

    public void setIntersection() {
        this.isIntersection = true;
    }

    public boolean isIntersection() {
        return this.isIntersection;
    }
}
