import java.io.*;

public class Taxi extends Position {
    int id;

    public Taxi(int id, double x, double y) {
        super(x, y);

        this.id = id;
    }

    public void printTaxi() {
        System.out.println("Taxi: " + id );
        this.print();
    }
}
