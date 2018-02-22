import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Client extends Position {
    int id;

    public Client(double x, double y) {
        super(x, y);
    }

    public void printClient() {
        System.out.println("Client: " + id );
        this.print();
    }

    public static Client parse(PrologParser prologSystem) {
        BufferedReader reader = null;

        Client position = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/" + Taxibeat.clientFile))));

            double x, y, xDest, yDest;
            int id, persons, luggage;
            String[] numbers = null;
            String time, language;

            reader.readLine(); // skip the first line with the captions
            String line = reader.readLine();
            numbers = line.split(",");

            x = Double.valueOf(numbers[0].trim());
            y = Double.valueOf(numbers[1].trim());
            xDest = Double.valueOf(numbers[2].trim());
            yDest = Double.valueOf(numbers[3].trim());
            time = numbers[4];
            persons = Integer.valueOf(numbers[5].trim());
            language = numbers[6];
            luggage = Integer.valueOf(numbers[7].trim());

            String predicate = "client(" + x + "," + y + "," + xDest + "," + yDest + "," + time + "," + persons + "," + language + "," + luggage + ")";
            prologSystem.asserta(predicate);

            position = new Client(x, y);
        } catch (IOException e) {
            System.err.println("Exception:" + e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Exception:" + e.toString());
                }
            }
        }

        return position;
    }
}
