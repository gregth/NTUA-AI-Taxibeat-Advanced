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

    public static ArrayList<Taxi> parse(PrologParser prologSystem) {
        BufferedReader reader = null;
        ArrayList<Taxi> fleet = new ArrayList<Taxi>();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/" + Taxibeat.taxisFile))));

            double x, y, rating;
            int id;
            String line = null, type, available, longDistance;
            String[] numbers = null, languages, capacity;
            reader.readLine(); // skip the first line with the captions
            Taxi taxi = null;
            while ((line = reader.readLine()) != null) {
                numbers = line.split(",");

                x = Double.valueOf(numbers[0].trim());
                y = Double.valueOf(numbers[1].trim());
                id = Integer.valueOf(numbers[2].trim());
                available = numbers[3].trim();
                capacity = numbers[4].trim().split("-");
                languages = numbers[5].trim().split("\\|");
                rating = Double.valueOf(numbers[6].trim());
                longDistance = numbers[7].trim();
                type = numbers[8].trim();

                String predicate = "taxi(" + x + "," + y + "," + id + "," + available + "," + rating + "," + longDistance + "," + type + ")";
                prologSystem.asserta(predicate);

                for (String language : languages) {
                    prologSystem.asserta("speaksDriver(" + id + "," + language + ")");
                }

                prologSystem.asserta("minPassengers(" + id + "," + capacity[0] + ")");
                prologSystem.asserta("maxPassengers(" + id + "," + capacity[1] + ")");

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
