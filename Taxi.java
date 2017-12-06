import java.io.*;

public class Taxi {
    int id;
    Position position;

    public Taxi(int id, double x, double y) {
        this.id = id;
        this.position = new Position(x, y);
    }

    public void printTaxi() {
        System.out.println("Taxi: " + id );
        this.position.print();
    }
}
