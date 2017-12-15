import java.util.*;

class FrontierNodeComparator implements Comparator<FrontierNode> {
    @Override
    public int compare(FrontierNode a, FrontierNode b) {
        double aG = a.getRouteCost(), bG = b.getRouteCost();
        double aH = a.getEdge().getHeuristic(), bH = b.getEdge().getHeuristic();

        if (a.getEdge().getNode().stringify().equals(b.getEdge().getNode().stringify())) {
            return 0;
        }

        if (aG + aH < bG + bH) {
            return -1;
        }

        return 1;
    }
}
