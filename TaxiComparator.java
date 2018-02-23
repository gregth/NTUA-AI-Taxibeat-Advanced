import java.util.*;

class TaxiComparator implements Comparator<Taxi> {
    @Override
    public int compare(Taxi a, Taxi b) {
        if (a.getRating() < b.getRating()) {
            return 1;
        }

        if (a.getRating() > b.getRating()) {
            return -1;
        }

        return 0;
    }
}
