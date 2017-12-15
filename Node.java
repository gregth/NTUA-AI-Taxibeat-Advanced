import java.util.*;

public class Node extends Position {
    private String streetName;
    private int streetID;

    public Node(double x, double y, String streetName, int streetID) {
        super(x, y);

        this.streetName = streetName;
        this.streetID = streetID;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getStreetID() {
        return streetID;
    }
}
