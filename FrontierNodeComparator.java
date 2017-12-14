import java.util.*;

class FrontierNodeComparator implements Comparator<FrontierNode> {
    @Override
    public int compare(FrontierNode a, FrontierNode b) {
        if (a.getEdge().getTotalWeight() < b.getEdge().getTotalWeight()) {
            return -1;
        }

        if (a.getEdge().getTotalWeight() > b.getEdge().getTotalWeight()) {
            return 1;
        }

        return 0;
    }
}
