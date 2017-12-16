import java.util.*;

public class SearchSpace {
    public HashMap<String, SearchNode> searchMap;
    private ArrayList<SearchNode> dirtyNodes;

    public SearchSpace(HashMap<String, SearchNode> searchMap){
        dirtyNodes = new ArrayList<SearchNode>();
        this.searchMap = searchMap;
    }

    public HashMap<String, SearchNode> getMap() {
        return this.searchMap;
    }

    public void setDirtyEntry(SearchNode node) {
        dirtyNodes.add(node);
    }

    public boolean isDirty() {
        return dirtyNodes.size() > 0;
    }

    public void clean() {
        for (SearchNode dirtyNode : dirtyNodes) {
            dirtyNode.setCost(0);
            dirtyNode.setPrevious(null);
        }
        dirtyNodes.clear();
    }

    public void print() {
        ArrayList<GraphEdge> neighbors;

        for(String currentKey : searchMap.keySet()) {
            System.out.println("Neighbors of " + currentKey);

            SearchNode node = searchMap.get(currentKey);
            for (GraphEdge neighbor : node.getNeighbors()) {
                neighbor.getNode().print();
            }
        }
    }
}
