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
        XMLFile.getInstance().write(routes);
    }

    private static Route findRoute(HashMap<String, ArrayList<GraphEdge>> searchSpace, Position startPosition) {
        Comparator<FrontierNode> comparator = new FrontierNodeComparator();
        PriorityQueue<FrontierNode> queue = new PriorityQueue<FrontierNode>(10, comparator);
        Set<String> visited = new TreeSet<String>();

        for (GraphEdge neighbor : searchSpace.get(startPosition.stringify())) {
            queue.add(new FrontierNode(neighbor, startPosition.stringify()));
        }
        visited.add(startPosition.stringify());

        FrontierNode top = null;
        FrontierNode frontier;
        ArrayList<String> route;
        int maxFrontier = 10, counter;
        boolean foundRoute = false;
        PriorityQueue<FrontierNode> cloneQueue;
        while (queue.size() > 0) {
            top = queue.poll();

            visited.add(top.getEdge().getNode());

            if (top.getEdge().isGoal()) {
                foundRoute = true;
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

            if (queue.size() > maxFrontier) {
                cloneQueue = new PriorityQueue<FrontierNode>(10, comparator); 
                counter = maxFrontier;
                while (counter > 0) {
                    cloneQueue.add(queue.poll());
                    --counter;
                }
                queue = cloneQueue;
            }
        }

        if (foundRoute) {
            top.getRoute().add(top.getEdge().getNode());
            return new Route(top.getRoute(), top.getRouteCost());
        }

        return null;
    }
}

class FrontierNodeComparator implements Comparator<FrontierNode> {
    @Override
    public int compare(FrontierNode a, FrontierNode b) {
        double aG = a.getRouteCost(), bG = b.getRouteCost();
        double aH = a.getEdge().getHeuristic(), bH = b.getEdge().getHeuristic();

        if (aG + aH < bG + bH) {
            return -1;
        }

        if (aG + aH > bG + bH) {
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
