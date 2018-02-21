import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

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

    public int getId() {
        return id;
    }

    public static ArrayList<Taxi> parse() {
        BufferedReader reader = null;
        ArrayList<Taxi> fleet = new ArrayList<Taxi>();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/" + Taxibeat.taxisFile))));

            double x, y, rating;
            int id;
            boolean available, longDistance;
            String line = null, languages, capacity, type;
            String[] numbers = null;
            reader.readLine(); // skip the first line with the captions
            Taxi taxi = null;
            while ((line = reader.readLine()) != null) {
                numbers = line.split(",");

                x = Double.valueOf(numbers[0].trim());
                y = Double.valueOf(numbers[1].trim());
                id = Integer.valueOf(numbers[2].trim());
                available = numbers[3].trim().equals("yes") ? true : false;
                capacity = numbers[4].trim();
                languages = numbers[5].trim();
                rating = Double.valueOf(numbers[6].trim());
                longDistance = numbers[7].trim().equals("yes") ? true : false;
                type = numbers[8].trim();

                //TODO: PARSE LANGUAGES
                String predicate = "taxi(" + x + "," + y + "," + id + "," + available + "," + capacity + "," + languages + "," + rating + "," + longDistance + "," + type + ")";

                fleet.add(new Taxi(id, x, y));
            }
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

        return fleet;
    }
}
