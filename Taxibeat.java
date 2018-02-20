import java.io.*;
import java.util.*;

public class Taxibeat {
    public static String taxisFile;
    public static String clientFile;
    public static String nodesFile;

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: Taxibeat <MAX_FRONTIER> <NODES_FILE.csv> <TAXIS_FILE.csv> <CLIENTS_FILE.csv>");
            System.out.println("Hint: Input must be located in data/ directory");
            System.exit(2);
        }

        int maxFrontier = Integer.valueOf(args[0]);
        nodesFile = new String(args[1]);
        taxisFile = new String(args[2]);
        clientFile = new String(args[3]);

        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = Taxi.parse();
        Client clientPosition = Client.parse();

        myWorld.parseNodes();

        Comparator<Route> routeComparator = new RouteComparator();
        SortedSet<Route> routes = new TreeSet<Route>(routeComparator);
        double minRouteDistance = -1;
        System.out.println("\n\n****PRINTING STATISTICS****\n");
        System.out.println("DriverID, Steps, ActualMaxFrontier, MaxFrontier");

        SearchSpace searchSpace = myWorld.generateSearchSpace(clientPosition);
        for (Taxi taxi : fleet) {
            searchSpace.clean();
            Node driverNode = myWorld.closestNode(taxi);
            System.out.print(taxi.getId() + ",");
            Route route = findRoute(searchSpace, driverNode, maxFrontier);
            if (route != null) {
                route.assignDriver(taxi);
                routes.add(route);
            }
        }

        System.out.println("\nSelected Driver ID: " + routes.first().getDriver().getId()
                + " with total cost: " + routes.first().getCost() + " Kilometers.");
        XMLFile outFile = new XMLFile("output/out-" +
                Taxibeat.nodesFile.replace(".csv","").replace("/","-") + "-" + maxFrontier + ".kml");
        outFile.write(routes);

    }

    private static Route findRoute(SearchSpace searchSpace, Position startPosition, int maxFrontier) {
        HashMap<String, SearchNode> searchSpaceMap = searchSpace.getMap();
        Comparator<SearchNode> comparator = new SearchNodeComparator();
        SortedSet<SearchNode> queue = new TreeSet<SearchNode>(comparator);
        Set<String> visited = new TreeSet<String>();
        HashMap<String,String> inQueueHash = new HashMap<String, String>();

        SearchNode startNode = searchSpaceMap.get(startPosition.stringify());
        for (GraphEdge neighbor : (startNode.getNeighbors())) {
            SearchNode theNode = neighbor.getNode();
            theNode.setCost(neighbor.getWeight());
            theNode.setPrevious(startNode);
            searchSpace.setDirtyEntry(theNode);
            queue.add(theNode);
            inQueueHash.put(theNode.stringify(), "true");
        }
        visited.add(startPosition.stringify());

        SearchNode top = null;
        SearchNode frontier;
        ArrayList<String> route;
        int counter, stepsCounter = 0, actualMaxFrontier = 0;
        boolean foundRoute = false;
        while (queue.size() > 0) {
            if (queue.size() > actualMaxFrontier) {
                actualMaxFrontier = queue.size();
            }

            ++stepsCounter;
            top = queue.first();
            queue.remove(top);
            inQueueHash.remove(top.stringify());
            visited.add(top.stringify());

            if (top.isGoal()) {
                foundRoute = true;
                break;
            }

            for (GraphEdge neighbor : top.getNeighbors()) {
                SearchNode theNode = neighbor.getNode();
                if (!visited.contains(theNode.stringify())) {
                    double theCost = top.getRouteCost() + neighbor.getWeight();
                    if (inQueueHash.containsKey(theNode.stringify())) {
                        if (theCost < theNode.getRouteCost()) {
                            /* WARRING: Strange it seems, but if you don't remove the
                             * node before updating its members, the next call of queue.remove()
                             * can't find the element for removal. A major loop bug was introduced
                             * by this, now solved
                             */
                            queue.remove(theNode);
                            theNode.setCost(theCost);
                            theNode.setPrevious(top);
                            searchSpace.setDirtyEntry(theNode);
                            queue.add(theNode);
                        }
                    } else {
                        theNode.setPrevious(top);
                        theNode.setCost(theCost);
                        searchSpace.setDirtyEntry(theNode);
                        queue.add(theNode);
                        inQueueHash.put(theNode.stringify(), "a");
                    }
                }
            }

            if (queue.size() > maxFrontier) {
                inQueueHash.remove(queue.last().stringify());
                queue.remove(queue.last());
            }
        }

        if (foundRoute) {
            // Print statistics
            System.out.print(stepsCounter + ",");
            System.out.print(actualMaxFrontier + ",");
            System.out.print(maxFrontier + "\n");
            return new Route(top, top.getRouteCost());
        } else {
            System.out.print(stepsCounter + ",");
            System.out.print("FAIL,");
            System.out.print(maxFrontier + "\n");
        }

        return null;
    }
}
