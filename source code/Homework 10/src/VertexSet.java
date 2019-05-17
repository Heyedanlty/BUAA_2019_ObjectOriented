import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.oocourse.specs2.models.Path;

class VertexSet {
    private HashMap<Integer, Integer> vertexCounter;//vertex & its count
    private ArrayList<Integer> vers;//list of vers
    
    VertexSet() {
        vertexCounter = new HashMap<>();
        vers = new ArrayList<>();
    }
    
    void add(Path path) {
        for (int i : path) {
            if (vertexCounter.containsKey(i)) {                        
                int count = vertexCounter.get(i);
                vertexCounter.replace(i, count, count + 1);
            } else {
                vertexCounter.put(i,1);
                vers.add(i);
            }
        }
    }
    
    void remove(Path path) {
        for (int i : path) {
            int count = vertexCounter.get(i);
            if (count == 1) {
                vertexCounter.remove(i);
                vers.remove((Integer)i);
            } else {
                vertexCounter.replace(i, count, count - 1);
            }
        }
    }
    
    int getDistinctNodeCount() {
        return vers.size();
    }

    boolean containsNode(int nodeId) {
        return vertexCounter.containsKey(nodeId);
    }
    
    public Iterator<Integer> iterator() {
        return vers.iterator();
    }
}
