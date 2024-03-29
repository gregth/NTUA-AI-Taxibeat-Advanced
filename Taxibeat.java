import java.io.*;
import java.util.*;

public class Taxibeat {
    public static String taxisFile;
    public static String clientFile;
    public static String nodesFile;
    public static String linesFile;
    public static String trafficFile;
    public static PrologParser prologSystem;

    public static void main(String[] args) {
        if (args.length != 6) {
            System.out.println("Usage: Taxibeat <MAX_FRONTIER> <NODES_FILE.csv> <TAXIS_FILE.csv> <CLIENTS_FILE.csv> <LINES_FILE.csv> <TRAFFIC_FILE.csv>");
            System.out.println("Hint: Input must be located in data/ directory");
            System.exit(2);
        }

        int maxFrontier = Integer.valueOf(args[0]);
        nodesFile = new String(args[1]);
        taxisFile = new String(args[2]);
        clientFile = new String(args[3]);
        linesFile = new String(args[4]);
        trafficFile = new String(args[5]);

        prologSystem = PrologParser.getInstance();
        // Use parser to add facts in //Prolog database

        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = Taxi.parse(prologSystem);
        Client clientPosition = Client.parse(prologSystem);

        myWorld.parseNodes();

        Line.parse();

        Traffic.parse();

        Comparator<Route> routeComparator = new RouteComparator();
        SortedSet<Route> routes = new TreeSet<Route>(routeComparator);
        double minRouteDistance = -1;

        SearchSpace searchSpace = myWorld.generateSearchSpace(clientPosition);

        Comparator<Taxi> taxiComparator = new TaxiComparator();
        SortedSet<Taxi> availableTaxis = new TreeSet<Taxi>(taxiComparator);
        for (Taxi taxi : fleet) {
            if (!prologSystem.isQualifiedDriver(taxi.getId())) {
                continue;
            }
            taxi.setRating(prologSystem.getDriverRank(taxi.getId()));
            searchSpace.clean();
            Node driverNode = myWorld.closestNode(taxi);
            Route route = findRoute(searchSpace, driverNode, maxFrontier);
            if (route != null) {
                route.assignDriver(taxi);
                routes.add(route);
                availableTaxis.add(taxi);
            }
        }

        System.out.println("Available taxis sorted by distance:");
        for (Route route : routes) {
            System.out.println(route.getDriver().getId() + " - " + route.getCost() + "km");
        }
        System.out.println("Available taxis sorted by rating:");
        for (Taxi taxi : availableTaxis) {
            System.out.println(taxi.getId() + " - Rating: " + taxi.getRating());
        }

        if (routes.size() > 0) {
            XMLFile outFile = new XMLFile("output/out-" +
                    Taxibeat.nodesFile.replace(".csv","").replace("/","-") + "-" + maxFrontier + ".kml");
            outFile.write(routes);
        }
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

            if (!prologSystem.canMoveFromTo(startNode, theNode)) {
                continue;
            }

            double weightFactor = prologSystem.calculateFactor(startNode, theNode);

            theNode.setFactor(weightFactor);
            theNode.setCost(weightFactor * neighbor.getWeight());
            theNode.setDistance(neighbor.getWeight());
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

                if (!prologSystem.canMoveFromTo(top, theNode)) {
                    continue;
                }

                if (!visited.contains(theNode.stringify())) {
                    double weightFactor = prologSystem.calculateFactor(top, theNode);
                    double theCost = top.getRouteCost() + weightFactor * neighbor.getWeight();
                    double theDistance = top.getDistance() + neighbor.getWeight();

                    if (inQueueHash.containsKey(theNode.stringify())) {
                        if (theCost < theNode.getRouteCost()) {
                            /* WARRING: Strange it seems, but if you don't remove the
                             * node before updating its members, the next call of queue.remove()
                             * can't find the element for removal. A major loop bug was introduced
                             * by this, now solved
                             */
                            queue.remove(theNode);
                            theNode.setFactor(weightFactor);
                            theNode.setCost(theCost);
                            theNode.setDistance(theDistance);
                            theNode.setPrevious(top);
                            searchSpace.setDirtyEntry(theNode);
                            queue.add(theNode);
                        }
                    } else {
                        theNode.setPrevious(top);
                        theNode.setFactor(weightFactor);
                        theNode.setCost(theCost);
                        theNode.setDistance(theDistance);
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
            return new Route(top, top.getDistance());
        }
        return null;
    }
}
