import java.util.*;

public class SearchSpace {
    public HashMap<String, SearchNode> theSearchSpace;
    private ArrayList<SearchNode> dirtyNodes;

    public SearchSpace(HashMap<String, SearchNode> theSearchSpace){
        dirtyNodes = new ArrayList<SearchNode>();
        this.theSearchSpace = theSearchSpace;
    }

    public HashMap<String, SearchNode> getMap() {
        return this.theSearchSpace;
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
}
