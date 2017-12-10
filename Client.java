import java.io.*;

public class Client extends Position {
    int id;

    public Client(double x, double y) {
        super(x, y);
    }

    public void printClient() {
        System.out.println("Client: " + id );
        this.print();
    }
}
