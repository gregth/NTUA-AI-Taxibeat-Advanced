import java.io.*;
import java.util.Scanner;

public class ParseClient {
    public static Position parse() {
        BufferedReader reader = null;

        Position clientPosition = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/client.csv"))));

            double x, y;
            int id;
            String[] numbers = null;

            reader.readLine(); // skip the first line with the captions
            String line = reader.readLine();
            numbers = line.split(",");

            x = Double.valueOf(numbers[0].trim());
            y = Double.valueOf(numbers[1].trim());

            clientPosition = new Position(x, y);
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

        return clientPosition;
    }
}