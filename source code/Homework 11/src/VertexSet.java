import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.oocourse.specs3.models.Path;

public class VertexSet {
    private HashMap<Vertex, Integer> vertexCounter;//vertex & its count
    private HashSet<Vertex> vers;//list of vers
    private HashSet<Vertex> cleanVers;
    
    VertexSet() {
        vertexCounter = new HashMap<>();
        vers = new HashSet<>();
        cleanVers = new HashSet<>();
    }
    
    void add(Path path, int pathId) {
        for (int i : path) {
            Vertex v1 = new Vertex(i, pathId);
            Vertex v2 = new Vertex(i, 0);
            if (vertexCounter.containsKey(v1)) {
                int count = vertexCounter.get(v1);
                vertexCounter.replace(v1, count, count + 1);
            } else {
                vertexCounter.put(v1, 1);
                vers.add(v1);
            }
            if (vertexCounter.containsKey(v2)) {
                int count = vertexCounter.get(v2);
                vertexCounter.replace(v2, count, count + 1);
            } else {
                vertexCounter.put(v2, 1);
                vers.add(v2);
            }
            if (!cleanVers.contains(v2)) {
                cleanVers.add(v2);
            }
        }
    }
    
    void remove(Path path, int pathId) {
        for (int i : path) {
            Vertex v1 = new Vertex(i, pathId);
            Vertex v2 = new Vertex(i, 0);
            int count1 = vertexCounter.get(v1);
            if (count1 == 1) {
                vertexCounter.remove(v1);
                vers.remove(v1);
            } else {
                vertexCounter.replace(v1, count1, count1 - 1);
            }
            int count2 = vertexCounter.get(v2);
            if (count2 == 1) {
                vertexCounter.remove(v2);
                vers.remove(v2);
                cleanVers.remove(v2);
            } else {
                vertexCounter.replace(v2, count2, count2 - 1);
            }
        }        
    }

    int getDistinctNodeCount() {
        return cleanVers.size();
    }
    
    boolean containsNode(int nodeId) {
        return cleanVers.contains(new Vertex(nodeId));
    }
    
    public Iterator<Vertex> iterator() {
        return vers.iterator();
    }
    
    public Iterator<Vertex> cleanIterator() {
        return cleanVers.iterator();
    }

    public HashSet<Vertex> getCleanVers() {
        return cleanVers;
    }
}
