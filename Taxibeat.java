import java.io.*;
import java.util.*;

public class Taxibeat {
    public static void main(String[] args) {
        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = Taxi.parse();
        Client clientPosition = Client.parse();

        myWorld.parseNodes();
        HashMap<String, ArrayList<GraphConnection>> searchSpace = myWorld.generateSearchSpace(clientPosition);

        Comparator<Route> routeComparator = new RouteComparator();
        SortedSet<Route> routes = new TreeSet<Route>(routeComparator);
        double minRouteDistance = -1;
        for (Taxi taxi : fleet) {
            Node driverNode = myWorld.closestNode(taxi);
            Route route = findRoute(searchSpace, driverNode);
            //route.print();
            if (route != null) {
                route.assignDriver(taxi);
                routes.add(route);
            }
        }
        System.out.println("min");
        XMLFile.getInstance().write(routes);
    }

    private static Route findRoute(HashMap<String, ArrayList<GraphConnection>> searchSpace, Position startPosition) {
        Comparator<FrontierNode> comparator = new FrontierNodeComparator();
        SortedSet<FrontierNode> queue = new TreeSet<FrontierNode>(comparator);
        Set<String> visited = new TreeSet<String>();

        for (GraphConnection neighbor : searchSpace.get(startPosition.stringify())) {
            queue.add(new FrontierNode(neighbor, startPosition.stringify()));
        }
        visited.add(startPosition.stringify());

        System.out.println("SOLVING");
        FrontierNode top = null;
        FrontierNode frontier;
        ArrayList<String> route;
        int maxFrontier = 5;
        boolean foundRoute = false;
        while (queue.size() > 0) {
            top = queue.first();

            visited.add(top.getConnection().getNode());

            if (top.getConnection().isGoal()) {
                foundRoute = true;
                System.out.println("FOUND THE CLIENT!!!");
                break;
            }


            for (GraphConnection neighbor : searchSpace.get(top.getConnection().getNode())) {
                if (!visited.contains(neighbor.getNode())) {
                    frontier = new FrontierNode(neighbor);
                    frontier.setCost(top.getRouteCost() + neighbor.getWeight());
                    route = frontier.getRoute();
                    route.addAll(top.getRoute());
                    route.add(top.getConnection().getNode());

                    queue.add(frontier);
                }
            }

            queue.remove(top);

            while (queue.size() > maxFrontier) {
                queue.remove(queue.last());
            }
        }

        if (foundRoute) {
            // found the client
            top.getConnection().print();
            top.getRoute().add(top.getConnection().getNode());
            return new Route(top.getRoute(), top.getRouteCost());
        }

        return null;
    }
}

class FrontierNodeComparator implements Comparator<FrontierNode> {
    @Override
    public int compare(FrontierNode a, FrontierNode b) {
        if (a.getConnection().getTotalWeight() < b.getConnection().getTotalWeight()) {
            return -1;
        }

        if (a.getConnection().getTotalWeight() > b.getConnection().getTotalWeight()) {
            return 1;
        }

        return 0;
    }
}

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
