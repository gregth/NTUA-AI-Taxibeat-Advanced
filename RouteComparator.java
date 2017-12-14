import java.util.*;

class RouteComparator implements Comparator<Route> {
    @Override
    public int compare(Route a, Route b) {
        if (a.getCost() < b.getCost()) {
            return -1;
        }

        if (a.getCost() > b.getCost()) {
            return 1;
        }

        return 0;
    }
}
