import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class ParseTaxis {
    public static ArrayList<Taxi> parse() {
        BufferedReader reader = null;
        ArrayList<Taxi> fleet = new ArrayList<Taxi>();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/taxis.csv"))));

            double x, y;
            int id;
            String line = null;
            String[] numbers = null;
            reader.readLine(); // skip the first line with the captions
            Taxi taxi = null;
            while ((line = reader.readLine()) != null) {
                numbers = line.split(",");

                x = Double.valueOf(numbers[0].trim());
                y = Double.valueOf(numbers[1].trim());
                id = Integer.valueOf(numbers[2].trim());

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