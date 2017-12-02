import java.io.*;
import java.util.Scanner;

public class ParseTaxis {
    public static void main(String args[]) throws IOException {
    BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("taxis.csv"))));

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

                taxi = new Taxi(id, x, y);
                taxi.printTaxi();

                //System.out.println("x:" + x + " y:" + y + " id:" + id);
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
    }
}
