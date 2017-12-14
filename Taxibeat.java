import java.io.*;
import java.util.*;

public class Taxibeat {
    public static void main(String[] args) {
        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = Taxi.parse();
        Client clientPosition = Client.parse();

        myWorld.parseNodes();
        HashMap<String, ArrayList<GraphEdge>> searchSpace = myWorld.generateSearchSpace(clientPosition);

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

    private static Route findRoute(HashMap<String, ArrayList<GraphEdge>> searchSpace, Position startPosition) {
        Comparator<FrontierNode> comparator = new FrontierNodeComparator();
        SortedSet<FrontierNode> queue = new TreeSet<FrontierNode>(comparator);
        Set<String> visited = new TreeSet<String>();

        for (GraphEdge neighbor : searchSpace.get(startPosition.stringify())) {
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

            visited.add(top.getEdge().getNode());

            if (top.getEdge().isGoal()) {
                foundRoute = true;
                System.out.println("FOUND THE CLIENT!!!");
                break;
            }


            for (GraphEdge neighbor : searchSpace.get(top.getEdge().getNode())) {
                if (!visited.contains(neighbor.getNode())) {
                    frontier = new FrontierNode(neighbor);
                    frontier.setCost(top.getRouteCost() + neighbor.getWeight());
                    route = frontier.getRoute();
                    route.addAll(top.getRoute());
                    route.add(top.getEdge().getNode());

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
            top.getEdge().print();
            top.getRoute().add(top.getEdge().getNode());
            return new Route(top.getRoute(), top.getRouteCost());
        }

        return null;
    }
}
