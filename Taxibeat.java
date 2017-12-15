import java.io.*;
import java.util.*;

public class Taxibeat {
    public static void main(String[] args) {
        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = Taxi.parse();
        Client clientPosition = Client.parse();

        myWorld.parseNodes();
        HashMap<String, Node> searchSpace = myWorld.generateSearchSpace(clientPosition);

        int maxFrontier = Integer.valueOf(args[0]);

        Comparator<Route> routeComparator = new RouteComparator();
        SortedSet<Route> routes = new TreeSet<Route>(routeComparator);
        double minRouteDistance = -1;
        System.out.println("DriverID, Steps, ActualMaxFrontier, MaxFrontier");

        for (Taxi taxi : fleet) {
            Node driverNode = myWorld.closestNode(taxi);
            System.out.print(taxi.getId() + ",");
            Route route = findRoute(searchSpace, driverNode, maxFrontier);
            if (route != null) {
                route.assignDriver(taxi);
                routes.add(route);
            }
        }
        XMLFile outFile = new XMLFile("output/out" + maxFrontier + ".kml");
        outFile.write(routes);
    }

    private static Route findRoute(HashMap<String, Node> searchSpace, Position startPosition, int maxFrontier) {
        Comparator<FrontierNode> comparator = new FrontierNodeComparator();
        PriorityQueue<FrontierNode> queue = new PriorityQueue<FrontierNode>(10, comparator);
        Set<String> visited = new TreeSet<String>();

        for (GraphEdge neighbor : searchSpace.get(startPosition.stringify()).getNeighbors()) {
            queue.add(new FrontierNode(neighbor, startPosition.stringify()));
        }
        visited.add(startPosition.stringify());

        FrontierNode top = null;
        FrontierNode frontier;
        ArrayList<String> route;
        int counter, stepsCounter = 0, actualMaxFrontier = 0;
        boolean foundRoute = false;
        PriorityQueue<FrontierNode> cloneQueue;
        while (queue.size() > 0) {
            if (queue.size() > actualMaxFrontier) {
                actualMaxFrontier = queue.size();
            }

            ++stepsCounter;
            top = queue.poll();

            visited.add(top.getEdge().getNode().stringify());

            if (top.getEdge().isGoal()) {
                foundRoute = true;
                break;
            }


            for (GraphEdge neighbor : searchSpace.get(top.getEdge().getNode().stringify()).getNeighbors()) {
                if (!visited.contains(neighbor.getNode().stringify())) {
                    frontier = new FrontierNode(neighbor);
                    frontier.setCost(top.getRouteCost() + neighbor.getWeight());
                    route = frontier.getRoute();
                    route.addAll(top.getRoute());
                    route.add(top.getEdge().getNode().stringify());

                    if (queue.contains(frontier)) {
                        // Check if the same neighbor is already inside the frontier
                        Iterator<FrontierNode> iterator = queue.iterator();
                        while (iterator.hasNext()) {
                            // Find the neighbor in the frontier to compare the cost
                            FrontierNode currentNode = iterator.next();
                            if (currentNode.getEdge().getNode().stringify().equals(neighbor.getNode().stringify())) {
                                if (currentNode.getRouteCost() > frontier.getRouteCost()) {
                                    // if the cost of reaching the neigbor from the current node
                                    // is less, replace the old one from the frontier
                                    queue.remove(currentNode);
                                    queue.add(frontier);
                                }
                                break;
                            }
                        }
                    } else {
                        queue.add(frontier);
                    }
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
            // Print statistics
            System.out.print(stepsCounter + ",");
            System.out.print(actualMaxFrontier + ",");
            System.out.print(maxFrontier + "\n");
            top.getRoute().add(top.getEdge().getNode().stringify());
            return new Route(top.getRoute(), top.getRouteCost());
        }

        return null;
    }
}
