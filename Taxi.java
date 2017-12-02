import java.io.*;

public class Taxi {
    int id;
    double x;
    double y;

    public Taxi(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void printTaxi() {
        System.out.println("Taxi: " + id + ", x: " + x + " y: " + y);
    }
}
