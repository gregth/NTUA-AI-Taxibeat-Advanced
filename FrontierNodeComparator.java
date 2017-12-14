import java.util.*;

class FrontierNodeComparator implements Comparator<FrontierNode> {
    @Override
    public int compare(FrontierNode a, FrontierNode b) {
        double aG = a.getRouteCost(), bG = b.getRouteCost();
        double aH = a.getEdge().getHeuristic(), bH = b.getEdge().getHeuristic();

        if (a.getEdge().getNode().equals(b.getEdge().getNode())) {
            return 0;
        }

        if (aG + aH < bG + bH) {
            return -1;
        }

        return 1;
    }
}
